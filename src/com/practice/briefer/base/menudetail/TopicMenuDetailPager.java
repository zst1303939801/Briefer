package com.practice.briefer.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.practice.briefer.base.BaseMenuDetailPager;

/**
 * 菜单详情页-专题
 * 
 * @author ZST
 *
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {

	public TopicMenuDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		
		TextView text = new TextView(myActivity);
		text.setText("菜单详情页-专题");
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.RED);
		
		return text;
	}

}
