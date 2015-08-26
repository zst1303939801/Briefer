package com.practice.briefer.domain;

import java.util.ArrayList;

/**
 * 组图数据
 * 
 * @author ZST
 *
 */
public class PhotoData {

	public int retcode;
	public PhotosInfo data;

	// json数据中的{}在这里面是一个对象,在上面表现的事一个对象：public PhotosInfo data;
	public class PhotosInfo {
		public String title;
		public ArrayList<PhotoInfo> news;
	}

	// json数据中的[]在这里是一个对象，但是在上面表现的是一个List: public ArrayList news;
	public class PhotoInfo {
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}
}
