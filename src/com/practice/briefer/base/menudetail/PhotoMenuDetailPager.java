package com.practice.briefer.base.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.practice.briefer.R;
import com.practice.briefer.base.BaseMenuDetailPager;
import com.practice.briefer.domain.PhotoData;
import com.practice.briefer.domain.PhotoData.PhotoInfo;
import com.practice.briefer.global.GlobalContants;
import com.practice.briefer.utils.CacheUtils;

/**
 * 菜单详情页-组图
 * 
 * @author ZST
 *
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	private ListView lvPhoto;
	private GridView gvPhoto;
	private ArrayList<PhotoInfo> mPhotoList;
	private PhotoAdapter mAdapter;

	public PhotoMenuDetailPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		// //没有自定义布局文件则返回自己定义的一个临时text，界面显示文字
		// TextView text = new TextView(myActivity);
		// text.setText("菜单详情页-组图");
		// text.setTextSize(25);
		// text.setGravity(Gravity.CENTER);
		// text.setTextColor(Color.RED);
		//
		// return text;
		View view = View.inflate(myActivity, R.layout.menu_photo_pager, null);
		
		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
		
		return view;

	}
	
	@Override
	public void inintData() {
		
		String cache = CacheUtils.getCache(GlobalContants.PHOTOS_URL, myActivity);
		
		if(!TextUtils.isEmpty(cache)){
			
		}
		
		getDataFromServer();
	}
	
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();//使用xUtils库来获取json数据
		utils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL, new RequestCallBack<String>() {

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
	
	private void parseData(String result) {
		Gson gson = new Gson();
		PhotoData data = gson.fromJson(result, PhotoData.class);
		
		mPhotoList = data.data.news;//获取组图列表集合
		
		mAdapter = new PhotoAdapter();//new一个Adapter对象
		lvPhoto.setAdapter(mAdapter);
		gvPhoto.setAdapter(mAdapter);
		
		
	}

	class PhotoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
		
	}

}
