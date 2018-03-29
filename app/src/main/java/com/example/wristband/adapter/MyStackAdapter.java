package com.example.wristband.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wristband.R;
import com.example.wristband.bean.Doing;
import com.example.wristband.utils.Constans;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import org.litepal.crud.DataSupport;

public class MyStackAdapter extends StackAdapter<Doing> {


    public MyStackAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(Doing data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    public Doing getItem(int position) {
        return super.getItem(position);
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(final ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
        TextView textView = view.findViewById(R.id.text_list_card_title);
        return new ColorItemViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_card_item;
    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        TextView time;
        int position;


        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            time = (TextView) view.findViewById(R.id.time);

        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(final Doing data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), Constans.Colors[position]), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(data.getName());
            time.setText(String.valueOf(data.getTimeLong()));
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder delete = new AlertDialog.Builder(getContext());
                    delete.setTitle("删除活动");
                    delete.setMessage("确定删除此活动？");
                    delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        deleteData(data);
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
        }

        private void deleteData(Doing data) {

        }


    }
}
