package com.practice.briefer.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.practice.briefer.base.BasePager;

/**
 * 新闻实现
 * 父类BasePager用子类HomePager对自己的页面进行控制
 * 
 * @author ZST
 *
 */
public class NewsCenterPager extends BasePager {

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {

		// 拿到标题对象和下面的FrameLayout对象，然后进行复制
		tvTitle.setText("briefer新闻");

		TextView text = new TextView(myActivity);
		text.setText("briefer新闻body");
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.RED);

		// 向framelayout中动态添加布局
		flContent.addView(text);
	}
}
