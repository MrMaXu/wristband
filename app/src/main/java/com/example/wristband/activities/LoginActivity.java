package com.example.wristband.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wristband.R;
import com.example.wristband.bean.ResponseInfo;
import com.example.wristband.utils.Constans;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText phone;
    private EditText pass;

    private Button login;
    private TextView regist;
    private TextView forget;

    private String u_phone;
    private String password;

    private CatLoadingView mCatLoading;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                mCatLoading.dismiss();
                Gson gson = new Gson();
                ResponseInfo  responseInfo = gson.fromJson(msg.obj.toString(),ResponseInfo.class);
                if (responseInfo.getCode() == 1){
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"登录失败，因为"+responseInfo.getContent(),Toast.LENGTH_SHORT).show();
                }

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCatLoading = new CatLoadingView();

        //表单项
        phone = findViewById(R.id.user_phone);
        pass = findViewById(R.id.user_password);

        //按钮
        login = findViewById(R.id.btn_login);
        regist = findViewById(R.id.btn_regist);
        forget = findViewById(R.id.forget);

        //设置全屏，保留状态栏文字
        Window window = getWindow();
        //默认API 最低19
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        }

        //登录监听事件
        login.setOnClickListener(this);
        //前往注册页面
        regist.setOnClickListener(this);
        //忘记密码
        forget.setOnClickListener(this);
    }

    private void userLogin() {
        final String url = Constans.httpHead + Constans.USER_JUDGE_LOGIN;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("u_phone",u_phone)
                        .addParams("password",password)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                mCatLoading.dismiss();
                                Toast.makeText(LoginActivity.this,"登录失败，加载时间过长",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = response;
                                mHandler.sendMessage(message);
                            }
                        });
            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mCatLoading.show(getSupportFragmentManager(),"正在登录···");
                u_phone = phone.getText().toString();
                password = pass.getText().toString();
                userLogin();
                break;
            case R.id.btn_regist:
                Intent registIntent = new Intent(this,RegistActivity.class);
                startActivity(registIntent);
                break;
            case R.id.forget:
                Toast.makeText(LoginActivity.this,"忘记密码，OH MY GOD！",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
