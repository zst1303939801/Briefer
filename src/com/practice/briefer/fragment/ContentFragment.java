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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页的Fragment,就是内容页(有两个Fragment，一个是内容的，一个是左边滑动菜单)
 * 
 * 首先走父类的声明周期，通过父类来运行相关子类的方法
 * 
 * 子Fragment使用的是基类的Fragment周期的方法，initData这个里面不规定什么时候调用，父类声明周期中是当界面画完之后进行调用
 * 
 * @author ZST
 *
 */
public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.re_group)
	// 引入xUtils框架后使用的方法 - 运用注解的方法
	private RadioGroup rgGroup;

	@ViewInject(R.id.vp_content)
	private ViewPager myViewPager;

	private ArrayList<BasePager> myPagerList;

	//首先走BaseFragment生命周期中的方法，走到onCreateView后调用父类的initViews，然后父类看子类那个实现了initViews，然后调用子类的initViews，就是下面的initViews
	@Override
	public View initViews() {

		View view = View.inflate(myActivity, R.layout.fragment_content, null);// 如果直接制定了布局文件.xml则可以直接用setContentView(R.layout.activity_main);，这里是通过fragment来实现的，所以使用inflate

		// rgGroup = (RadioGroup) view.findViewById(R.id.re_group);//正常取view元素的方法
		ViewUtils.inject(this, view); // 注入view和事件，引入xUtils后使用的方法

		return view;
	}

	/**
	 * 初始化页面数据
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
		// 用for循环的时五个相同的页面，现在五个子页面已经各自实现了，所以要再list中放五个子页面
		myPagerList.add(new HomePager(myActivity));
		myPagerList.add(new NewsCenterPager(myActivity));
		myPagerList.add(new SmartServicePager(myActivity));
		myPagerList.add(new GovAffairsPager(myActivity));
		myPagerList.add(new SettingPager(myActivity));

		myViewPager.setAdapter(new ContentAdapter());// 给ViewPager一个适配器

		// 监听RadioGroup的选择监听事件，主页面下面的五个分类的
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					// myViewPager.setCurrentItem(0);//使用ViewPager适配器自己的方法-设置当前页面被选中
					// myViewPager.setCurrentItem(item,
					// smoothScroll);//item-ViewPager那个页面，smoothScroll平滑
					myViewPager.setCurrentItem(0, false);// 去掉切换页面的滑动
					break;
				case R.id.rb_news:
					myViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_smart:
					myViewPager.setCurrentItem(2, false);
					break;
				case R.id.rb_gov:
					myViewPager.setCurrentItem(3, false);
					break;
				case R.id.rb_setting:
					myViewPager.setCurrentItem(4, false);
					break;
				default:
					break;
				}
			}
		});

		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				myPagerList.get(position).initData();// 如果选中那个页面就初始化当前页面的数据
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		myPagerList.get(0).initData();// 上面的方法只有点击后才能初始化，开始没有点击任何页面，所以首页没有数据，所以现在手动初始化首页
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
			// pager.initData();//
			// 初始化数据(ViewPager默认初始化前两页)...不能放在这里，否则ViewPager会预加载两页，在成两页中继承父类的不同功能会依照第二个页面为主,如果第二页不用加载浪费流量
			return pager.myRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	/**
	 * 获取新闻中心页面
	 * 
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager() {
		return (NewsCenterPager) myPagerList.get(1);

	}

}
