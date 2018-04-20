package com.example.wristband.bluetooth;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.wristband.activities.BlueToothSocketActivity;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 发送消息的服务
 */
public class SendSocketService {

    /**
     * 发送文本消息
     *
     * @param message
     */
    public static void sendMessage(String message) {
        //判断蓝牙是否已经开启
        if (BltManager.getInstance().getmBluetoothAdapter() == null && !BltManager.getInstance().getmBluetoothAdapter().isEnabled()) {
            if (BltAppliaction.bluetoothSocket == null || TextUtils.isEmpty(message)) return;
            try {
                message += "\n";
                OutputStream outputStream = BltAppliaction.bluetoothSocket.getOutputStream();
                outputStream.write(message.getBytes("utf-8"));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else{

            Toast.makeText(BlueToothSocketActivity.blueToothSocketActivity,"发送失败,请检查蓝牙",Toast.LENGTH_SHORT);
        }
    }

}
