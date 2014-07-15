package com.xjy.mua.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xjy.mua.R;
import com.xjy.mua.app.PushApplication;
import com.xjy.mua.exception.NetAccessException;
import com.xjy.mua.util.HttpRestAchieve;
import com.xjy.mua.util.L;
import com.xjy.mua.util.NetBackDataEntity;
import com.xjy.mua.util.SharePreferenceUtil;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private TextView tv_username;
    private TextView tv_password;
    private Context ctx = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharePreferenceUtil sharePreferenceUtil = PushApplication.getInstance().getSpUtil();
        if (sharePreferenceUtil.getId() != -1) {
            Intent intent = new Intent(this,HomeActivity.class);
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, PushApplication.API_KEY);
            this.startActivity(intent);
            this.finish();
        }

        Button button = (Button) findViewById(R.id.button);
        tv_username = (TextView) findViewById(R.id.username);
        tv_password = (TextView) findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, PushApplication.API_KEY);
                String username, password;
                if (tv_username.getText() != null) {
                    username = tv_username.getText().toString();
                } else {
                    Toast.makeText(ctx, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tv_password.getText() != null) {
                    password = tv_password.getText().toString();
                } else {
                    Toast.makeText(ctx, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                try {
                    NetBackDataEntity netBackDataEntity = httpRestAchieve.postRequestData(getString(R.string.base_url) + "user/login", params);
                    String data = netBackDataEntity.getData();
                    L.d(TAG, data);
                    if(data != null) {
                        Gson gson = new Gson();
                        HashMap map = gson.fromJson(data, HashMap.class);
                        SharePreferenceUtil sharePreferenceUtil = PushApplication.getInstance().getSpUtil();
                        sharePreferenceUtil.setId( ((Double) map.get("id")).intValue() );
                        sharePreferenceUtil.setUsername((String) map.get("username"));
                        sharePreferenceUtil.setLevel(((Double) map.get("level")).intValue());
                        Intent intent = new Intent(ctx,HomeActivity.class);
                        ctx.startActivity(intent);
                    }

                } catch (NetAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
