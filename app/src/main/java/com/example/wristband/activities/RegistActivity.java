package com.example.wristband.activities;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wristband.R;
import com.example.wristband.bean.ResponseInfo;
import com.example.wristband.utils.Constans;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class RegistActivity extends AppCompatActivity {

    private EditText phone;
    private EditText name;
    private EditText pass;

    private Button regist;

    private String u_phone;
    private String username;
    private String password;

    private CatLoadingView mCatLoading;

    private Handler registHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                mCatLoading.dismiss();
                Gson gson = new Gson();
                ResponseInfo responseInfo = gson.fromJson(msg.obj.toString(),ResponseInfo.class);
                if (responseInfo.getCode() == 1){
                    Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegistActivity.this,"注册失败，因为"+responseInfo.getContent(),Toast.LENGTH_SHORT).show();
                }

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        mCatLoading = new CatLoadingView();
        //表单项
        phone = findViewById(R.id.r_user_phone);
        name = findViewById(R.id.r_user_name);
        pass = findViewById(R.id.r_user_password);

        //按钮
        regist = findViewById(R.id.r_regist);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCatLoading.show(getSupportFragmentManager(),"正在注册···");
                u_phone = phone.getText().toString();
                username = name.getText().toString();
                password = pass.getText().toString();
                //注册
                userRegist();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:                 //点击返回
                finish();
                break;
        }
        return true;
    }

    private void userRegist() {
        final String url = Constans.httpHead + Constans.USER_REGISTER;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("u_phone",u_phone)
                        .addParams("username",username)
                        .addParams("password",password)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
//                                dialog.dismiss();
                                mCatLoading.dismiss();
                                Toast.makeText(RegistActivity.this,"注册失败，加载时间过长",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = response;
                                registHandler.sendMessage(message);
                            }
                        });
            }
        }).start();

    }
}
