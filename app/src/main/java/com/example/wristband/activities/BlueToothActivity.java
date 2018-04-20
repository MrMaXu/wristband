package com.example.wristband.activities;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wristband.R;
import com.example.wristband.bluetooth.BltContant;
import com.example.wristband.bluetooth.BltManager;
import com.example.wristband.bluetooth.BltService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlueToothActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //蓝牙开关
    private Switch blue_switch;
    //搜索到的蓝牙列表
    private ListView blue_list;
    private List<BluetoothDevice> bltList;
    //自定义适配器
    private BlueToothActivity.MyAdapter myAdapter;
    //搜索蓝牙时的圆形滚动条
    private ProgressBar btl_bar;
    //滚动条后的提示文本
    private TextView blt_status_text;

    boolean a;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://搜索蓝牙
                    break;
                case 2://蓝牙可以被搜索
                    break;
                //未连接蓝牙，则从搜索连接开始
                case 3://设备已经接入
                    //搜索蓝牙时的圆形滚动条可见
                    btl_bar.setVisibility(View.GONE);
                    //指代对方蓝牙设备
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    //更改滚动条后的提示文本为：某 蓝牙设备已经介入
                    blt_status_text.setText("设备" + device.getName() + "已经接入");
                    Toast.makeText(BlueToothActivity.this, "设备" + device.getName() + "已经接入", Toast.LENGTH_LONG).show();
                    //蓝牙连接后跳转到 同步页面
                    Intent intent = new Intent(BlueToothActivity.this, BlueToothSocketActivity.class);
                    startActivity(intent);
                    break;
                //已经连接蓝牙，则从当前页面跳转到 同步页面
                case 4://已连接某个设备
                    //搜索蓝牙时的圆形滚动条可见
                    btl_bar.setVisibility(View.GONE);
                    //指代对方蓝牙设备
                    BluetoothDevice device1 = (BluetoothDevice) msg.obj;
                    //更改滚动条后的提示文本为：某 蓝牙设备已经介入
                    blt_status_text.setText("已连接" + device1.getName() + "设备");
                    Toast.makeText(BlueToothActivity.this, "已连接" + device1.getName() + "设备", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(BlueToothActivity.this, BlueToothSocketActivity.class);
                    startActivity(intent1);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_match);

        //调用BltManager中的方法 初始化检测蓝牙
        BltManager.getInstance().initBltManager(this);
        //初始化
        initView();
        initData();
    }

    //初始化视图
    private void initView() {

        blue_switch = findViewById(R.id.blue_switch);
        blue_list = findViewById(R.id.blue_list);
        btl_bar = findViewById(R.id.btl_bar);
        blt_status_text = findViewById(R.id.blt_status_text);

        //设置进度条可见
        btl_bar.setVisibility(View.VISIBLE);


    }

    //初始化数据
    private void initData() {
        //搜索进度条不可见
        btl_bar.setVisibility(View.GONE);
        bltList = new ArrayList<>();
        myAdapter = new MyAdapter();
        blue_list.setOnItemClickListener(this);
        blue_list.setAdapter(myAdapter);



       /* removePairDevice();*/
        //检查蓝牙是否开启
        BltManager.getInstance().checkBleDevice(this);
        //注册蓝牙扫描广播
        blueToothRegister();



  /* //定义穿戴设备
       int health=BltManager.getInstance().getmBluetoothAdapter().getProfileConnectionState(BluetoothProfile.HEALTH);*/




  

        //暂时采用这种方式进行判断·········
        if (BltService.getInstance().getBluetoothServerSocket() != null) {
            Intent intent1 = new Intent(BlueToothActivity.this, BlueToothSocketActivity.class);
            startActivity(intent1);
        } else {
            removePairDevice();
            //更新蓝牙开关状态
            checkBlueTooth();
            //第一次进来搜索设备
            BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
        }





     /*   BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);

            if (state == BluetoothAdapter.STATE_CONNECTED) {

                Set<BluetoothDevice> devices = adapter.getBondedDevices();


                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (isConnected) {
                        Intent intent1 = new Intent(BlueToothActivity.this, BlueToothSocketActivity.class);
                        startActivity(intent1);
                    }else{
                        BltManager.getInstance().checkBleDevice(this);
                        blueToothRegister();
                        checkBlueTooth();
                        BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    /**
     * 注册蓝牙回调广播
     */
    private void blueToothRegister() {
        BltManager.getInstance().registerBltReceiver(this, new BltManager.OnRegisterBltReceiver() {

            /**
             * 搜索到新设备
             * @param device
             */
            @Override
            public void onBluetoothDevice(BluetoothDevice device) {
                if (bltList != null && !bltList.contains(device)) {
                    bltList.add(device);
                }
                if (myAdapter != null)
                    myAdapter.notifyDataSetChanged();
            }

            /**连接中
             * @param device
             */
            @Override
            public void onBltIng(BluetoothDevice device) {
                btl_bar.setVisibility(View.VISIBLE);
                blt_status_text.setText("连接" + device.getName() + "中……");
            }

            /**连接完成，跳转 手环设置页面
             * @param device
             */
            @Override
            public void onBltEnd(BluetoothDevice device) {
                btl_bar.setVisibility(View.GONE);
                blt_status_text.setText("连接" + device.getName() + "完成");
                a = true;
//连接完成，跳 同步 页面
                Intent intent = new Intent(BlueToothActivity.this, BlueToothSocketActivity.class);
                startActivity(intent);
            }

            /**取消连接
             * @param device
             */
            @Override
            public void onBltNone(BluetoothDevice device) {
                //蓝牙不可见
                btl_bar.setVisibility(View.GONE);
                blt_status_text.setText("取消了连接" + device.getName());
            }
        });
    }

    /**
     * 检查蓝牙的开关状态
     */


    private void checkBlueTooth() {
        if (BltManager.getInstance().getmBluetoothAdapter() == null && !BltManager.getInstance().getmBluetoothAdapter().isEnabled()) {
            //蓝牙开关 显示  关
            blue_switch.setChecked(false);
        } else
            //蓝牙开关 显示  开
            blue_switch.setChecked(true);
        //设置蓝牙开关的 点击事件：
        blue_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //启用蓝牙
                    BltManager.getInstance().clickBlt(BlueToothActivity.this, BltContant.BLUE_TOOTH_OPEN);

                    //设置进度条
                    btl_bar.setVisibility(View.VISIBLE);
                    blt_status_text.setText("正在搜索设备，请等待");
                    BltManager.getInstance().clickBlt(BlueToothActivity.this, BltContant.BLUE_TOOTH_SEARTH);
                } else
                    //禁用蓝牙
                    BltManager.getInstance().clickBlt(BlueToothActivity.this, BltContant.BLUE_TOOTH_CLOSE);
            }
        });
    }

    //点击搜索蓝牙的开关，开始搜索周围蓝牙
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searth_switch://搜索设备的按钮
                btl_bar.setVisibility(View.VISIBLE);
                blt_status_text.setText("正在搜索设备，请等待");
                BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
                break;
            case R.id.create_service://创建服务端
                btl_bar.setVisibility(View.VISIBLE);
                blt_status_text.setText("正在等待设备加入");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BltService.getInstance().run(handler);
                    }
                }).start();
                break;
        }
    }

    //点击搜索到的可连接的蓝牙设备，点击选中 开始连接
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final BluetoothDevice bluetoothDevice = bltList.get(position);
        //滚动度可见
        btl_bar.setVisibility(View.VISIBLE);
        blt_status_text.setText("正在连接" + bluetoothDevice.getName());
        //链接的操作应该在子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                BltManager.getInstance().createBond(bluetoothDevice, handler);
            }
        }).start();


    }

    //自定义蓝牙适配器
    private class MyAdapter extends BaseAdapter {
        //返回搜索到的蓝牙数量个列表项
        @Override
        public int getCount() {
            return bltList.size();
        }

        //返回值决定第position处的列表项的内容
        @Override
        public Object getItem(int position) {
            return bltList.get(position);
        }

        //返回值决定第position处的列表项的ID
        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        //返回值决定第position处的列表项的组件
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder vh;
            // 从集合中获取当前行的数据
            BluetoothDevice device = bltList.get(position);
            if (convertView == null) {
                // 说明当前这一行不是重用的
                // 加载行布局文件，产生具体的一行
                v = getLayoutInflater().inflate(R.layout.bluetooth_item, null);
                // 创建存储一行控件的对象
                vh = new ViewHolder();
                // 将该行的控件全部存储到vh中
                vh.blt_name = v.findViewById(R.id.blt_name);
                vh.blt_address = v.findViewById(R.id.blt_address);
                vh.blt_type = v.findViewById(R.id.blt_type);
                vh.blt_bond_state = v.findViewById(R.id.blt_bond_state);
                v.setTag(vh);// 将vh存储到行的Tag中
            } else {
                v = convertView;
                // 取出隐藏在行中的Tag--取出隐藏在这一行中的vh控件缓存对象
                vh = (ViewHolder) convertView.getTag();
            }

            // 从ViewHolder缓存的控件中改变控件的值
            // 这里主要是避免多次强制转化目标对象而造成的资源浪费
            vh.blt_name.setText("蓝牙名称：" + device.getName());
            vh.blt_address.setText("蓝牙地址:" + device.getAddress());
            vh.blt_type.setText("蓝牙类型:" + device.getType());
            vh.blt_bond_state.setText("蓝牙状态:" + BltManager.getInstance().bltStatus(device.getBondState()));
            return v;
        }

        private class ViewHolder {
            TextView blt_name, blt_address, blt_type, blt_bond_state;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //页面关闭的时候要断开蓝牙
        BltManager.getInstance().unregisterReceiver(this);
    }

    //得到配对的设备列表，清除已配对的设备
    public void removePairDevice() {
        if (BltManager.getInstance().getmBluetoothAdapter() != null) {
            Set<BluetoothDevice> bondedDevices = BltManager.getInstance().getmBluetoothAdapter().getBondedDevices();
            for (BluetoothDevice device : bondedDevices) {
                unpairDevice(device);
            }
        }

    }

    //反射来调用BluetoothDevice.removeBond取消设备的配对
    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {

        }
    }
}

