package com.practice.briefer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 11个页签水平滑动的ViewPager，由于在NewsMenuDetailPager.java中自定义了ViewPager的滑动时间，这个暂时不用
 * 
 * @author ZST
 *
 */
public class HorizontalViewPager extends ViewPager {

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HorizontalViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 事件分发，这个是子，请求父亲以及祖宗不允许拦截我的touch事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// this.requestDisallowInterceptTouchEvent(true);
		//请求不允许拦截Touch事件
		//在news_menu_detai.xml中引用了这个自定义的类(这个ViewPager)，所以可以直接监听到ViewPager的状态，是在那个item(页面)里面
		if (getCurrentItem() != 0) {
			getParent().requestDisallowInterceptTouchEvent(true);// 先拿到父亲，让父亲请求不允许拦截Touch事件，因为这个ViewPager外边有一层LinerLayout
		} else {
			getParent().requestDisallowInterceptTouchEvent(false);// 拦截
		}

		return super.dispatchTouchEvent(ev);
	}

}
