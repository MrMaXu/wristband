package com.example.wristband.bean;

import org.litepal.crud.DataSupport;

public class Doing extends DataSupport{

    private String name;
    private int timeLong;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(int timeLong) {
        this.timeLong = timeLong;
    }
}
