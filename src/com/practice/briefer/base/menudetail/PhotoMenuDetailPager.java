package com.practice.briefer.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.practice.briefer.R;
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
		// //没有自定义布局文件则返回自己定义的一个临时text，界面显示文字
		// TextView text = new TextView(myActivity);
		// text.setText("菜单详情页-组图");
		// text.setTextSize(25);
		// text.setGravity(Gravity.CENTER);
		// text.setTextColor(Color.RED);
		//
		// return text;
		View view = View.inflate(myActivity, R.layout.menu_photo_pager, null);
		
		return view;

	}

}
