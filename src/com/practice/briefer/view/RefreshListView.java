package com.practice.briefer.view;

import com.practice.briefer.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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

		myHeaderView.setPadding(0, -myHeaderViewHeight, 0, 0);
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
			break;
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			ivArrow.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
