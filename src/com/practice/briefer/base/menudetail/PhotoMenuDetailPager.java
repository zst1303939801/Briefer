package com.practice.briefer.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.practice.briefer.base.BaseMenuDetailPager;

/**
 * 菜单详情页-组图
 * 
 * @author ZST
 *
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	public PhotoMenuDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		
		TextView text = new TextView(myActivity);
		text.setText("菜单详情页-组图");
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.RED);
		
		return text;
	}

}
