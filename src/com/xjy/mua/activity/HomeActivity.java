package com.xjy.mua.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.xjy.mua.R;
import com.xjy.mua.app.PushApplication;
import com.xjy.mua.db.FriendDB;
import com.xjy.mua.exception.NetAccessException;
import com.xjy.mua.model.Friend;
import com.xjy.mua.util.HttpRestAchieve;
import com.xjy.mua.util.NetBackDataEntity;
import com.xjy.mua.util.SharePreferenceUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends Activity implements AdapterView.OnItemClickListener{

    private final static String TAG = "HomeActivity";

    private ListView lv_friends;
    private List<Friend> friendList;
    private FriendDB friendDB = new FriendDB(this);
    private Context context = this;
    public FriendListAdapter friendListAdapter = new FriendListAdapter();
    final SharePreferenceUtil sharePreferenceUtil = PushApplication.getInstance().getSpUtil();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        lv_friends = (ListView) findViewById(R.id.friendList);

        friendList = friendDB.getAllFriends();
        lv_friends.setAdapter(friendListAdapter);
        lv_friends.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,1,getString(R.string.refresh_friends));
        menu.add(0,2,2,getString(R.string.mua_for_all));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == 1){
            HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("userId", sharePreferenceUtil.getId().toString());

            NetBackDataEntity netBackDataEntity = null;
            try {
                netBackDataEntity = httpRestAchieve.postRequestData(getString(R.string.base_url) + "user/friend", params);
            } catch (NetAccessException e) {
                e.printStackTrace();
            }
            if (netBackDataEntity != null) {
                String data = netBackDataEntity.getData();
//                    L.d(TAG, data);
                ArrayList<Friend> friends = new ArrayList<Friend>();
                try {
                    JSONArray friendList = new JSONArray(data);
                    for (int i = 0; i < friendList.length(); i++) {
                        JSONObject f = friendList.getJSONObject(i);
                        Gson gson = new Gson();
                        Friend friend = gson.fromJson(String.valueOf(f), Friend.class);
                        friends.add(friend);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                friendDB.deleteAll();
                friendDB.batchInsert(friends);
                friendListAdapter.notifyDataSetChanged();
            }
        }
        else if(item.getItemId() == 2){
            Toast t = Toast.makeText(this, "你选的是香蕉", Toast.LENGTH_SHORT);
            t.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView = (TextView) view.findViewById(R.id.mua);
        textView.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = createMuaAnimate(textView);
        valueAnimator.start();
        postMUA(String.valueOf(friendList.get(i).getFriendId()));
    }

    class FriendListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return friendList.size();
        }

        @Override
        public Object getItem(int i) {
            return friendList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = View.inflate(context, R.layout.friend_list_item, null);
            }
            TextView textView = (TextView) view.findViewById(R.id.name);
            textView.setText(friendList.get(i).getName());
            return view;
        }
    }

    public static ValueAnimator createMuaAnimate(final View view){
        ValueAnimator animator = ValueAnimator.ofInt(0, 50, 100, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
//                L.d(TAG, String.valueOf(value));
                TextView tv = (TextView) view;
                tv.setTextColor(Color.argb(value*255/100,252,228,236));
                tv.setBackgroundColor(Color.argb(value*255/100,233,30,99));
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        animator.setDuration(2500);
        return animator;
    }

    public void postMUA(String friendId){
        HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("userId", sharePreferenceUtil.getId().toString());
        params.put("friendId", friendId);

        NetBackDataEntity netBackDataEntity = null;
        try {
            netBackDataEntity = httpRestAchieve.postRequestData(getString(R.string.base_url) + "user/mua", params);
        } catch (NetAccessException e) {
            e.printStackTrace();
        }
        if (netBackDataEntity != null) {
            String data = netBackDataEntity.getData();
            System.out.println(data);
        }
    }
}