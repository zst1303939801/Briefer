package com.practice.briefer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.practice.briefer.fragment.ContentFragment;
import com.practice.briefer.fragment.LeftMenuFragment;

/**
 * 主页面
 * 
 * @author ZST 继承第三方插件Library(SlidingMenu)
 */
public class MainActivity extends SlidingFragmentActivity {

	private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	private static final String FRAGMENT_CONTENT = "fragment_content";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);//让页面没有title
		setContentView(R.layout.activity_main);
		
		setBehindContentView(R.layout.left_menu);//设置侧边栏menu
		SlidingMenu slidingMenu = getSlidingMenu();//获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
		slidingMenu.setBehindOffset(200);//设置预留屏幕的宽度
		
		initFragment();
	}
	
	/**
	 * 初始化fragment，将fragment数据填充给布局文件
	 */
	private void initFragment() {
		
		FragmentManager fm = getSupportFragmentManager();//如果要用一个fragment通常要拿到一个fragment控制文件
		FragmentTransaction transaction = fm.beginTransaction();//开启事务
		
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAGMENT_LEFT_MENU);//前面是一个布局文件，用后面的那个fragment来替换，最后一个是tag标记，方便以后查找使用Fragment LeftMenuFragment = fm.findFragmentByTag(FRAGMENT_CONTENT);
		transaction.replace(R.id.fl_content, new ContentFragment(), FRAGMENT_CONTENT);//用fragment替换fragmentayout
		
		transaction.commit();//提交事务
		//Fragment leftMenuFragment = fm.findFragmentByTag(FRAGMENT_CONTENT);
		
		
	}

}
