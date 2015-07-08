package com.practice.briefer.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.practice.briefer.MainActivity;
import com.practice.briefer.R;

/**
 * 主页下5个子页面的基类,里面的方法都是自定义的方法 由于5个子页面都有相同的对象，所以自定义一个BasePager对象，构造函数构造出每个对象(页面)
 * 定义各种方法让子类使用
 * 
 * @author ZST
 *
 */
public class BasePager {

	// Activity本身就是一个context对象，可以先穿一个Activity进来，方便以后获取资源文件，调用api。
	public Activity myActivity;
	public View myRootView;// 布局对象

	public TextView tvTitle;// 标题对象，获得后给子类使用
	public FrameLayout flContent;// 框架对象，标题线面的框架对象，由于内容不能确定，所以用框架

	public ImageButton btnMenu;// 页面的左边菜单的按钮对象

	// 构造函数
	public BasePager(Activity activity) {
		myActivity = activity;
		initViews();
	}

	/**
	 * 初始化布局
	 */
	public void initViews() {
		myRootView = View.inflate(myActivity, R.layout.base_pager, null);

		// 由于是使用inflate获得的布局文件所以不能直接使用findViewByid，必须使用myRootView.findViewById
		tvTitle = (TextView) myRootView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) myRootView.findViewById(R.id.fl_content);
		btnMenu = (ImageButton) myRootView.findViewById(R.id.btn_menu);
		
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleSlidingMenu();
			}
		});

	}
	
	/**
	 * 切换SlidingMenu状态
	 * @param b
	 */
	protected void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) myActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//切换显示状态，如果是显示状态点击会隐藏，如果是隐藏状态点击会显示
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}

	/**
	 * 设置侧边栏(SlidingMenu)的开关
	 * @param enable
	 */
	public void setSlidingMenuEnable(boolean enable) {
		MainActivity mainUi = (MainActivity) myActivity;// 由于已经查到子类被初始化的时候继承父类的myActivity是MainActivity所以可以强制转换

		SlidingMenu slidingMenu = mainUi.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 可以在全屏滑动显示左边菜单
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 不可以在全屏滑动显示左边菜单
		}
	}

}
