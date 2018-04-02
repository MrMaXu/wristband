package com.example.wristband.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wristband.R;
import com.example.wristband.bean.Note;

import java.util.List;

/**
 * Created by 刘青林 on 2018/4/2.
 */

public class NoteAdapter extends BaseItemDraggableAdapter<Note,BaseViewHolder> {

    public NoteAdapter (List data) {
        super(R.layout.note_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Note item) {
        helper.setText(R.id.note_content,item.getContent().toString());
        helper.setText(R.id.note_time,item.getTimeStamp().toString());
    }

}
