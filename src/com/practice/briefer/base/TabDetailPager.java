package com.practice.briefer.base;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.practice.briefer.R;
import com.practice.briefer.domain.NewsData.NewsTabData;
import com.practice.briefer.domain.TabData;
import com.practice.briefer.domain.TabData.TabNewsData;
import com.practice.briefer.domain.TabData.TopNewsData;
import com.practice.briefer.global.GlobalContants;
import com.practice.briefer.view.RefreshListView;
import com.practice.briefer.view.RefreshListView.OnRefreshListener;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 页签详情页-屏幕中间的那块内容
 * 
 * 由于内容形式和BaseMenuDetailPager有点像，所以没有建立自己的基类，使用的BaseMenuDetailPager这个基类（基本功能一样，
 * 初始化view，舒适化data）
 * 
 * @author ZST
 *
 */
public class TabDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener {

	NewsTabData myTabData;

	// private TextView tvText;

	private String mUrl;

	private TabData myTabDetailData;

	@ViewInject(R.id.vp_news)
	private ViewPager myViewPager;

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;// 头条新闻标题
	ArrayList<TopNewsData> myTopNewsList;// 头条新闻集合

	@ViewInject(R.id.indicator)
	private CirclePageIndicator myIndicator; // 头条新闻位置指示器

	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;// 新闻列表
	ArrayList<TabNewsData> myNewsList;// 新闻数据集合
	NewsAdapter myNewsAdapter;// 列表适配器

	// 构造方法把Activity，和需要的标签数据传递过来，在别的地方new这个对象的时候
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		myTabData = newsTabData;

		mUrl = GlobalContants.SERVER_URL + myTabData.url;
	}

	@Override
	public View initViews() {
		View view = View.inflate(myActivity, R.layout.tab_detail_pager, null);
		
		//加载头布局，为了让上面的ViewPager和Listview都能向上滑动
		View headerView = View.inflate(myActivity, R.layout.list_header_topnews, null);

		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);
		
		//将头条新闻以头布局的形式添加给listView
		lvList.addHeaderView(headerView);
		
		//lvList是RefreshListView.java对象，setOnRefreshListener是里面的方法-设置下拉刷新监听
		//运行本页面是不会调用下面的方法的
		lvList.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getDataFromServer();				
			}
		});

		return view;

		// 临时测试使用的数据
		// tvText = new TextView(myActivity);
		// tvText.setText("页签详情页");
		// tvText.setTextSize(25);
		// tvText.setGravity(Gravity.CENTER);
		// tvText.setTextColor(Color.RED);
		// return tvText;
	}

	@Override
	public void inintData() {
		// tvText.setText(myTabData.title);
		getDataFromServer();// 得到内容数据
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println("详情内容页的结果数据：" + result);

				parseData(result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}
		});
	}

	// 使用Gson解析数据
	protected void parseData(String result) {
		Gson gson = new Gson();
		myTabDetailData = gson.fromJson(result, TabData.class);
		System.out.println("页签详情页内容：" + myTabDetailData);

		myTopNewsList = myTabDetailData.data.topnews;

		myNewsList = myTabDetailData.data.news;

		if (myTopNewsList != null) {
			myViewPager.setAdapter(new TopNewsAdapter());// 给里面的ViewPager设置一个适配器
			myIndicator.setViewPager(myViewPager);
			myIndicator.setSnap(true);// 一跳一挑效果显示
			myIndicator.setOnPageChangeListener(this);// 把myViewPager的适配器送给myIndicator，引用的框架俩处理
			myIndicator.onPageSelected(0);// 让指示器重新回到第一个，默认会记住切出去的位置，等切回来的时候会调到记住的位置

			tvTitle.setText(myTopNewsList.get(0).title);
		}

		// 填充新闻列表数据-万一接口挂掉了，养成良好的习惯
		if (myNewsList != null) {
			myNewsAdapter = new NewsAdapter();
			lvList.setAdapter(myNewsAdapter);// 给ListView设置适配器
		}

	}

	// 头条新闻的adapter
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils utils;

		// 本类构造函数
		public TopNewsAdapter() {
			utils = new BitmapUtils(myActivity);// 这个是xUtils中的方法，处理网络图片
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);// 设置默认加载图片，防止由于网络原因在成那个地方是白板
		}

		@Override
		public int getCount() {
			return myTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image = new ImageView(myActivity);// 头条新闻都是一张一张的图片
			// image.setImageResource(R.drawable.topnews_item_default);//图片默认背景，可以使用xUtils处理
			image.setScaleType(ScaleType.FIT_XY);// 基于控件大小填充图片

			// 使用xUtils框架，节约了各种问题，包括内存溢出、缓存等等问题
			TopNewsData topNewsData = myTopNewsList.get(position);
			utils.display(image, topNewsData.topimage);// 传递imageView对象和图片的地址，xUtils的方法

			container.addView(image);
			return image;// 返回给ViewPager的各个item中
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	// 给LiseView-lvList设置新闻列表适配器
	class NewsAdapter extends BaseAdapter {

		private BitmapUtils utils;

		public NewsAdapter() {
			utils = new BitmapUtils(myActivity);// 使用xUtils的BitmapUtils来处理图片
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return myNewsList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			return myNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// listView重用
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(myActivity, R.layout.list_news_item,
						null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TabNewsData item = getItem(position);// 使用自己的方法拿到自身的界面item，一个假面对应一个javabean类

			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.pubdate);
			
			utils.display(holder.ivPic, item.listimage);

			return convertView;
		}

	}

	// 这个是内部中的一个listView，没有必要全局，所以建一个内部类
	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivPic;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		TopNewsData topNewsData = myTopNewsList.get(arg0);
		tvTitle.setText(topNewsData.title);
	}

}
