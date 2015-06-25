package com.practice.briefer;

import java.util.ArrayList;

import com.practice.briefer.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {

	private static final int imgIds[] = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4 };// 取出引导页的图片的id

	private ArrayList<ImageView> imageViewList;
	private ViewPager vpGuide;

	private LinearLayout llPointGroup;// 引导页圆点父控件
	private int mPointWidth;// 圆点之间的距离
	private View viewRedPoint;// 声明红色圆点对象

	private Button btnStart;// 获取开始按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置引导页没有标题，必须在setContentView之前调用
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);// 获取viewpager
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);// 获取圆点组
		viewRedPoint = findViewById(R.id.view_red_point);// 获取红色圆点
		btnStart = (Button) findViewById(R.id.btn_start);

		initViews();// 初始化界面
		vpGuide.setAdapter(new GuideAdapter());// 使用适配器adapter给vpGuide页面填充数据

		vpGuide.setOnPageChangeListener(new GuidePageListener());// 给网页设置一个（滑动等操作的）事件监听；在给vpGuide这页绑定listener时候直接new一个GuidePageListener，相关操作在GuidePageListener里面实现

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跟新sp，表示已经展示了引导页
				// SharedPreferences sp = getSharedPreferences("config",
				// MODE_PRIVATE);
				// sp.edit().putBoolean("is_uesr_guide_showed", true);
				// 封装后
				PrefUtils.setBoolean(GuideActivity.this,
						"is_user_guide_showed", true);

				// 跳转主页面
				startActivity(new Intent(GuideActivity.this, MainActivity.class));// 第一个拿到context对象，调到第二个
				finish();// 结束当前页面
			}
		});
	}

	/**
	 * 初始化界面(准备数据)
	 */
	private void initViews() {
		imageViewList = new ArrayList<ImageView>();

		// 初始化引导页的三个页面
		for (int i = 0; i < imgIds.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(imgIds[i]);// 新建一个image对象把取出的每张图片放到这个对象中
			imageViewList.add(image);
		}

		// 初始化引导页的三个小圆点
		for (int i = 0; i < imgIds.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);// 新建一个view对象把取出的每张背景放到这个对象中

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					10, 10);// 由于view的属性没有margin所以用linearLayout的params
			if (i > 0) {
				params.setMargins(10, 0, 0, 0);// 设置第2以上的的圆点间隔
			}

			point.setLayoutParams(params);// 设置圆点大小，使用线性布局的LayoutParams
			llPointGroup.addView(point);// 把圆点对象嵌加给线性布局LinearLayout
		}

		// 界面绘制的几种计算方法(在onCreate结束之后才会进行的计算方法)
		// measure：测量布局大小
		// Layout：位置(具体放在那里)
		// ondraw：怎么画按钮
		// 下面width为0
		// int width = llPointGroup.getChildAt(1).getLeft() -
		// llPointGroup.getChildAt(0).getLeft();
		// System.out.println("width:"+width);

		// 获取树视图,给树视图增加一个全局样式监听，动态的得到两个远点的距离，对layout结束时间进行一个监听，
		// 查看视图树有个sdk自带的工具：sdk-tools-hierarchyviewer工具
		// 监听全局样式运行完毕，正好有一个视图树能实现全局监听，网上查到的方法有一个全局监听的方法
		llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					// 当layout执行完毕后回调此方法
					@Override
					public void onGlobalLayout() {
						System.out.println("layout 结束");// 会执行三次
						llPointGroup.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);// 执行一次就销毁这个监听方法，this是本方法
						// llPointGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						mPointWidth = llPointGroup.getChildAt(1).getLeft()
								- llPointGroup.getChildAt(0).getLeft();
						System.out.println("width:" + mPointWidth);
					}
				});

	}

	/**
	 * ViewPager数据适配器
	 * 
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

		// arg0==arg1才会显示
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 初始化页面，放到容器里面-container:容器
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViewList.get(position));
			return imageViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);// 从ViewGroup
												// 容器中(container：容器)重删除这个对象
		}
	}

	/**
	 * viewPager的滑动事件监听
	 * 
	 * @author ZST
	 *
	 */
	class GuidePageListener implements OnPageChangeListener {

		// 滑动状态发生变化
		@Override
		public void onPageScrollStateChanged(int state) {

		}

		// 滑动事件，position当前页面位置，positionOffset偏移量，positionOffsetPixels偏移的像素
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

			System.out.println("偏移：" + "当前位置：" + position + "....." + "百分比："
					+ positionOffset + "....." + "移动距离：" + positionOffsetPixels
					+ ".....");
			int len = (int) (mPointWidth * positionOffset) + position
					* mPointWidth;// 页面百分比*两点之间的距离=圆点移动的距离，由于返回的是float类型，但是这里不需要精度太高的，所以使用强转成int

			// 由于LayoutParams没有marginLeft，所以使用RelativeLayout.LayoutParams。
			// 此处红色的点可以直接viewRedPoint.getLayoutParams是因为红点的高度和宽度在布局文件中已经制定了，灰色的是在.java手动指定的，所以灰色的点必须设置对象
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint
					.getLayoutParams();
			params.leftMargin = len;// 设置左边距
			viewRedPoint.setLayoutParams(params);// 重新给小红点设置布局参数

		}

		// 某个页面被选中
		@Override
		public void onPageSelected(int position) {

			// 最后一页被选中则显示开始按钮
			if (position == imgIds.length - 1) {
				btnStart.setVisibility(View.VISIBLE);
			} else {
				btnStart.setVisibility(View.INVISIBLE);
			}

		}

		// //滑动状态发生变化
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		//
		// }
		//
		// //滑动事件
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		//
		// }
		//
		// //某个页面被选中
		// @Override
		// public void onPageSelected(int arg0) {
		//
		// }

	}

}
