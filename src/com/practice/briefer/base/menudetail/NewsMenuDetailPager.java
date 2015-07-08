package com.practice.briefer.base.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.practice.briefer.R;
import com.practice.briefer.base.BaseMenuDetailPager;
import com.practice.briefer.base.TabDetailPager;
import com.practice.briefer.domain.NewsData.NewsTabData;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 菜单详情页-新闻
 * 
 * @author ZST
 *
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

	private ViewPager myViewPager;

	private ArrayList<TabDetailPager> myPagerList;

	private ArrayList<NewsTabData> myNewsTabData;// 这个是NewsCenterPager.java传递过来的数据，在new这里面的构造函数时候就传递过来的-页签数据

	private TabPageIndicator myIndicator;

	public NewsMenuDetailPager(Activity activity,
			ArrayList<NewsTabData> children) {
		super(activity);

		myNewsTabData = children;
	}

	@Override
	public View initViews() {

		View view = View.inflate(myActivity, R.layout.news_menu_detail, null);
		myViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);

		ViewUtils.inject(this, view);//想使用xUtils必须注册当前对象，才能使用xUtils的标签
		
		myIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);// 模仿ViewPagerIndivator库中的方法,关联上面的TabPage和下面的ViewPager

		return view;
	}

	// 这个初始化数据的饿方法在什么时候调用呢，在本对象初始化(构造函数被调用的时候)时候之后完成一些操作后进行调用
	@Override
	public void inintData() {
		myPagerList = new ArrayList<TabDetailPager>();

		// 初始化页签数据
		for (int i = 0; i < myNewsTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(myActivity,
					myNewsTabData.get(i));
			myPagerList.add(pager);
		}

		// 下面逻辑处理好了，然后给这个ViewPager设一个Adapter
		myViewPager.setAdapter(new MenuDetailAdapter());

		myIndicator.setViewPager(myViewPager);// 模仿ViewPageIndivator库中的方法,只能是ViewPager在设定后适配器后才能使用这个方法，将viewpager和Indicator关联起来
	}

	// 跳转到下一个页面，使用xUtils的库
	@OnClick(R.id.btn_next)
	public void nextPage(View view) {
		int currentItem = myViewPager.getCurrentItem();// 得到当前位置
		myViewPager.setCurrentItem(++currentItem);
	}

	/**
	 * viewpager需要一个适配器adapter
	 */
	class MenuDetailAdapter extends PagerAdapter {

		// 模仿ViewPagerIndivator库中的方法，只要继承PagerAdapter都会有这份方法，重写此方法，用于viewpagerIndicator的页签显示
		@Override
		public CharSequence getPageTitle(int position) {
			return myNewsTabData.get(position).title;
		}

		@Override
		public int getCount() {
			return myPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// @Override
		// public Object instantiateItem(View container, int position) {
		// TabDetailPager pager = myPagerList.get(position);//ViewPager中的一个页面
		//
		// return super.instantiateItem(container, position);
		// }

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = myPagerList.get(position);// ViewPager中的一个页面
			container.addView(pager.mRootView);// 把单个pager付给Group pager
			pager.inintData();// 给pager初始化数据

			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
