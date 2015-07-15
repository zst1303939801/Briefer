package com.practice.briefer.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 11个页签水平滑动的ViewPager
 * 
 * @author ZST
 *
 */
public class TopNewsViewPager extends ViewPager {

	int startX;
	int startY;
	int endX;
	int endY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 事件分发，这个是子，请求父亲以及祖宗不允许拦截我的touch事件 标签页中上部的那个ViewPager 1.
	 * 右划，而且是第一个页面，需要父控件拦截 2. 左划，而且是最后一个页面，需要父控件拦截 3. 上下滑动需要父控件拦截，把滑动权交给父亲
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// this.requestDisallowInterceptTouchEvent(true);
		// 请求不允许拦截Touch事件
		// 在news_menu_detai.xml中引用了这个自定义的类(这个ViewPager)，所以可以直接监听到ViewPager的状态，是在那个item(页面)里面
		// getParent().requestDisallowInterceptTouchEvent(true);//
		// 先拿到父亲，让父亲请求不允许拦截Touch事件，因为这个ViewPager外边有一层LinerLayout
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:// 起点坐标
			getParent().requestDisallowInterceptTouchEvent(true);// 默认开始不要拦截，这样为了保证ACTION_MOVE不被拦截

			startX = (int) ev.getRawX();// rawx是屏幕整体的位置，x是基于浮空的位置
			startY = (int) ev.getRawY();

			break;
		case MotionEvent.ACTION_MOVE:// 终点坐标

			endX = (int) ev.getRawX();
			endY = (int) ev.getRawY();

			// 根据x和y坐标的移动大小判断是左右移动还是上下移动
			// Math的abs方法是算出绝对值
			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑动，左右滑动距离大于上下距离
				if(endX>startX){//右滑
					if(getCurrentItem() == 0){
						getParent().requestDisallowInterceptTouchEvent(false);//拦截，当到第一个时候父控件是SlidingMenu这个ViewPager拦截滑出菜单
					}
				}else{//右滑
					if(getCurrentItem()==getAdapter().getCount()-1){
						getParent().requestDisallowInterceptTouchEvent(false);//最后一个下一个标签拦截，切换到下一个标签页面
					}
				}
			} else {
				getParent().requestDisallowInterceptTouchEvent(false);//上下滑动不需要拦截
			}

			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

}
