package com.practice.briefer.utils.bitmap;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 网络缓存
 * 
 * @author ZST
 *
 */
public class NetCacheUtils {

	/**
	 * 从网络下载图片
	 * 
	 * @param ivPic
	 * @param url
	 */
	public void getBitmapFromNet(ImageView ivPic, String url) {
		// 网络下载图片用到了异步
		new BitmapTask().execute(ivPic, url);// 启动AsyncTask,参数会在doInBackground中获取
	}

	/**
	 * Handler和线程池Thread的封装
	 * 
	 * 第一个泛型：参数类型 第二个泛型：更新进度的泛型 第三个泛型：onPostExecute的返回结果
	 * 
	 * @author ZST
	 *
	 */
	// 自己写一个类，实现AsyncTask这个类，必须实现和知道这三个回调方法
	class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView ivPic;
		private String url;

		/**
		 * 后台耗时方法再次执行，子线程运行
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			// 拿到值赋给成员变量
			ivPic = (ImageView) params[0];
			url = (String) params[1];

			return null;
		}

		/**
		 * 更新进度，现实下载图片的进度，主线程运行
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		/**
		 * 耗时方法结束后，执行该方法，主线程运行
		 */
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
		}

	}

	private void downloadBitmap(String url) {
		// 建立连接
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			
			conn.setConnectTimeout(5000);//连接超时
			conn.setReadTimeout(5000);//读取超时
			conn.setRequestMethod("GET");
			conn.connect();
			
			int responseCode = conn.getResponseCode();//获取响应码
			if(responseCode == 200) {
				
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
