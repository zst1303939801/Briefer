package com.practice.briefer.utils;

import android.content.Context;

/**
 * 缓存工具类
 * 
 * @author ZST
 *
 */
public class CacheUtils {

	/**
	 * 设置缓存key：url ， value：json ,搞成静态的static可以直接通过（类名.方法名）调用
	 * 
	 * @param key
	 * @param value
	 * @param ctx
	 */
	public static void setCache(String key, String value, Context ctx) {
		PrefUtils.setString(ctx, key, value);
	}

	/**
	 * 获取缓存key：url
	 * 
	 * @param key
	 * @param ctx
	 * @return
	 */
	public static String getCache(String key, Context ctx) {
		return PrefUtils.getString(ctx, key, null);
	}
}
