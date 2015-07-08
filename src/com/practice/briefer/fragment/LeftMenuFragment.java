package com.practice.briefer.fragment;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.practice.briefer.MainActivity;
import com.practice.briefer.R;
import com.practice.briefer.base.impl.NewsCenterPager;
import com.practice.briefer.domain.NewsData;
import com.practice.briefer.domain.NewsData.NewsMenuData;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 侧边栏
 * 
 * @author ZST
 *
 */
public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_list)
	private ListView lvList;// 使用的时xUtils Library包

	ArrayList<NewsMenuData> myMenuList;// 得到侧边栏列表
	
	private int mGurrentPos;

	private MenuAdapter myAdapter;

	// 初始化界面
	@Override
	public View initViews() {

		// inflate就相当于将一个xml中定义的布局找出来.
		// 因为在一个Activity里如果直接用findViewById()的话,
		// 对应的是setConentView()的那个layout里的组件.
		// 因此如果你的Activity里如果用到别的layout,比如对话框上的layout,你还要设置
		// 对话框上的layout里的组件(像图片ImageView,文字TextView)上的内容,你就必须用inflate()先将对话框上的layout找出来,
		// 然后再用这个layout对象去找到它上面的组件
		View view = View.inflate(myActivity, R.layout.fragment_left_menu, null);// 使用父类的myActivity
		ViewUtils.inject(this, view);// //使用的时xUtils Library包来初始化这个view页面

		return view;
	}

	// 初始化数据,想初始化数据首先需要一个adapter
	@Override
	public void initData() {
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mGurrentPos = position;//用全局变量记录点击的是哪个listview
				myAdapter.notifyDataSetChanged();//刷新这个adapter会调用里面的getView方法
				
				setCurrentMenuDetailPager(position);
				
				toggleSlidingMenu();//切换SlidingMenu隐藏开关
			}

		});
	}
	
	/**
	 * 切换SlidingMenu状态
	 * @param b
	 */
	protected void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) myActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//切换显示状态，如果是显示状态点击会隐藏，如果是隐藏状态点击会显示
	}
	
	/**
	 * 设置当前菜单详情页
	 * 侧边栏想要和NewsCenterPager等交际，把位置等信息传过去，让新闻等中间那个板块产生变化
	 * @param position
	 */
	protected void setCurrentMenuDetailPager(int position) {
		MainActivity mainUi = (MainActivity) myActivity;
		ContentFragment contentFragment = mainUi.getContentFragment();//获取主页面Fragment
		NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();//获取新闻中心页面
		newsCenterPager.setCurrentMenuDetailPager(position);//设置新闻中心当前页面，传递位置
	}

	// 设置网络数据
	// 因为数据都是从fragment_content.xml传递过来的，例如：新闻中心中渠道的分类，要放在左侧边栏中进行输出，所以要传递数据，
	public void setMenuData(NewsData data) {
		System.out.println("侧边栏数据拿到了：" + data);
		myMenuList = data.data;
		myAdapter = new MenuAdapter();
		lvList.setAdapter(myAdapter);// lvlist是Fragment_left_menu中的一个ListView，通过适配器给这个ListView填充TextView，TextView是在适配器中得到：样式在list_menu_item,数据在适配器中得到
	}

	/**
	 * 侧边栏数据适配器，侧边栏是一个ListView，给这个ListView填充TextView，TextView的样式在list_menu_item.
	 * xml（只相当于一个标题+前面的logo）,在这个Fragment中塞一个一个的TextView
	 * 
	 * 一个Fragment需要得到很多view(pager)进行填充，为了返回一个带有数据的view
	 * 
	 * @author ZST
	 *
	 */
	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return myMenuList.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return myMenuList.get(position);// 拿到点击list中的哪一个
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		/**
		 * 给本Fragment填充pager(页面)和为页面填充数据
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// 把新建的list_menu_item.xml塞到fragment_lefet_menu.xml中
			View view = View.inflate(myActivity, R.layout.list_menu_item, null);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
			NewsMenuData newsMenuData = getItem(position);

			tvTitle.setText(newsMenuData.title);
			
			if(mGurrentPos == position) {//判断当前的绘制的view是否被选中
				//显示红色
				tvTitle.setEnabled(true);//布局文件中引用的selector里面规定了按钮和字体选中和不选中的颜色和背景图片，用代码设置他们的的属性的Enabled来控选择器的改变
			} else {
				//显示白色
				tvTitle.setEnabled(false);
			}

			return view;
		}

	}

}
