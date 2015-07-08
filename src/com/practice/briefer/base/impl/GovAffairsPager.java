package com.practice.briefer.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.practice.briefer.base.BasePager;

/**
 * 政务实现
 * 父类BasePager用子类HomePager对自己的页面进行控制
 * 
 * @author ZST
 *
 */
public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {

		// 拿到标题对象和下面的FrameLayout对象，然后进行复制
		tvTitle.setText("briefer政务");
		setSlidingMenuEnable(true);//开启侧边栏

		TextView text = new TextView(myActivity);
		text.setText("briefer政务body");
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.RED);

		// 向framelayout中动态添加布局
		flContent.addView(text);
	}
}
