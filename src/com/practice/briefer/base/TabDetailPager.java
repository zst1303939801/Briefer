package com.practice.briefer.base;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.practice.briefer.NewsDetailActivity;
import com.practice.briefer.R;
import com.practice.briefer.domain.NewsData.NewsTabData;
import com.practice.briefer.domain.TabData;
import com.practice.briefer.domain.TabData.TabNewsData;
import com.practice.briefer.domain.TabData.TopNewsData;
import com.practice.briefer.global.GlobalContants;
import com.practice.briefer.utils.CacheUtils;
import com.practice.briefer.utils.PrefUtils;
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

	private String mMoreUrl;// 加载更多新闻列表

	private Handler mHandler;

	// 构造方法把Activity，和需要的标签数据传递过来，在别的地方new这个对象的时候
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		myTabData = newsTabData;

		mUrl = GlobalContants.SERVER_URL + myTabData.url;
	}

	@Override
	public View initViews() {
		View view = View.inflate(myActivity, R.layout.tab_detail_pager, null);

		// 加载头布局，为了让上面的ViewPager和Listview都能向上滑动
		View headerView = View.inflate(myActivity,
				R.layout.list_header_topnews, null);

		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);

		// 将头条新闻以头布局的形式添加给listView
		lvList.addHeaderView(headerView);

		// lvList是RefreshListView.java对象，setOnRefreshListener是里面的方法-设置下拉刷新监听
		// 运行本页面是不会调用下面的方法的
		lvList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(myActivity, "最后一页了...", Toast.LENGTH_SHORT)
							.show();// 弹出框提示：最后一页了...，LENGTH_SHORT：提示的时间2s
					lvList.onRefreshComplete(false);// 收起加载更多布局
				}
			}
		});

		// 设置listView里面的list被点击时间
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 由于加了两个headview所以点击第一个新闻会出现2，不是0，所以要把率List这个RefreshView对象中的OnItemClickListener方法进行包装
				// 包装的OnItemClickListener方法在率List的RefreshView.java中实现
				System.out.println("被点击:" + position);

				// 35311,34221,34234 - 最终结果：key:read_ids value:35311,34221,34234
				String ids = PrefUtils.getString(myActivity, "read_ids", "");
				String readId = myNewsList.get(position).id;
				if (!ids.contains(readId)) {
					ids = ids + readId + ",";
					PrefUtils.setString(myActivity, "read_ids", ids);
				}

				// myNewsAdapter.notifyDataSetChanged();//
				// 刷新adapter，刷新adapter后adapter相关的方法都会运行一下
				// 方法放到这里会让所有的item都刷新运行，所以自定义方法实现单个item改变颜色（不运行所有的adapter的方法）
				changeReadState(view);// 局部view刷新，view是被点击的item对象

				// 一个Activity跳转到另一个Activity
				// start一个Activity，点击跳转进入详情页
				Intent intent = new Intent();
				intent.setClass(myActivity, NewsDetailActivity.class);
				intent.putExtra("url", myNewsList.get(position).url);// 一个Activity传递参数到另一个Activity
				myActivity.startActivity(intent);
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

	// 改变已读新闻颜色
	// public void onItemClick 中的view就是被点击的那个view，能拿到这个view就能拿到对应的id
	private void changeReadState(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	@Override
	public void inintData() {
		// tvText.setText(myTabData.title);
		String cache = CacheUtils.getCache(mUrl, myActivity);

		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}

		getDataFromServer();// 得到内容数据
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println("详情内容页的结果数据：" + result);

				parseData(result, false);

				lvList.onRefreshComplete(true);// 等接口数据调用成功后隐藏下拉刷新

				// 设置缓存
				CacheUtils.setCache(mUrl, result, myActivity);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(myActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();

				lvList.onRefreshComplete(false);// 等接口数据调用成功后隐藏下拉刷新
			}
		});
	}

	/**
	 * 加载下一页数据
	 */
	private void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println("下一页详情内容页的结果数据：" + result);

				parseData(result, true);

				lvList.onRefreshComplete(true);// 等接口数据调用成功后隐藏下拉刷新

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(myActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();

				lvList.onRefreshComplete(false);// 等接口数据调用成功后隐藏下拉刷新
			}
		});
	}

	// 使用Gson解析数据
	protected void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		myTabDetailData = gson.fromJson(result, TabData.class);
		System.out.println("页签详情页内容：" + myTabDetailData);

		// 处理下一页链接
		String more = myTabDetailData.data.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = GlobalContants.SERVER_URL + more;
		} else {
			mMoreUrl = null;// 没有更多页面
		}

		if (!isMore) {// 如果不是下一页，正常加载数据
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

			// 当mHandler不为空的时候，初始化（new）和实现其方法
			if (mHandler == null) {
				mHandler = new Handler() {
					@Override
					public void handleMessage(android.os.Message msg) {
						// System.out.println("handler ...... 3s");

						int currentItem = myViewPager.getCurrentItem();

						if (currentItem < myTopNewsList.size() - 1) {
							currentItem++;
						} else {
							currentItem = 0;
						}

						myViewPager.setCurrentItem(currentItem);// 切换到下一页
						mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒发送一个消息，新城循环
					}
				};

				mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒钟发送一个消息，随便发送的一个空消息，发送一个消息后会延时3秒钟走到上卖弄的handleMessage
			}
		} else {// 如果是下一页，把数据追加给原来的集合
			ArrayList<TabNewsData> news = myTabDetailData.data.news;
			myNewsList.addAll(news);// 把新的数据追加到之前的新闻页面后面
			myNewsAdapter.notifyDataSetChanged();// 刷新adapter把新的和老的数据都跟新显示

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

			image.setOnTouchListener(new TopNewsTouchListener());// 自己定义Touch的方法，设置触摸监听

			return image;// 返回给ViewPager的各个item中
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	/**
	 * 头条新闻的触摸监听
	 * 
	 * @author ZST
	 *
	 */
	class TopNewsTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 删除Handler中的所有消息以及线程和回调
				mHandler.removeCallbacksAndMessages(null);// 定时器的消息机制（自己定义的一个handler来实现定时器的），如果参数为null则会把所有的都清除
				/**
				 * //Handler的其它方法 mHandler.post(new Runnable() {
				 * 
				 * @Override public void run() { // 这个也是主线程，但是不是同步的，是异步的，不同时进行
				 * 
				 *           } }); mHandler.postDelayed(new Runnable() {
				 * @Override public void run() { // 延迟的执行一些操作
				 * 
				 *           } }, 3000);
				 */
				break;
			case MotionEvent.ACTION_CANCEL:// 这个是事件取消，例如按下以后向下拖动，而不是抬起来手指
				mHandler.sendEmptyMessageAtTime(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				mHandler.sendEmptyMessageAtTime(0, 3000);// 当手指抬起来之后，继续Handler继续发送消息
				break;
			}
			return true;// 默认是false，自己处理就返回true
			// return false;
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

			// 文章已读就改成灰色
			String ids = PrefUtils.getString(myActivity, "read_ids", "");
			if (ids.contains(getItem(position).id)) {
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}

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
