package com.example.wristband.bean;

import org.litepal.crud.DataSupport;

public class Note  extends DataSupport {

    private String content;
    private String timeStamp;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
