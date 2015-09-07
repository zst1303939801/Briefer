package com.practice.briefer.base.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
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
	private ImageButton btnPhoto;

	public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		
		//由于按钮在新闻页的那个类里面，所以不能拿到这个按钮对象，只有在NewsCenterPager new这个PhotoMenuDetailPager时候把按钮传入进来
		this.btnPhoto = btnPhoto;
		
		btnPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeDisplay();
			}
		});
		
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

		String cache = CacheUtils.getCache(GlobalContants.PHOTOS_URL,
				myActivity);

		if (!TextUtils.isEmpty(cache)) {

		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();// 使用xUtils库来获取json数据
		utils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo responseInfo) {
						String result = (String) responseInfo.result;// 知道是String所以强转成String
						System.out.println("返回结果：" + result);// 这个返回比较复杂，通过get得到会计较麻烦

						parseData(result);

						// 设置缓存
						CacheUtils.setCache(GlobalContants.CATEGORIES_URL,
								result, myActivity);
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

		mPhotoList = data.data.news;// 获取组图列表集合

		if(mPhotoList != null){
			mAdapter = new PhotoAdapter();// new一个Adapter对象
			lvPhoto.setAdapter(mAdapter);
			gvPhoto.setAdapter(mAdapter);
		}

	}

	class PhotoAdapter extends BaseAdapter {

		private BitmapUtils utils;

		// 建立构造函数，加载图片
		public PhotoAdapter() {
			utils = new BitmapUtils(myActivity);
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mPhotoList.size();
		}

		//拿到的是实体类对象的item
		@Override
		public PhotoInfo getItem(int position) {
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(myActivity,
						R.layout.list_photo_item, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				System.out.println("111111..."+holder.tvTitle);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 拿到这个item对象 - 一个一个的item
			PhotoInfo item = getItem(position);

			holder.tvTitle.setText(item.title);//把实体类的对象值赋给布局文件的对象值

			utils.display(holder.ivPic, item.listimage);// 使用utils来加载图片

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivPic;
	}

	private boolean isListDiaplay = true;//是否列表展示
	/**
	 * 切换展现方式
	 */
	private void changeDisplay(){
		if(isListDiaplay){
			isListDiaplay = false;
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);
			
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
		}else {
			isListDiaplay = true;
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);
			
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}
}
