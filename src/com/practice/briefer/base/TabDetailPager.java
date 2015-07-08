package com.practice.briefer.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.practice.briefer.domain.TabData.TopNewsData;
import com.practice.briefer.global.GlobalContants;

/**
 * 页签详情页-屏幕中间的那块内容
 * 
 * @author ZST
 *
 */
public class TabDetailPager extends BaseMenuDetailPager {

	NewsTabData myTabData;

	// private TextView tvText;

	private String mUrl;

	private TabData myTabDetailData;

	@ViewInject(R.id.vp_news)
	private ViewPager myViewPager;

	// 构造方法把Activity，和需要的标签数据传递过来，在别的地方new这个对象的时候
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		myTabData = newsTabData;

		mUrl = GlobalContants.SERVER_URL + myTabData.url;
	}

	@Override
	public View initViews() {
		View view = View.inflate(myActivity, R.layout.tab_detail_pager, null);

		ViewUtils.inject(this, view);

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

		myViewPager.setAdapter(new TopNewsAdapter());// 给里面的ViewPager设置一个适配器
	}

	// 头条新闻的adapter
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils utils;

		public TopNewsAdapter() {
			utils = new BitmapUtils(myActivity);// 这个是xUtils中的方法，处理网络图片
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);//设置默认加载图片，防止由于网络原因在成那个地方是白板
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
			TopNewsData topNewsData = myTabDetailData.data.topnews
					.get(position);
			utils.display(image, topNewsData.topimage);// 传递imageView对象和图片的地址，xUtils的方法

			container.addView(image);
			return image;// 返回给ViewPager的各个item中
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
