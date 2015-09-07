package com.practice.briefer.global;

/**
 * 定义全局参数
 * 
 * @author ZST
 *
 */
public class GlobalContants {

	public static final String SERVER_URL = "http://zhihuibj.sinaapp.com/zhbj";
//	public static final String SERVER_URL = "http://192.168.1.44:8080/zhbj";
	public static final String CATEGORIES_URL = SERVER_URL + "/categories.json";//获取分类信息
	public static final String PHOTOS_URL = SERVER_URL + "/photos/photos_1.json";//获取组图信息接口 - 数据出现问题
//	public static final String PHOTOS_URL = SERVER_URL + "";//获取组图信息接口
}
