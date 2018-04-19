package com.example.wristband.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wristband.R;
import com.example.wristband.bluetooth.ReceiveSocketService;
import com.example.wristband.bluetooth.SendSocketService;

import java.util.Calendar;
import java.util.TimeZone;

public class BlueToothSocketActivity extends AppCompatActivity implements View.OnClickListener {
    //聊天区域记录对话
    private LinearLayout content_ly;
    //输入信息
    private EditText et_go_edit_text;
    // 发送信息的按钮
    private Button btn_go_text;
    //修改番茄时间
    private Button change_tomato_time;
    //同步手机时间-向手环发送
    private Button btn_syn_time;
    //现在的番茄时间为：
    private TextView tv_now_tomato_time;
//获取手环番茄数
    private Button get_wis_tomato_nubmer;
    //显示手环番茄数
    private TextView wis_tomato_number;

    private String tomato_time;

    //当前系统时间
    private TextView phone_time;
    public String hour, minute, second;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://文本消息
                    if (TextUtils.isEmpty(msg.obj.toString())) return;
                    content_ly.addView(getLeftTextView(msg.obj.toString()));

                    break;
                case 2://系统时间
                    phone_time.setText(hour + ":" + minute + ":" + second);
                    break;

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetootn_socket);
        //获取手机模式番茄设置页面的 番茄时间
        Intent intent = getIntent();
        tomato_time = intent.getStringExtra("timeLong");

        content_ly = (LinearLayout) findViewById(R.id.content_ly);
        et_go_edit_text = (EditText) findViewById(R.id.et_go_edit_text);
        btn_go_text = (Button) findViewById(R.id.btn_go_text);
        //显示系统时间
        phone_time = findViewById(R.id.phone_time);



        //显示更改后的番茄时间
        tv_now_tomato_time = (TextView) findViewById(R.id.tv_now_tomato_time);
        //修改番茄时间
        change_tomato_time = (Button) findViewById(R.id.change_tomato_time);


        //获取手环番茄数
        get_wis_tomato_nubmer = findViewById(R.id.get_wis_tomato_nubmer);
        //显示手环今日番茄数
        wis_tomato_number = findViewById(R.id.wis_tomato_number);




        //显示计划表中的番茄时间
        tv_now_tomato_time.setText("当前番茄时间为" + tomato_time);
        //在同步按钮上添加监听事件
        change_tomato_time.setOnClickListener(this);
        btn_syn_time.setOnClickListener(this);
        //发送消息
        btn_go_text.setOnClickListener(this);



        //开启一个线程：获取服务端的服务，读取信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReceiveSocketService.receiveMessage(handler);
            }
        }).start();


        //开启线程，刷新当前系统时间
        new Thread() {
            @Override
            public void run() {

                try {
                    while (true) {
                        final Calendar c = Calendar.getInstance();
                        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                        hour = String.valueOf(c.get(Calendar.HOUR));
                        minute = String.valueOf(c.get(Calendar.MINUTE));
                        second = String.valueOf(c.get(Calendar.SECOND));
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                super.run();
            }
        }.start();

    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //获取左边文本，点击按钮，发送消息
            case R.id.btn_go_text://发送文本消息
                if (TextUtils.isEmpty(et_go_edit_text.getText().toString().trim())) return;
                SendSocketService.sendMessage(et_go_edit_text.getText().toString().trim());
                //布局中添加 发送的内容
                content_ly.addView(getRightTextView(et_go_edit_text.getText().toString().trim()));
                break;
            //点击按钮 发送番茄时间 到手环
            case R.id.change_tomato_time:
                 final EditText changed_tomato_time=new EditText(this);
              /*   final EditText changed_rest_time=new EditText(this);*/
                changed_tomato_time.setHint("在这里输入你要修改的番茄时间");
              /*  changed_rest_time.setHint("在这里输入你要修改的休息时间");*/
                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("请修改番茄工作时间")
                       .setIcon(R.drawable.fanqie)
                        .setView(changed_tomato_time);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tv_now_tomato_time.setText(changed_tomato_time.getText().toString());
                                SendSocketService.sendMessage("请将番茄时间修改为"+changed_tomato_time.getText().toString());
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();

                break;
          /*  //点击按钮  发送手机上的系统时间 到手环
            case R.id.btn_syn_time:
                SendSocketService.sendMessage("请将手环时间修改为");
                break;*/
            //获取手环今日番茄数——未完成
            case R.id.get_wis_tomato_nubmer:
                SendSocketService.sendMessage("请问目前手环记录的番茄数为多少个？");
                break;

        }
    }


    //在测试区域 输出信息
    private TextView getLeftTextView(String message) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        textView.setGravity(View.FOCUS_LEFT);
        textView.setBackgroundResource(android.R.color.darker_gray);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setLayoutParams(layoutParams);
        textView.setText(message);
        return textView;
    }

    //在测试区域 输出信息
    private TextView getRightTextView(String message) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        textView.setGravity(View.FOCUS_RIGHT);
        textView.setBackgroundResource(android.R.color.white);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setLayoutParams(layoutParams);
        textView.setText(message);
        return textView;
    }
}
