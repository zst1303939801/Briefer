package com.practice.briefer;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * 新闻详情页
 * 
 * @author ZST
 *
 */
public class NewsDetailActivity extends Activity implements OnClickListener {

	private WebView mWebView;
	private ImageButton btnBack;
	private ImageButton btnSize;
	private ImageButton btnShare;

	private ProgressBar pbProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		mWebView = (WebView) findViewById(R.id.wv_web);
		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnSize = (ImageButton) findViewById(R.id.btn_size);
		btnShare = (ImageButton) findViewById(R.id.btn_share);

		btnBack.setOnClickListener(this);
		btnSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);

		String url = getIntent().getStringExtra("url");// 得到传递过来的url地址 -
														// 两个Activity之间传递数据只用intent
		System.out.println("url" + url);

		WebSettings settings = mWebView.getSettings();// 拿到webview的setting
		settings.setJavaScriptEnabled(true);// 表示支持js，默认是关闭js的
		// settings.setBuiltInZoomControls(true);//显示放大缩小按钮 - 很难看
		settings.setUseWideViewPort(true);// 支持双击缩放

		mWebView.setWebViewClient(new WebViewClient() {
			// 实现回调方法
			// 网页开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("网页开始加载");
				pbProgress.setVisibility(View.VISIBLE);
			}

			// 网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页加载结束");
				pbProgress.setVisibility(View.GONE);
			}

			// 所有页面跳转链接都会在此回调，url：页面中的跳转链接
			// 默认页面中的url是使用手机默认浏览器进行跳转显示
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("跳转url:" + url);

				// tel:110 - 拦截页面是tel开始的可以直接打电话的逻辑
				view.loadUrl(url);// 强制加载网页，这样就能实现在软件中访问网页

				return true;
				// return super.shouldOverrideUrlLoading(view, url);
			}
		});

		// mWebView.goBack();//页面向后，类似uc的前一页
		// mWebView.goForward();//后一页

		mWebView.setWebChromeClient(new WebChromeClient() {

			// 网页加载进度变化
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("网页加载进度:" + newProgress);
				super.onProgressChanged(view, newProgress);
			}

			// 获取网页标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				System.out.println("网页标题:" + title);
				super.onReceivedTitle(view, title);
			}
		});

		mWebView.loadUrl(url);// 加载网页
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();// 当点击返回只用结束当前的Activity就可以了
			break;
		case R.id.btn_size:
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();
			break;
		default:
			break;
		}
	}

	private int mCurrentChooseItem;// 记录当前字体选择位置,记录那个被点击
	private int mCurrentItem = 2;// 记录确定那个被点击,若想持久化则可以使用SharePreference来记录

	/**
	 * 显示对话选择框
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);// 安卓的对话框

		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
		builder.setTitle("字体设置");
		builder.setSingleChoiceItems(items, mCurrentItem,
				new DialogInterface.OnClickListener() {// 2默认的选中

					@Override
					public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
						System.out.println("选中：" + which);
						mCurrentChooseItem = which;
					}
				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {// 确定这个which位置永远是0
				WebSettings settings = mWebView.getSettings();// 很多方法都在getSettings里面，所以要拿到settings

				switch (mCurrentChooseItem) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					// settings.setTextZoom(20);//api 14以上才能用
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;
				default:
					break;
				}

				mCurrentItem = mCurrentChooseItem;
			}
		});

		builder.setNegativeButton("取消", null);

		builder.show();// 让弹出框显示
	}

	/**
	 * ShareSDK分享代码，注意在sdcard根目录放jpg
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		
		//oks.setTheme//设置主题，但是我的sdk没有这个主题，sdk有问题
		
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
