package com.example.wristband.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wristband.R;
import com.example.wristband.activities.PhoneModeActivity;
import com.example.wristband.activities.Starttask;
import com.example.wristband.bean.Doing;
import com.example.wristband.utils.Constans;

import org.litepal.crud.DataSupport;

import java.util.List;

import javax.xml.transform.Templates;


public class DoingAdapter extends RecyclerView.Adapter<DoingAdapter.Viewholder> {

    public static int DOING_BAC = 4;//背景样式，默认为五颜六色样式

    private Context mContext;

    private List<Doing> mDoingList;

    static class Viewholder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView doingName;
        TextView timeLong;
        ImageView imageView;

        //内部类找到布局中的控件
        public Viewholder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            doingName =  itemView.findViewById(R.id.doingname_tv);
            timeLong =  itemView.findViewById(R.id.timelong_tv);
            imageView = itemView.findViewById(R.id.item_img);
        }
    }

    public DoingAdapter(List<Doing> doingList) {
        mDoingList = doingList;
    }

    @Override
    public Viewholder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.doing_item,parent,false);
        final Viewholder holder = new Viewholder(view);
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder delete = new AlertDialog.Builder(mContext);
                delete.setTitle("删除活动");
                delete.setMessage("确定删除此活动？");
                delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteData(holder.getAdapterPosition());
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext,"item短点击",Toast.LENGTH_SHORT).show();
                Doing doing = mDoingList.get(holder.getAdapterPosition());
                Intent intent = new Intent();
                intent.putExtra("doingName",doing.getName());
                intent.putExtra("timeLong",doing.getTimeLong());
                intent.setClass(mContext,Starttask.class);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Doing doing = mDoingList.get(position);
        holder.doingName.setText(doing.getName());
        holder.timeLong.setText(String.valueOf(doing.getTimeLong()));
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        switch (DOING_BAC) {
            case 0:
                holder.doingName.setTextColor(Color.parseColor("grey"));
                holder.timeLong.setTextColor(Color.parseColor("grey"));
                holder.imageView.setImageResource(Constans.sightsDrawables[position % Constans.sightsDrawables.length]);
            break;
            case 1:
                holder.doingName.setTextColor(Color.WHITE);
                holder.timeLong.setTextColor(Color.WHITE);
                holder.imageView.setImageResource(Constans.wordDrawables[position % Constans.wordDrawables.length]);
            break;
            case 2:
                holder.imageView.setImageResource(Constans.yelloDrawables[position % Constans.yelloDrawables.length]);
            break;
            case 3:
                holder.imageView.setImageResource(Constans.alwaysDrawables[position % Constans.alwaysDrawables.length]);
                break;
            case 4:
                holder.imageView.setImageResource(Constans.Colors[position % Constans.Colors.length]);
            default:
                break;
        }


    }


    @Override
    public int getItemCount() {
        return mDoingList.size();
    }

    public void deleteData(int position) {
        DataSupport.deleteAll(Doing.class,"name = ?",mDoingList.get(position).getName());
        mDoingList.remove(position);
        notifyItemRemoved(position);
    }
}
