package com.practice.briefer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

public class GuideActivity extends Activity {
	
	private static final int imgIds[] = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3,R.drawable.guide_4};//取出引导页的图片的id
	private ArrayList<ImageView> imageViewList;
	private ViewPager vpGuide;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//设置引导页没有标题，必须在setContentView之前调用
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);//获取viewpager
		
		initViews();//初始化界面
		vpGuide.setAdapter(new GuideAdapter());//使用适配器adapter给vpGuide页面填充数据
	}
	
	/**
	 * 初始化界面(准备数据)
	 */
	private void initViews() {
		imageViewList = new ArrayList<ImageView>();
		
		//初始化引导页的三个页面
		for(int i=0; i<imgIds.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(imgIds[i]);//新建一个image对象把取出的每张图片放到这个对象中
			imageViewList.add(image);
		}
	}
	
	/**
	 * ViewPager数据适配器
	 * @author ZST
	 *
	 */
	class GuideAdapter extends PagerAdapter {

		/**
		 * 填充的数据是三个
		 */
		@Override
		public int getCount() {
			return imgIds.length;
		}

		//arg0==arg1才会显示
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		//初始化页面，放到容器里面-container:容器
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViewList.get(position));
			return imageViewList.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);//从ViewGroup 容器中(container：容器)重删除这个对象
		}
	}
}
