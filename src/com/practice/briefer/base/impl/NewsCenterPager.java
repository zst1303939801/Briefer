package com.practice.briefer.base.impl;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.practice.briefer.MainActivity;
import com.practice.briefer.base.BaseMenuDetailPager;
import com.practice.briefer.base.BasePager;
import com.practice.briefer.base.menudetail.InteractMenuDetailPager;
import com.practice.briefer.base.menudetail.NewsMenuDetailPager;
import com.practice.briefer.base.menudetail.PhotoMenuDetailPager;
import com.practice.briefer.base.menudetail.TopicMenuDetailPager;
import com.practice.briefer.domain.NewsData;
import com.practice.briefer.domain.NewsData.NewsMenuData;
import com.practice.briefer.fragment.LeftMenuFragment;
import com.practice.briefer.global.GlobalContants;
import com.practice.briefer.utils.CacheUtils;

/**
 * 新闻实现 父类BasePager用子类HomePager对自己的页面进行控制
 * 
 * @author ZST
 *
 */
public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mPagers;// 新闻中四个详情页的集合

	private NewsData myNewsData;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {

		// // 拿到标题对象和下面的FrameLayout对象，然后进行复制
		tvTitle.setText("briefer新闻");
		setSlidingMenuEnable(true);// 开启侧边栏

		// 下面是静态的数据，第一次写的测试数据
		// TextView text = new TextView(myActivity);
		// text.setText("briefer新闻body");
		// text.setTextSize(25);
		// text.setGravity(Gravity.CENTER);
		// text.setTextColor(Color.RED);
		//
		// // 向framelayout中动态添加布局
		// flContent.addView(text);
		String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL, myActivity);
		
		if(!TextUtils.isEmpty(cache)){//如果缓存存在直接解析数据，无需调用访问网络
			parseData(cache);
		}
		
		getDataFromServer();//不管用没有缓存，都获取最新的数据，保证数据最新

	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();// 这个使用的时自己导入的Library-xUtils

		// HttpMethod请求方法:使用get请求，callBack:回调接口
		// 回调的泛型是String：json数据是String
		// utils.send(method, url, callBack);
		// 使用xUtils发送请求
		utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
				new RequestCallBack<String>() {

					// 访问成功，会有一个相应信息：responseInfo
					// 在主线程
					@Override
					public void onSuccess(ResponseInfo responseInfo) {
						String result = (String) responseInfo.result;// 知道是String所以强转成String
						System.out.println("返回结果：" + result);// 这个返回比较复杂，通过get得到会计较麻烦

						parseData(result);
						
						//设置缓存
						CacheUtils.setCache(GlobalContants.CATEGORIES_URL, result, myActivity);
					}

					// 访问失败，在主线程
					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(myActivity, msg, Toast.LENGTH_SHORT)
								.show();// Android中的Toast是一种简易的消息提示框
						error.printStackTrace();
					}
				});
	}

	/**
	 * 解析json网络数据
	 * 
	 * @param result
	 */
	protected void parseData(String result) {
		Gson gson = new Gson();

		// gson.fromJson(json,
		// classOfT);//json从json得到数据，classOfT把传过来的数据当封装成对象，进行处理
		// Date date = gson.fromJson(result,Date.class);//如果封装成对象，会吧
		// result里面的date取出来放进去.前面的Date date是自己创建的
		// Gson能解析json中的数据，必须使用一个类javabean对象来接收这个对象，bean在新建的文件夹domain下面
		myNewsData = gson.fromJson(result, NewsData.class);
		System.out.println("解析结果：" + myNewsData);// 打印结果依照NewsData的toString的自定义结果为主

		// 刷新侧边栏数据
		MainActivity mainUi = (MainActivity) myActivity;
		LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
		leftMenuFragment.setMenuData(myNewsData);

		// 准备四个菜单详情页
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(myActivity,
				myNewsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(myActivity));
		mPagers.add(new PhotoMenuDetailPager(myActivity,btnPhoto));
		mPagers.add(new InteractMenuDetailPager(myActivity));

		setCurrentMenuDetailPager(0);// 设置默认当前详情页为第一个
	}

	/**
	 * 设置当前菜单的详情页
	 */
	public void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailPager pager = mPagers.get(position);// 拿到对应的子类页面的类
		flContent.removeAllViews();// 清楚之前所有的布局，是FrameLayout的方法
		flContent.addView(pager.mRootView);// 把子类的pager放到这个Framelayout中，将布局文件设置给Framelayout

		// 设置新闻中心的标题
		NewsMenuData newsMenuData = myNewsData.data.get(position);
		tvTitle.setText(newsMenuData.title);

		pager.inintData();// 初始化对应子类的当前页面的数据
		
		if(pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);
		}else {
			btnPhoto.setVisibility(View.GONE);
		}

	}
}
