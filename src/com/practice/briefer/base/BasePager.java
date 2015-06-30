package com.practice.briefer.base;

import com.practice.briefer.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 主页下5个子页面的基类,里面的方法都是自定义的方法
 * 由于5个子页面都有相同的对象，所以自定义一个BasePager对象，构造函数构造出每个对象(页面)
 * 定义各种方法让子类使用
 * 
 * @author ZST
 *
 */
public class BasePager {
	
	//Activity本身就是一个context对象，可以先穿一个Activity进来，方便以后获取资源文件，调用api。
	public Activity myActivity;
	public View myRootView;//布局对象
	
	public TextView tvTitle;//标题对象，获得后给子类使用
	public FrameLayout flContent;//框架对象，标题线面的框架对象，由于内容不能确定，所以用框架
	
	//构造函数
	public BasePager(Activity activity) {
		myActivity = activity;
		initViews();
	}
	
	/**
	 * 初始化布局
	 */
	public void initViews() {
		myRootView = View.inflate(myActivity, R.layout.base_pager, null);
		
		//由于是使用inflate获得的布局文件所以不能直接使用findViewByid，必须使用myRootView.findViewById
		tvTitle = (TextView) myRootView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) myRootView.findViewById(R.id.fl_content);
		
		
	}
	
	/**
	 * 初始化数据
	 */
	public void initData() {
		
	}
	

}
