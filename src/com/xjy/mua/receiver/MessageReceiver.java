package com.xjy.mua.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;
import com.baidu.android.pushservice.PushConstants;
import com.google.gson.Gson;
import com.xjy.mua.R;
import com.xjy.mua.activity.HomeActivity;
import com.xjy.mua.activity.MainActivity;
import com.xjy.mua.app.PushApplication;
import com.xjy.mua.exception.NetAccessException;
import com.xjy.mua.model.Friend;
import com.xjy.mua.model.User;
import com.xjy.mua.util.HttpRestAchieve;
import com.xjy.mua.util.L;
import com.xjy.mua.util.NetBackDataEntity;
import com.xjy.mua.util.SharePreferenceUtil;

import java.util.HashMap;
import java.util.Map;

public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = "MessageReceiver";
    public Gson gson = new Gson();

    public void onReceive(Context context, Intent intent) {
        L.d(TAG, ">>> Receive intent: \r\n" + intent);
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            // 获取消息内容
            String message = intent.getExtras().getString(
                    PushConstants.EXTRA_PUSH_MESSAGE_STRING);

            // 消息的用户自定义内容读取方式
            L.i(TAG, "onMessage: " + message);
            User u = gson.fromJson(message, User.class);
            showNotification(context, "MUA~", "您的好友 " + u.getName() + " 给您一个MUA~");

            // 自定义内容的json串
            L.d(TAG,
                    "EXTRA_EXTRA = "
                            + intent.getStringExtra(PushConstants.EXTRA_EXTRA));

            // 用户在此自定义处理消息,以下代码为demo界面展示用

        } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
            // 处理绑定等方法的返回数据
            // PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到

            // 获取方法
            final String method = intent
                    .getStringExtra(PushConstants.EXTRA_METHOD);
            // 方法返回错误码。若绑定返回错误（非0），则应用将不能正常接收消息。
            // 绑定失败的原因有多种，如网络原因，或access token过期。
            // 请不要在出错时进行简单的startWork调用，这有可能导致死循环。
            // 可以通过限制重试次数，或者在其他时机重新调用来解决。
            int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
                    PushConstants.ERROR_SUCCESS);
            String content = "";
            if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
                // 返回内容
                content = new String(
                        intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
                L.d(TAG, content);
                SharePreferenceUtil sharePreferenceUtil = PushApplication.getInstance().getSpUtil();
                if (sharePreferenceUtil.getAppId().equals("") || !sharePreferenceUtil.getUpload()) {

                    HashMap hm = gson.fromJson(content, HashMap.class);
                    L.d(TAG, hm.toString());
                    Map hashMap = (Map) hm.get("response_params");
                    L.d(TAG, hashMap.toString());
                    String appId = (String) hashMap.get("appid");
                    String channelId = (String) hashMap.get("channel_id");
                    String userId = (String) hashMap.get("user_id");
                    sharePreferenceUtil.setAppId(appId);
                    sharePreferenceUtil.setChannelId(channelId);
                    sharePreferenceUtil.setUserId(userId);

                    HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("id", sharePreferenceUtil.getId().toString());
                    params.put("appId", appId);
                    params.put("channelId", channelId);
                    params.put("userId", userId);
                    NetBackDataEntity netBackDataEntity = null;
                    try {
                        netBackDataEntity = httpRestAchieve.postRequestData(context.getString(R.string.base_url) + "user/app", params);
                    } catch (NetAccessException e) {
                        e.printStackTrace();
                    }
                    if (netBackDataEntity != null) {
                        String data = netBackDataEntity.getData();
                        if (data.contains("fail")) {
                            Toast.makeText(context,"发送设备信息失败，可能导致接收不到消息或者发送不了消息，请重新登录",Toast.LENGTH_LONG).show();
                        } else {
                            sharePreferenceUtil.setUpload(true);
                        }
                    }
                }
            }

            // 用户在此自定义处理消息,以下代码为demo界面展示用
            L.d(TAG, "onMessage: method : " + method);
            L.d(TAG, "onMessage: result : " + errorCode);
            L.d(TAG, "onMessage: content : " + content);
            /*Toast.makeText(
                    context,
                    "method : " + method + "\n result: " + errorCode
                            + "\n content = " + content, Toast.LENGTH_SHORT
            )
                    .show();*/


            // 可选。通知用户点击事件处理
        } else if (intent.getAction().equals(
                PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
            L.d(TAG, "intent=" + intent.toUri(0));

            // 自定义内容的json串
            String customData = intent
                    .getStringExtra(PushConstants.EXTRA_EXTRA);

            L.d(TAG,
                    "EXTRA_EXTRA = "
                            + intent.getStringExtra(PushConstants.EXTRA_EXTRA));

        }
    }
    private static int NOTIFY_ID = 1000;
    /**
     * 在状态栏显示通知
     */
    private void showNotification(Context context, String title, String text){
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // 定义Notification的各种属性
        Notification notification =new Notification(R.drawable.ic_launcher,
                "MUA~", System.currentTimeMillis());
        //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
        //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
        //FLAG_ONGOING_EVENT 通知放置在正在运行
        //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
        //DEFAULT_LIGHTS  使用默认闪光提示
        //DEFAULT_SOUNDS  使用默认提示声音
        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
        notification.defaults = Notification.DEFAULT_VIBRATE;
        //叠加效果常量
        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS =5000; //闪光时间，毫秒

        // 设置通知的事件消息
        Intent notificationIntent =new Intent(context, MainActivity.class); // 点击该通知后要跳转的Activity
        PendingIntent contentItent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, text, contentItent);

        // 把Notification传递给NotificationManager
        notificationManager.notify(0, notification);
    }
}
