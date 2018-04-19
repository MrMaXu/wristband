package com.example.wristband.activities;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.wristband.R;
import com.example.wristband.bean.ResponseInfo;
import com.example.wristband.utils.Constans;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText phone;
    private EditText name;
    private EditText pass;
    private Button login;
    private Button regist;

    private String u_phone;
    private String username;
    private String password;

    private ProgressDialog dialog;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                dialog.dismiss();
                Gson gson = new Gson();
                ResponseInfo  responseInfo = gson.fromJson(msg.obj.toString(),ResponseInfo.class);
                if (responseInfo.getCode() == 1){
                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"注册失败，因为"+responseInfo.getContent(),Toast.LENGTH_SHORT).show();
                }

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //表单项
        phone = findViewById(R.id.user_phone);
        name = findViewById(R.id.user_name);
        pass = findViewById(R.id.user_password);

        //按钮
        login = findViewById(R.id.btn_login);
        regist = findViewById(R.id.btn_regist);

        //设置全屏，保留状态栏文字
        Window window = getWindow();
        //默认API 最低19
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        }

        //注册按钮监听事件
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //数据加载提示框
                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setTitle("提示信息");
                dialog.setMessage("正在注册，请稍后...");
                dialog.setCancelable(true);
                dialog.show();

                u_phone = phone.getText().toString();
                username = name.getText().toString();
                password = pass.getText().toString();

                //注册
                userRegist();
            }
        });

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
                        .connTimeOut(20000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                dialog.dismiss();
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
}
