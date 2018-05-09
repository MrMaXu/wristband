package com.example.wristband.bean;

/**
 * Created by lql on 2018/4/18.
 */

public class ResponseInfo {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * code : 1
     * content : 手机号已存在
     */
    private int code;
    private String content;
}
