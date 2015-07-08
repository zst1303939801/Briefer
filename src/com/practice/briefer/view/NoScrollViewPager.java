package com.practice.briefer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能左右滑动的ViewPager
 * 
 * @author ZST
 *
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// 表示事件是否被拦截,默认是true表示是被拦截，不往下传递，设成false不被拦截往下传递，
	// 因为这个父ViewPager定义了onTouchEvent什么都不做，如果传递下去了则子类有可能受影响，现在不让传递给子类
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	/**
	 * 重写onTouchEvent事件，什么都不做
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
