package com.practice.briefer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity {
	
	private static final int imgIds[] = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3,R.drawable.guide_4};//取出引导页的图片的id
	
	private ArrayList<ImageView> imageViewList;
	private ViewPager vpGuide;
	
	private LinearLayout llPointGroup;//引导页圆点父控件

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//设置引导页没有标题，必须在setContentView之前调用
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);//获取viewpager
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
		
		initViews();//初始化界面
		vpGuide.setAdapter(new GuideAdapter());//使用适配器adapter给vpGuide页面填充数据
		
		//vpGuide.setOnPageChangeListener(listener);//给网页设置一个（滑动等操作的）事件监听
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
		
		//初始化引导页的三个小圆点
		for(int i=0; i<imgIds.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);//新建一个view对象把取出的每张背景放到这个对象中
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);//由于view的属性没有margin所以用linearLayout的params
			if(i>0) {
				params.setMargins(10, 0, 0, 0);//设置第2以上的的圆点间隔
			}
			
			point.setLayoutParams(params);//设置圆点大小，使用线性布局的LayoutParams
			llPointGroup.addView(point);//把圆点对象嵌加给线性布局LinearLayout
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
	
	/**
	 * viewPager的滑动事件监听
	 * @author ZST
	 *
	 */
	class GuidePageListener implements OnPageChangeListener {

		//滑动状态发生变化
		@Override
		public void onPageScrollStateChanged(int state) {
			
		}

		//滑动事件，position当前页面位置，positionOffset偏移量，positionOffsetPixels偏移的像素
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			System.out.println("偏移："+"position"+"....."+"positionOffset"+"....."+"positionOffsetPixels"+".....");
		}

		//某个页面被选中
		@Override
		public void onPageSelected(int position) {
			
		}

//		//滑动状态发生变化
//		@Override
//		public void onPageScrollStateChanged(int arg0) {
//			
//		}
//
//		//滑动事件
//		@Override
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//			
//		}
//
//		//某个页面被选中
//		@Override
//		public void onPageSelected(int arg0) {
//			
//		}
		
	}
	
}
