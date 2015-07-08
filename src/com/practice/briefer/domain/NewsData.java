package com.practice.briefer.domain;

import java.util.ArrayList;

/**
 * 网络分类信息封装
 * 
 * 字段名必须和服务器返回的字段名一致，方便json解析
 * 
 * @author ZST
 *
 */
public class NewsData {

	// 根据json数据实际解析

	// 第一层数据
	public int retcode;
	public ArrayList<NewsMenuData> data;

	// 侧边连数据对象
	// 第二层数据-用内部类接收data的map数据
	public class NewsMenuData {
		public String id;// 有些id比较长，超出int范围，所以使用String接收
		public String title;
		public int type;
		public String url;

		public ArrayList<NewsTabData> children;

		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}
		
	}

	// 新闻页面下11个子页签的数据对象
	// 第三层数据-接收children的map数据
	public class NewsTabData {
		public String id;
		public String title;
		public int type;
		public String url;
		
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
	}

	//重写一下toString方法，方便打印NewsData这个对象中的变量
	@Override
	public String toString() {
		return "NewsData [data=" + data + "]";
	}
	
	
}
