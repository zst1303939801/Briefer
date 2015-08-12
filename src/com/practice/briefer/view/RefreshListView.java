package com.practice.briefer.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.practice.briefer.R;

/**
 * 下拉刷新的ListView，在TabDetailPager.java中new这个对象以后这个对象就会被调用显示
 * 
 * @author ZST
 *
 */

public class RefreshListView extends ListView implements OnScrollListener,
		android.widget.AdapterView.OnItemClickListener {

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

	private View mFooterView;
	private int mFooterViewHeight;

	// 下面的是ListView自带的构造函数，当这个对象在别的地方被(TabDetailPager.java中)new以后，下面的方法会自动调用
	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeaderView();// 初始化头布局，在每个构造函数中都有，这样可以保证不会被遗忘
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
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

		tvTime.setText("刷新时间：" + getCurrentTime());// 初始化刷新时间
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(),
				R.layout.refresh_listview_footer, null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();

		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏

		this.setOnScrollListener(this);// 给this(ListView)设置一个滑动监听-让listView实现implements
										// OnScrollListener
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
				mListener.onRefresh();// 运行的时候由于OnRefreshListener是一个接口，所以实际上运行的是RefreshListView.java中调用这个接口的方法
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
	 * 下面是给出了一个接口，TabDetailPager.java中的RefreshListView
	 * .java对象调用调用这个setOnRefreshListener方法 接口的方法会先运行继承的那个类中的方法
	 */
	OnRefreshListener mListener;

	// 设置一个刷新的监听
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	// 下拉刷新接口
	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();// 当脚布局显示出来以后，需要调用新的接口请求数据，所以放出一个接口
	}

	/**
	 * 收起下拉刷新的控件
	 */
	public void onRefreshComplete(boolean success) {
		if (isLoadingMore) {// 正在加载更多...
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
			isLoadingMore = false;
		} else {
			mCurrentState = STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);

			myHeaderView.setPadding(0, -myHeaderViewHeight, 0, 0);// 隐藏

			if (success) {
				tvTime.setText("刷新时间：" + getCurrentTime());
			}
		}
	}

	/**
	 * 获取当前时间
	 */
	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// MM：年份从1月份开始，mm：年份从0开始，HH：24小时制，hh：12小时制
		return format.format(new Date());
	}

	/**
	 * 重写implements OnScrollListener的方法
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	private boolean isLoadingMore;// 默认值是false，这个变量只在这里用，所以可以临近放，符合谷歌规范

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// SCROLL_STATE_IDLE SCROLL_STATE_FLING
		// 是OnScrollListener里面的两个状态，滑动到底快速滑动
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {
			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
				System.out.println("到最后了.....");

				mFooterView.setPadding(0, 0, 0, 0);// ListView到底以后全部显示footer加载更多

				setSelection(getCount() - 1);// 由于下拉的时候不显示footer，再拉一下才显示，所以使用listView自带的方法setSelection把所有的都显示//改变ListView的显示位置

				isLoadingMore = true;// 显示一次，否则客户一直拉会一直运行这个代码

				// 接口不为空，代表这个接口被别人调用过
				if (mListener != null) {
					mListener.onLoadMore();// 调用自己放出的接口，寻找运行被调用的方法
				}
			}
		}
	}

	/**
	 * 重写listView 的 OnItemClickListener的方法 1.把setOnItemClickListener重写出来
	 * 2.把listener接收一下：mItemClickListener = listener; 系统默认会吧TabDetailPager
	 * new的那个OnItemClickListener传给底层，
	 */
	OnItemClickListener mItemClickListener;// 自己保留listener

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		// super.setOnItemClickListener(listener);//把listener传递给底层
		super.setOnItemClickListener(this);// 因为重写OnItemClickListener，所以要在本类中实现OnItemClickListener接口，把自己传递给底层，继承以后就会有一个下面的实现方法，所以并没有把用户在外边建的listener传给了底层，二十把自身传给了底层

		mItemClickListener = listener;// 把传递过来的listener接收一下,自己保留
	}

	// 继承OnItemClickListener以后实现的一个方法
	// 以后listView被点击之后，这个方法会相应，不是上面的那个相应
	// 回调自身的onItemClick
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);// getHeaderViewsCount：listView总共有多少headerview的数量
		}

	}

}
