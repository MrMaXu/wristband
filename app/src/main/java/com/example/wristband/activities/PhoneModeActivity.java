package com.example.wristband.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wristband.MainActivity;
import com.example.wristband.R;
import com.example.wristband.adapter.DAdapter;
import com.example.wristband.bean.Doing;

import org.litepal.crud.DataSupport;

import java.util.List;

public class PhoneModeActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Doing> doingList;
    private RecyclerView doingRecycler;
    private DAdapter dAdapter;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_mode);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.phoneToolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //点击进入记事本、未来计划
        FloatingActionButton phoneFab = (FloatingActionButton) findViewById(R.id.phone_fab);
            phoneFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder help = new AlertDialog.Builder(PhoneModeActivity.this);
                    final View phoneBottom = LayoutInflater.from(PhoneModeActivity.this).inflate(R.layout.phone_bottom_dialog,null);
                    final AlertDialog dialog = help.setView(phoneBottom).create();
                    dialog.setCanceledOnTouchOutside(true);
                    ImageView node = phoneBottom.findViewById(R.id.node);
                    ImageView future = phoneBottom.findViewById(R.id.future);
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
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);
                    window.getDecorView().setPadding(0, 0, 0, 0);
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(lp);
                    window.setBackgroundDrawableResource(android.R.color.transparent);//占据宽度显示
                }
            });
        getData();
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
            case R.id.add:
                addDoing();
                break;
            default:
                break;
        }
        return true;
    }

    //添加活动
    private void addDoing() {
        //新建Dialog添加活动
        final AlertDialog.Builder add = new AlertDialog.Builder(PhoneModeActivity.this);
        final View dialogView = LayoutInflater.from(PhoneModeActivity.this).inflate(R.layout.phone_add_dialog,null);
        add.setTitle("添加活动");
        add.setView(dialogView);
        add.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText addName = (EditText) dialogView.findViewById(R.id.add_name);
                EditText addTimelong = (EditText) dialogView.findViewById(R.id.add_timelong);
                Doing doing = new Doing();
                doing.setName(addName.getText().toString());
                doing.setTimeLong(Integer.parseInt(addTimelong.getText().toString()));
                doing.save();
                //刷新视图
                refreshData();
            }
        });
        add.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        add.show();
    }

    //从数据库中获得数据
    private void getData() {
        List<Doing> doings = DataSupport.findAll(Doing.class);
        doingList = doings;
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
        dAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        dAdapter.isFirstOnly(false);
        doingRecycler.setAdapter(dAdapter);//doingAdapter
    }

    private void deleteData(int position) {
        DataSupport.deleteAll(Doing.class,"name = ?",doingList.get(position).getName());
        doingList.remove(position);
        refreshData();
    }

    //刷新数据
    public void refreshData() {
        doingList.clear();
        doingList.addAll(DataSupport.findAll(Doing.class));
        dAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.node:
                break;
            case R.id.future:
                break;
            default:
                break;
        }
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
}
