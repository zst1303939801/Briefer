package com.practice.briefer.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.practice.briefer.base.BasePager;

/**
 * 设置实现
 * 父类BasePager用子类HomePager对自己的页面进行控制
 * 
 * @author ZST
 *
 */
public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {

		// 拿到标题对象和下面的FrameLayout对象，然后进行复制
		tvTitle.setText("briefer设置");
		btnMenu.setVisibility(View.GONE);//隐藏菜单按钮
		setSlidingMenuEnable(false);//关闭侧边栏

		TextView text = new TextView(myActivity);
		text.setText("briefer设置body");
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.RED);

		// 向framelayout中动态添加布局
		flContent.addView(text);
	}
}
