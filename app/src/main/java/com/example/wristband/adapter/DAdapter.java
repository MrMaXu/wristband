package com.example.wristband.adapter;

import android.graphics.Color;
import android.widget.BaseAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wristband.R;
import com.example.wristband.bean.Doing;
import com.example.wristband.utils.Constans;

import java.util.List;

/**
 * Created by 刘青林 on 2018/3/26.
 */

public class DAdapter extends BaseQuickAdapter<Doing,BaseViewHolder> {

    public static int DOING_BACKGROUND = 4;//背景样式，默认为五颜六色样式

    public DAdapter(int layoutResId,List data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Doing item) {
        helper.setText(R.id.doingname_tv,item.getName().toString());
        helper.setText(R.id.timelong_tv,String.valueOf(item.getTimeLong()));
        switch (DOING_BACKGROUND){
            case 0:
                helper.setTextColor(R.id.doingname_tv, Color.parseColor("grey"));
                helper.setTextColor(R.id.timelong_tv, Color.parseColor("grey"));
                helper.setBackgroundRes(R.id.item_img, Constans.sightsDrawables[helper.getPosition() % Constans.sightsDrawables.length]);
            break;
            case 1:
                helper.setTextColor(R.id.doingname_tv,Color.WHITE);
                helper.setTextColor(R.id.timelong_tv,Color.WHITE);
                helper.setBackgroundRes(R.id.item_img,Constans.wordDrawables[helper.getPosition() % Constans.wordDrawables.length]);
                break;
            case 2:
                helper.setBackgroundRes(R.id.item_img,Constans.yelloDrawables[helper.getPosition() % Constans.yelloDrawables.length]);
                break;
            case 3:
                helper.setBackgroundRes(R.id.item_img,Constans.alwaysDrawables[helper.getPosition() % Constans.alwaysDrawables.length]);
                break;
            case 4:
                helper.setBackgroundRes(R.id.item_img,Constans.Colors[helper.getPosition() % Constans.Colors.length]);
            default:
                break;

        }


    }
}
