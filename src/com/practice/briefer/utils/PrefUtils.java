package com.practice.briefer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 对SharedPreferences封装
 * 
 * @author ZST
 *
 */
public class PrefUtils {

	public static final String PREF_NAME = "config";

	//boolean
	// ctx:传进来的context对象(调用这份方法的类)，key：是自定义的常用配置，defaultValue：value
	public static boolean getBoolean(Context ctx, String key,
			boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	// String
	public static String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

}
