package com.practice.briefer.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 * 
 * @author ZST
 *
 */
public abstract class BaseMenuDetailPager {
	
	public Activity myActivity;
	
	public View mRootView;//根布局对象
	
	public BaseMenuDetailPager(Activity activity){
		myActivity = activity;
		mRootView = initViews();
	}
	
	/**
	 * 初始化界面
	 */
	public abstract View initViews();
	
	/**
	 * 初始化数据
	 */
	public void inintData(){
		
	}
}
