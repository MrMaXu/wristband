package com.example.wristband;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.wristband.activities.BlueToothActivity;
import com.example.wristband.activities.BlueToothSocketActivity;
import com.example.wristband.activities.LoginActivity;
import com.example.wristband.activities.PhoneModeActivity;
import com.example.wristband.activities.StatisticsActivity;
import com.example.wristband.bluetooth.BltContant;
import com.example.wristband.bluetooth.BltManager;
import com.example.wristband.bluetooth.BltService;
import com.gc.materialdesign.widgets.ColorSelector;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //NavigationView headerde的点击事件
        View header = navigationView.inflateHeaderView(R.layout.main_nav_header);
        CircleImageView userImage = header.findViewById(R.id.icon_image);
        TextView userName = header.findViewById(R.id.username);
        TextView mail = header.findViewById(R.id.mail);
        userImage.setOnClickListener(this);
        userName.setOnClickListener(this);
        mail.setOnClickListener(this);

        //NavigationView menu的点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.statistics:
                        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.summary:
                        Toast.makeText(MainActivity.this,"点击总结",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        Toast.makeText(MainActivity.this,"点击关于",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.back_us:
                        Toast.makeText(MainActivity.this,"点击设置",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.help:
                        AlertDialog.Builder help = new AlertDialog.Builder(MainActivity.this);
                        final View helpDialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_nav_help,null);
                        final AlertDialog dialog = help.setView(helpDialogView).create();
                        dialog.setCanceledOnTouchOutside(true);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.BOTTOM);
                        Button connect = helpDialogView.findViewById(R.id.connect);
                        Button feedBack = helpDialogView.findViewById(R.id.back);
                        Button cancle = helpDialogView.findViewById(R.id.cancle);
                        connect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this,"点击联系",Toast.LENGTH_SHORT).show();
                            }
                        });
                        feedBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this,"点击反馈",Toast.LENGTH_SHORT).show();
                            }
                        });
                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.choose_color:
                        ColorSelector colorSelector = new ColorSelector(MainActivity.this, R.color.colorAccent, new ColorSelector.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int i) {
                                Toast.makeText(MainActivity.this,"颜色："+i,Toast.LENGTH_SHORT).show();
                            }
                        });
                        colorSelector.show();
                        break;
                    case R.id.choose_poster:
                        Toast.makeText(MainActivity.this,"点击选择海报",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.category);
        }

        fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(this);

        Button blueTooth = (Button) findViewById(R.id.blue_tooth);
        Button phone = (Button) findViewById(R.id.phone);
        blueTooth.setOnClickListener(this);
        phone.setOnClickListener(this);


    }

    //打开侧滑菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blue_tooth:

                Intent intent1 = new Intent(MainActivity.this, BlueToothActivity.class);
                startActivity(intent1);
                break;
            case R.id.phone:
                Intent intent = new Intent(MainActivity.this, PhoneModeActivity.class);
                startActivity(intent);
                break;
            case R.id.main_fab:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.username:
            case R.id.mail:
            case R.id.icon_image:
                Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                break;
            default:
                break;
        }
    }
    //关闭手机系统蓝牙的按钮
    public void test(View view){
        //打开系统自带蓝牙页面
        this.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
    }
}
