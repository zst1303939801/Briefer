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
		PrefUtils.setString(ctx, key, value);//sharedPreference的config.xml中放（缓存）好多配置文件，如果都把缓存放到SharedPreference中会很乱，根据实际请款防止不用文件里面
		//可以将缓存放在文件中，文件名就是Md5(url)，文件内容是json数据 - 注解：因为url有很多http://所以文件夹不会一次命名，可以使用Md5处理url
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
