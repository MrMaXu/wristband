package com.example.wristband.bluetooth;

import android.text.TextUtils;

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


}
