package com.practice.briefer.fragment;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.practice.briefer.R;
import com.practice.briefer.base.BasePager;
import com.practice.briefer.base.impl.GovAffairsPager;
import com.practice.briefer.base.impl.HomePager;
import com.practice.briefer.base.impl.NewsCenterPager;
import com.practice.briefer.base.impl.SettingPager;
import com.practice.briefer.base.impl.SmartServicePager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * 主页的Fragment
 * 
 * @author ZST
 *
 */
public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.re_group)
	// 引入xUtils框架后使用的方法
	private RadioGroup rgGroup;

	@ViewInject(R.id.vp_content)
	private ViewPager myViewPager;

	private ArrayList<BasePager> myPagerList;

	@Override
	public View initViews() {

		View view = View.inflate(myActivity, R.layout.fragment_content, null);// 如果直接制定了布局文件.xml则可以直接用setContentView(R.layout.activity_main);，这里是通过fragment来实现的，所以使用inflate

		// rgGroup = (RadioGroup)
		// view.findViewById(R.id.re_group);//正常取view元素的方法
		ViewUtils.inject(this, view); // 注入view和事件，引入xUtils后使用的方法

		return view;
	}

	/**
	 * 初始化页面
	 */
	@Override
	public void initData() {
		rgGroup.check(R.id.rb_home);// 默认勾选首页，初始化时候让首页默认勾选

		// 初始化五个子页面
		myPagerList = new ArrayList<BasePager>();
		// for (int i = 0; i < 5; i++) {
		// BasePager pager = new BasePager(myActivity);
		// myPagerList.add(pager);
		// }
		//用for循环的时五个相同的页面，现在五个子页面已经各自实现了，所以要再list中放五个子页面
		myPagerList.add(new HomePager(myActivity));
		myPagerList.add(new NewsCenterPager(myActivity));
		myPagerList.add(new SmartServicePager(myActivity));
		myPagerList.add(new GovAffairsPager(myActivity));
		myPagerList.add(new SettingPager(myActivity));

		myViewPager.setAdapter(new ContentAdapter());// 一个适配器
	}

	/**
	 * 初始化页面 用一个ViewPager(就是可以左右滑动的多个页面)的适配器来为每个页面来完成适配器的任务，ViewPager适配器里面有很多方法
	 * 
	 * @author ZST
	 *
	 */
	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return myPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		/**
		 * 实例化，ViewPager是一组view对象(container)，没有对象都已一个位置(position)
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// myPagerList中的BasePager不是view对象，但是BasePager中的myRootView是一个view对象
			BasePager pager = myPagerList.get(position);
			container.addView(pager.myRootView);
			pager.initData();//初始化数据...
			return pager.myRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
