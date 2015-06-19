package com.practice.briefer;

import com.practice.briefer.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;


public class SplashActivity extends Activity {
	
	public static final String tag ="SplashActivity";
	
	RelativeLayout rlRoot;//由于在activity_splash使用的时相对布局RelativeLayout
	
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.activity_splash);
		
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);//获取根布局
		
		startAnim();
	}

	/*
	 * 开始一个动画
	 */
	private void startAnim() {
		
		//动画集合  让下面两个动作都能执行，false是让差不器不共享，自己折腾
		AnimationSet set = new AnimationSet(false);//
		
		//开始一个旋转
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );//从0°旋转360°，0.5f-基于中心点，自己旋转
		rotate.setDuration(1000);//动画时间
		rotate.setFillAfter(true);//设置动画后保持这个
		
		//开始一个缩放
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );//宽度从0到1，自身
		scale.setDuration(1000);
		rotate.setFillAfter(true);
		
		//开始一个渐变动画
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(1000);
		alpha.setFillAfter(true);
		
		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);
		
		//设置动画监听，然后new一个动画监听
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			//监听动画结束
			@Override
			public void onAnimationEnd(Animation animation) {
				//结束后开始一个activity,要new一个新的意图
				startActivity(new Intent(SplashActivity.this, GuideActivity.class));//传一个自身的context和跳转的对象
				finish();//跳出去以后就结束当前的页面
			}
		});
		
		rlRoot.startAnimation(set);//根布局开始一个动画
	}
}
