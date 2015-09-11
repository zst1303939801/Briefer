package com.practice.briefer.utils.bitmap;

import android.widget.ImageView;

/**
 * 自定义图片加载工具
 * 
 * @author ZST
 *
 */
public class MyBitmapUtils {

	NetCacheUtils mNetCacheUtils;

	public MyBitmapUtils() {
		mNetCacheUtils = new NetCacheUtils();
	}

	public void display(ImageView ivPic, String url) {
		// 用户第一次进来先从内存读

		// 从本地

		// 从网络
		mNetCacheUtils.getBitmapFromNet(ivPic, url);
	}

}
