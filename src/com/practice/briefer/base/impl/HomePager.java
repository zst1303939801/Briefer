package com.practice.briefer.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.practice.briefer.MainActivity;
import com.practice.briefer.base.BasePager;

/**
 * 首页实现
 * 父类BasePager用子类HomePager对自己的页面进行控制
 * 
 * @author ZST
 *
 */
public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {

		// 拿到标题对象和下面的FrameLayout对象，然后进行复制
		tvTitle.setText("briefer首页");
		btnMenu.setVisibility(View.GONE);//隐藏菜单按钮
		setSlidingMenuEnable(false);//设置不能全屏滑出左边菜单侧边栏

		TextView text = new TextView(myActivity);
		text.setText("briefer首页body");
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.RED);

		// 向framelayout中动态添加布局
		flContent.addView(text);
	}
	
	//为了设置左边菜单不被左滑出来，首先获得SlidingMenu对象
	//在MainActivity中继承了SlidingMenu，能拿到这个对象，所以拿到MainActivity就能得到这个SlidingMenu对象
	//具体方法见印象笔记：学习 - briefer(智慧北京) - day02 主页5个页面实现 
	//放到基类里面，线面是本页面找到SlidingMenu的步骤
	// private void setSlidingMenuEnable(boolean enable) {
	// MainActivity mainUi = (MainActivity)
	// myActivity;//由于已经查到子类被初始化的时候继承父类的myActivity是MainActivity所以可以强制转换
	//
	// SlidingMenu slidingMenu = mainUi.getSlidingMenu();
	//
	// if (enable) {
	// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//可以在全屏滑动显示左边菜单
	// }else {
	// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//不可以在全屏滑动显示左边菜单
	// }
	// }
	
	
}
