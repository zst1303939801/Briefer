package com.practice.briefer.fragment;

import com.practice.briefer.R;

import android.view.View;

/**
 * 侧边栏
 * 
 * @author ZST
 *
 */
public class LeftMenuFragment extends BaseFragment {

	@Override
	public View initViews() {
		
		//inflate就相当于将一个xml中定义的布局找出来.
		//因为在一个Activity里如果直接用findViewById()的话,
		//对应的是setConentView()的那个layout里的组件.
		//因此如果你的Activity里如果用到别的layout,比如对话框上的layout,你还要设置
		//对话框上的layout里的组件(像图片ImageView,文字TextView)上的内容,你就必须用inflate()先将对话框上的layout找出来,
		//然后再用这个layout对象去找到它上面的组件
		View view = View.inflate(myActivity, R.layout.fragment_left_menu, null);//使用父类的myActivity
		return view;
	}

}
