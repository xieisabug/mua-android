package com.xjy.mua.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xjy.mua.baidu.BaiduPush;
import com.xjy.mua.util.SharePreferenceUtil;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class PushApplication extends Application {
	public final static String API_KEY = "vkuBbc45CzsUXAxUyGfk5ZZi";
	public final static String SECRIT_KEY = "MUpsQKygLZzXu7G2UzwRXXfoxQMnbNdl";
	public static final String SP_FILE_NAME = "push_msg_sp";
	private static PushApplication mApplication;
	private BaiduPush mBaiduPushServer;
	private SharePreferenceUtil mSpUtil;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private Gson mGson;

	public synchronized static PushApplication getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		initData();
	}

	private void initData() {
		mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
				SECRIT_KEY, API_KEY);
		// 不转换没有 @Expose 注解的字段
		mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}

	public synchronized BaiduPush getBaiduPush() {
		if (mBaiduPushServer == null)
			mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
					SECRIT_KEY, API_KEY);
		return mBaiduPushServer;

	}

	public synchronized Gson getGson() {
		if (mGson == null)
			// 不转换没有 @Expose 注解的字段
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
		return mGson;
	}

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}

	public synchronized SharePreferenceUtil getSpUtil() {
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		return mSpUtil;
	}

}
