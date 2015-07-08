package com.practice.briefer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment基类
 * 
 * @author ZST fragment有自己的生命周期，实现其生命周期的各种方法
 *         这个是基类的fragment，是基础的fragment，控制侧边栏和主页等的子类的fragment
 *
 */
public abstract class BaseFragment extends Fragment {

	public Activity myActivity;

	// fragment创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myActivity = getActivity();//目前只有三个activity(GuideActivity(运行完会消失),MainActivity(进入首页会一直运行),SplashActivity(运行完会消失)),所以是MainActivity给myActivity赋值
	}

	// 处理fragment的布局，这个是基类的布局，让子类的布局实现view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initViews();
	}

	// 依附于activity的创建完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();// 界面创建完成了，view已经画完了，则可以初始化一下页面数据
	}

	// 必须让孩子实现，可以搞成抽象的类
	public abstract View initViews();

	// 初始化数据。因为子类加载页面会有很多的数据，如果是静态的，可以不实现
	public void initData() {

	}

}
