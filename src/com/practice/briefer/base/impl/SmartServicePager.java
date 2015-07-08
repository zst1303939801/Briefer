package com.practice.briefer.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.practice.briefer.base.BasePager;

/**
 * 生活实现
 * 父类BasePager用子类HomePager对自己的页面进行控制
 * 
 * @author ZST
 *
 */
public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {

		// 拿到标题对象和下面的FrameLayout对象，然后进行复制
		tvTitle.setText("briefer生活");
		setSlidingMenuEnable(true);//开启侧边栏

		TextView text = new TextView(myActivity);
		text.setText("briefer生活body");
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.RED);

		// 向framelayout中动态添加布局
		flContent.addView(text);
	}
}
