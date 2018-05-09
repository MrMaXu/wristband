package com.example.wristband.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wristband.MainActivity;
import com.example.wristband.R;
import com.example.wristband.adapter.DAdapter;
import com.example.wristband.bean.Doing;
import com.example.wristband.utils.Constans;
import com.example.wristband.utils.ListDoingCallback;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhoneModeActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Doing> doingList = new ArrayList<>();//初始化
    private RecyclerView doingRecycler;
    private DAdapter dAdapter;
    private Context mContext = this;
    private CatLoadingView mCatLoading;//网络加载对话框

    private Button time25;
    private Button time30;
    private Button timeself;
    private int timeLong = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_mode);

        mCatLoading = new CatLoadingView();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.phoneToolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //点击进入记事本NoteBook、未来计划
        FloatingActionButton phoneFab = (FloatingActionButton) findViewById(R.id.phone_fab);
        phoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder help = new AlertDialog.Builder(PhoneModeActivity.this);
                final View phoneBottomView = LayoutInflater.from(PhoneModeActivity.this).inflate(R.layout.phone_bottom_dialog,null);
                final AlertDialog dialog = help.setView(phoneBottomView).create();
                dialog.setCanceledOnTouchOutside(true);
                ImageView node = phoneBottomView.findViewById(R.id.node);                //加载phone_bottom_dialog布局中的控件
                ImageView future = phoneBottomView.findViewById(R.id.future);
                //点击事件
                node.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PhoneModeActivity.this,NoteActivity.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });
                future.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PhoneModeActivity.this,NoteActivity.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });
                dialog.show();//显示dialog
                //设dialog宽度占据父布局显示
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
                window.setBackgroundDrawableResource(android.R.color.transparent);//占据宽度显示
            }
        });
        //getData();
        doingRecycler = (RecyclerView) findViewById(R.id.phone_recycler);
        setDoingRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doing_bac,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:                 //点击返回
                //修改为点击，退回主菜单。
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                addDoing();
                break;
            case R.id.refresh:
                mCatLoading.show(getSupportFragmentManager(),"正在刷新···");
                getData();
                break;
            case R.id.paint:
                DAdapter.DOING_BACKGROUND = 0;
                setDoingRecycler();
                break;
            case R.id.word:
                DAdapter.DOING_BACKGROUND = 1;
                setDoingRecycler();
                break;
            case R.id.yellow:
                DAdapter.DOING_BACKGROUND = 2;
                setDoingRecycler();
                break;
            case R.id.always:
                DAdapter.DOING_BACKGROUND = 3;
                setDoingRecycler();
                break;
            case R.id.colorful:
                DAdapter.DOING_BACKGROUND = 4;
                setDoingRecycler();
                break;
            default:
                break;
        }
        return true;
    }

    //添加活动，并在添加的时候同步到后台
    private void addDoing() {
        //新建Dialog添加活动
        AlertDialog.Builder addDialog = new AlertDialog.Builder(PhoneModeActivity.this);
        final View dialogView = LayoutInflater.from(PhoneModeActivity.this).inflate(R.layout.phone_add_dialog,null);//加载dialog布局

        time25 = dialogView.findViewById(R.id.time25);
        time30 = dialogView.findViewById(R.id.time30);
        timeself = dialogView.findViewById(R.id.timeself);
        time25.setOnClickListener(this);
        time30.setOnClickListener(this);
        timeself.setOnClickListener(this);

        addDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText addName = (EditText) dialogView.findViewById(R.id.add_name);
                Doing doing = new Doing();
                doing.setName(addName.getText().toString());
                doing.setTimeLong(timeLong);
                doing.save();
                //添加活动同步到后台
//                mCatLoading.show(getSupportFragmentManager(),"添加活动");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            OkHttpUtils
//                                    .get()
//                                    .url(Constans.httpHead+Constans.PLAN_INSERT)
//                                    .addParams("phone", Constans.USER_NAME)
//                                    .addParams("title",addName.getText().toString())
//                                    .addParams("duration",addTimelong.getText().toString())
//                                    .build()
//                                    .connTimeOut(5000)
//                                    .execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                    getData();
//                    setDoingRecycler();
                //刷新视图
                refreshData();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog add = addDialog.setView(dialogView).setTitle("添加活动").create();
        add.show();
        add.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1c79f2"));
        add.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1c79f2"));

    }

    //获取活动数据
    private void getData() {
        List<Doing> doings = DataSupport.findAll(Doing.class);//从数据库中获得数据
        doingList = doings;

        //从网络中获取计划表数据
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpUtils
//                        .get()
//                        .url(Constans.httpHead+Constans.PLAN_SELECT)
//                        .addParams("d_phone",Constans.USER_NAME)
//                        .build()
//                        .connTimeOut(5000)
//                        .execute(new ListDoingCallback() {
//                            @Override
//                            public void onError(Request request, Exception e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(List<Doing> doings) {
//                                doingList = doings;
//                                mCatLoading.dismiss();
//                                setDoingRecycler();//重新加载布局
//                            }
//                        });
//
//            }
//        }).start();
    }

    //删除一个活动
    private void deleteData(final int position) {
        DataSupport.deleteAll(Doing.class,"name = ?",doingList.get(position).getName());//在数据库中删除活动
        doingList.remove(position);
        refreshData();
        //在后台中删除活动
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpUtils
//                            .get()
//                            .url(Constans.httpHead+Constans.PLAN_DELETE)
//                            .addParams("p_id",doingList.get(position).getName())
//                            .build()
//                            .connTimeOut(5000)
//                            .execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        getData();
//        mCatLoading.dismiss();
//        setDoingRecycler();//重新加载布局
    }

    //刷新数据
    public void refreshData() {
        doingList.clear();
        doingList.addAll(DataSupport.findAll(Doing.class));
        dAdapter.notifyDataSetChanged();
    }

    //监听返回键，返回主菜单
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作
                // 监听到返回按钮点击事件
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //设置RecycleyView显示布局
    private void setDoingRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        doingRecycler.setLayoutManager(manager);
        dAdapter = new DAdapter(R.layout.doing_item,doingList);

        //长按删除数据
        dAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                final AlertDialog.Builder delete = new AlertDialog.Builder(mContext);
                delete.setTitle("删除活动");
                delete.setMessage("确定删除此活动？");
                delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        mCatLoading.show(getSupportFragmentManager(),"删除活动");
                        deleteData(position);
                    }
                });
                delete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                delete.show();
                return true;
            }
        });
        dAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Doing doing = doingList.get(position);
                Intent intent = new Intent();
                intent.putExtra("doingName",doing.getName());
                intent.putExtra("timeLong",doing.getTimeLong());
                intent.setClass(mContext,Starttask.class);
                mContext.startActivity(intent);
            }
        });
        dAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);//动画
        dAdapter.isFirstOnly(false);//每次都加载动画
        doingRecycler.setAdapter(dAdapter);//doingAdapter
    }

    //按钮点击
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.time25:
                timeLong = 25;
                time25.setSelected(true);
                time30.setSelected(false);
                timeself.setSelected(false);
                break;
            case R.id.time30:
                timeLong = 30;
                time25.setSelected(false);
                time30.setSelected(true);
                timeself.setSelected(false);
                break;
            case R.id.timeself:
                time25.setSelected(false);
                time30.setSelected(false);
                timeself.setSelected(true);
                AlertDialog.Builder numpicker = new AlertDialog.Builder(PhoneModeActivity.this);
                View numDialogView = LayoutInflater.from(PhoneModeActivity.this).inflate(R.layout.timelong_pick_dialog,null);
                numpicker.setPositiveButton("选好了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println(timeLong);
                        timeself.setText(timeLong+"分钟");
                    }
                });
                AlertDialog dialog = numpicker.setView(numDialogView).setTitle("选择时间").create();
                dialog.setCanceledOnTouchOutside(true);
                NumberPicker np = numDialogView.findViewById(R.id.np);
                np.setMaxValue(100);
                np.setMinValue(5);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        timeLong = i1;
                    }
                });
                dialog.show();
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF00ACED"));
            break;
        }
    }
}
