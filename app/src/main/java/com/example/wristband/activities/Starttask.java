package com.example.wristband.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.wristband.R;
import com.john.waveview.WaveView;

import info.abdolahi.CircularMusicProgressBar;


public class Starttask extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = this;
    private WaveView waveView;
    private CircularMusicProgressBar progressBar;
    private TextView timeShow;
    private ViewFlipper flipper;
    private ImageButton quit;
    private ImageButton pause;

    public double TIME_PROCESS = 0;
    public String doingName ;
    public int timeLong = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starttask);
        timeShow = (TextView) findViewById(R.id.count_down_time);
        quit = findViewById(R.id.quit);
        pause = findViewById(R.id.pause);
        quit.setOnClickListener(this);
        pause.setOnClickListener(this);


        doingName = getIntent().getStringExtra("doingName");            //活动名称
        timeLong = getIntent().getIntExtra("timeLong",25);//上个活动传送的数据，活动名称

        waveView = (WaveView) findViewById(R.id.wave_view);        //波浪背景
        waveView.setProgress(0);                                  //初始化

        progressBar = (CircularMusicProgressBar) findViewById(R.id.album_art);//带图片的进度条
        progressBar.setValue(0);                                   //初始化

        setFill();//设置全屏，保留状态栏文字

        setFlipper();//背景轮播图

//        workTimer.start();//倒计时
        restTimer.start();

    }

    private void setFlipper() {
        flipper = findViewById(R.id.vf);
        flipper.setFlipInterval(8000);//设置自动切换的间隔时间
        flipper.startFlipping();//开启切换效果
    }

    //工作倒计时
    private CountDownTimer workTimer = new CountDownTimer( (25*60*1000), 1000) {

        @Override
        public void onTick(long millisUntilFinished) {   //millisUntilFinished是还剩多长时间
            timeShow.setText("还需工作"+(millisUntilFinished/(1000*60))+":"+((millisUntilFinished / 1000 )) % 60+"");
            long pastTime = (25*60*1000) - millisUntilFinished;
            TIME_PROCESS = pastTime / (25*60*10);
            waveView.setProgress((int) TIME_PROCESS);
            progressBar.setValue((float) TIME_PROCESS);
        }

        @Override
        public void onFinish() {
            workTimer.cancel();
            restTimer.start();
        }

        public void onStop() {
            onFinish();
        }


    };

    //休息倒计时
    private CountDownTimer restTimer = new CountDownTimer(10*1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {   //millisUntilFinished是还剩多长时间
            timeShow.setText("还能休息"+(millisUntilFinished/(1000*60))+":"+((millisUntilFinished / 1000 )) % 60+"");
            long pastTime = 10*1000 - millisUntilFinished;
            TIME_PROCESS = pastTime / (10*10);
            waveView.setProgress((int)TIME_PROCESS);
            progressBar.setValue((float) TIME_PROCESS);
        }

        @Override
        public void onFinish() {
            AlertDialog.Builder startTaskDia = new AlertDialog.Builder(mContext);
            startTaskDia.setTitle("是否为您开启活动？")
                    .setMessage("直接开启倒计时")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            restTimer.cancel();
                            workTimer.start();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }
    };

    public void setFill() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {//默认API 最低19
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        }

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        workTimer.cancel();
//        System.out.println("退出");
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quit:
                AlertDialog.Builder quit = new AlertDialog.Builder(mContext);
                quit.setTitle("退出")
                        .setMessage("此次任务时间将不会计算在工作时间内\n确定退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                workTimer.cancel();
                                restTimer.cancel();
//                                finish();
                                System.out.println("退出");
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                break;
            case R.id.pause:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        workTimer.cancel();
        restTimer.cancel();
    }
}
