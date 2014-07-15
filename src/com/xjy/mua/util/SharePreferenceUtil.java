package com.xjy.mua.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	public static final String MESSAGE_NOTIFY_KEY = "message_notify";
	public static final String MESSAGE_SOUND_KEY = "message_sound";
	public static final String SHOW_HEAD_KEY = "show_head";
	public static final String PULLREFRESH_SOUND_KEY = "pullrefresh_sound";
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// appid
	public void setAppId(String appid) {
		// TODO Auto-generated method stub
		editor.putString("appid", appid);
		editor.commit();
	}

	public String getAppId() {
		return sp.getString("appid", "");
	}

	// user_id
	public void setUserId(String userId) {
		editor.putString("userId", userId);
		editor.commit();
	}

	public String getUserId() {
		return sp.getString("userId", "");
	}

	// channel_id
	public void setChannelId(String ChannelId) {
		editor.putString("ChannelId", ChannelId);
		editor.commit();
	}

	public String getChannelId() {
		return sp.getString("ChannelId", "");
	}

    public void setId(Integer id) {
        editor.putInt("id", id);
        editor.commit();
    }

    public Integer getId(){
        return sp.getInt("id",-1);
    }

    public void setUsername(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername() {
        return sp.getString("username", "");
    }

    public void setLevel(Integer level) {
        editor.putInt("level", level);
        editor.commit();
    }

    public Integer getLevel(){
        return sp.getInt("level", -1);
    }

    public boolean getUpload(){
        return sp.getBoolean("upload", false);
    }

    public void setUpload(boolean upload) {
        editor.putBoolean("upload", true);
        editor.commit();
    }
}
