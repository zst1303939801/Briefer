package com.practice.briefer.view;

import com.practice.briefer.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新的ListView
 * 
 * @author ZST
 *
 */

public class RefreshListView extends ListView {

	// 下拉刷新的三种状态
	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新

	private int mCurrentState = STATE_PULL_REFRESH;// 当前状态

	private View myHeaderView;
	private int myHeaderViewHeight;
	private int startY = -1;// 手指按下去的y坐标

	TextView tvTitle;
	TextView tvTime;
	ImageView ivArrow;
	ProgressBar pbProgress;

	private RotateAnimation animUp;
	private RotateAnimation animDown;

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeaderView();// 初始化头布局，在每个构造函数中都有，这样可以保证不会被遗忘
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
	}

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		myHeaderView = View
				.inflate(getContext(), R.layout.refresh_header, null);// getContext()拿到布局文件view对象
		this.addHeaderView(myHeaderView);// 因为重写了ListView，所以要把这个下拉的样式布局增加到自己写好的ListView里面，必须使用ListView的addHeaderView把myHeaderView增加到TabDetailPager.java中的lvList里面

		tvTitle = (TextView) myHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) myHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) myHeaderView.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) myHeaderView.findViewById(R.id.pb_progress);

		myHeaderView.measure(0, 0);// 先拿到measure
		myHeaderViewHeight = myHeaderView.getMeasuredHeight();

		myHeaderView.setPadding(0, -myHeaderViewHeight, 0, 0);// 隐藏头布局

		initArrowAnim();// 动画效果在初始化页面就应该调用
	}

	/**
	 * listView的触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下的时候的y坐标
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getRawY();
			}

			if (mCurrentState == STATE_REFRESHING) {// 正在刷新的时候再动界面则不做任何处理
				break;
			}

			// 手指移动的时候的结束坐标
			int endY = (int) ev.getRawY();

			int dy = endY - startY;// 移动偏移量

			if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且是第一个item才允许下拉
				int padding = dy - myHeaderViewHeight;
				myHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding

				if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {// 当全部拉下来以后状态改为松开刷新
					mCurrentState = STATE_RELEASE_REFRESH;
					refreshState();// 刷新
				} else if (padding < 0 && mCurrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
					mCurrentState = STATE_PULL_REFRESH;
					refreshState();// 刷新
				}
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 重置

			// 手指下来然后松开后让之变为刷新状态
			if (mCurrentState == STATE_RELEASE_REFRESH) {
				mCurrentState = STATE_REFRESHING;// 正在刷新
				myHeaderView.setPadding(0, 0, 0, 0);// 下拉界面全部显示则高度正好是下来界面的高度
				refreshState();// 刷新
			} else if (mCurrentState == STATE_PULL_REFRESH) {
				myHeaderView.setPadding(0, -myHeaderViewHeight, 0, 0);// 如果是手指下拉了一点没有把下拉刷新的全部显示出来，则让下拉刷新重新隐藏
			}
			break;
		default:
			break;

		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 刷新下拉控件的布局
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animDown);// 给图标设置一个动画效果
			break;
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animUp);// 给图标设置一个动画效果
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			ivArrow.clearAnimation();// 必须清楚动画才能隐藏
			ivArrow.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);

			if (mListener != null) {
				mListener.onRefresh();//运行的时候由于OnRefreshListener是一个接口，所以实际上运行的是RefreshListView.java中调用这个接口的方法
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化箭头动画，在页面初始化就应该调用
	 */
	private void initArrowAnim() {

		// 箭头向上动画
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);// 持续状态时间
		animUp.setFillAfter(true);// 状态是true保持

		// 箭头向下动画
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);// 持续状态时间
		animDown.setFillAfter(true);// 状态是true保持

	}

	/**
	 * 下面三个接口OnRefreshListener的方法用法很重要
	 * 下面是给出了一个接口，TabDetailPager.java中的RefreshListView.java对象调用调用这个setOnRefreshListener方法
	 * 接口的方法会先运行继承的那个类中的方法
	 */
	OnRefreshListener mListener;

	// 设置一个刷新的监听
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	// 下拉刷新接口
	public interface OnRefreshListener {
		public void onRefresh();
	}

}
