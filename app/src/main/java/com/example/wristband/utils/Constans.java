package com.example.wristband.utils;

import com.example.wristband.R;

import java.util.List;

/**
 * Created by 刘青林 on 2018/3/19.
 */

public class Constans {

    public static String httpHead = "http://192.168.128.66:8080/";

    //User Controller Url
    public static String USER_JUDGE_LOGIN = "user/judgeLogin";
    public static String USER_DELETE_USER = "user/deleteUser";
    public static String USER_REGISTER = "user/register";
    public static String USER_UPDATE_USER= "user/updateUser";

    //Plan Controller Url
    public static String PLAN_DELETE = "plan/delete";
    public static String PLAN_INSERT = "plan/insert";
    public static String PLAM_UPDATE = "plan/update";
    public static String PLAN_SELECT = "plan/select";

    //Notebook Controller Url
    public static String NOTEBOOK_DELETE = "notebook/delete";
    public static String NOTEBOOK_INSERT = "notebook/insert";
    public static String NOTEBOOK_UPDATE = "notebook/update";
    public static String NOTEBOOK_SELECT = "notebook/select";

    //Diary Controller Url
    public static String DIARY_SELECT = "diary/select";
    public static String DIARY_SELECT_ALLTIME = "diary/selectAllTime";
    public static String DIARY_SELECT_ALLPTOMATO = "diary/selectAllTomato";
    public static String DIARY_UPDATE = "diary/update";


    public static int relaxTime;//休息时间

    public static Integer[] Colors = new Integer[] {
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10,
            R.color.color_11,
            R.color.color_12,
            R.color.color_13,
            R.color.color_14,
            R.color.color_15,
            R.color.color_16,
            R.color.color_17,
            R.color.color_18,
            R.color.color_19,
            R.color.color_20,
            R.color.color_21,
            R.color.color_22,
            R.color.color_23,
            R.color.color_24,
            R.color.color_25,
            R.color.color_26
    };

    public static Integer[] sightsDrawables = new Integer[] {
            R.drawable.nav_bac,
            R.drawable.p2,
            R.drawable.p4,
            R.drawable.p5,
            R.drawable.p6,
            R.drawable.p7,
            R.drawable.p8,
            R.drawable.p9,
            R.drawable.p10,
            R.drawable.p11,
            R.drawable.p12,
            R.drawable.p15,
            R.drawable.p17,
            R.drawable.p18,
    };

    public static Integer[] wordDrawables = new Integer[] {
            R.drawable.p25,
            R.drawable.p26,
            R.drawable.p27,
            R.drawable.p28,
            R.drawable.p29,
            R.drawable.p30,
            R.drawable.p31,
            R.drawable.p32,
            R.drawable.p33,
    };

    public static Integer[] alwaysDrawables = new Integer[] {
            R.drawable.p23,
            R.drawable.p24,
            R.drawable.p34,
            R.drawable.p35,
            R.drawable.p36,
            R.drawable.p37,
            R.drawable.p38,
            R.drawable.p39,
            R.drawable.p40,
            R.drawable.p41,
    };

    public static Integer[] yelloDrawables = new Integer[] {
            R.drawable.p19,
            R.drawable.p20,
            R.drawable.p21,
            R.drawable.p22,
    };

}
