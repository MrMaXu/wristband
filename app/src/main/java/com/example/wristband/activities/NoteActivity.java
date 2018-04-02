package com.example.wristband.activities;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.wristband.R;
import com.example.wristband.adapter.NoteAdapter;
import com.example.wristband.bean.Doing;
import com.example.wristband.bean.Note;
import com.example.wristband.utils.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private List<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.noteToolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getData();
        mRecyclerView = findViewById(R.id.note_recycler);
        setNoteRecycler();

    }

    //初始化RecyclerView
    private void setNoteRecycler() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager (2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NoteAdapter(noteList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);//开启动画
        mAdapter.isFirstOnly(false);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.note_item_cardview, true);
        mAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

            }
        });

        // 开启滑动删除
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, final int pos) {
//                final AlertDialog.Builder delete = new AlertDialog.Builder(NoteActivity.this);
//                delete.setTitle("删除活动");
//                delete.setMessage("确定删除此活动？");
//                delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        deleteData(pos);//删除
//                    }
//                });
//                delete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                delete.show();
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    public void getData(){
        List<Note> notes = DataSupport.findAll(Note.class);
        noteList = notes;
    }

    //更新数据
    private void refresh() {
        noteList.clear();
        noteList.addAll(DataSupport.findAll(Note.class));
        mAdapter.notifyDataSetChanged();
    }

    //删除数据
    private void deleteData(int position) {
        DataSupport.deleteAll(Note.class,"content = ?",noteList.get(position).getContent());
        noteList.remove(position);
        refresh();
    }

    //添加活动
    private void addNote() {
        //新建Dialog添加活动
        final AlertDialog.Builder add = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.phone_add_dialog,null);
        add.setTitle("添加活动");
        add.setView(dialogView);
        add.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText addContent = (EditText) dialogView.findViewById(R.id.add_name);
                Note note = new Note();
                note.setContent(addContent.getText().toString());
                note.setTimeStamp(TimeUtil.getCurrentDate());
                note.save();
                //刷新视图
                refresh();
            }
        });
        add.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        add.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:                 //点击返回
                finish();
                break;
            case R.id.add_note:
//                Toast.makeText(this,"添加活动",Toast.LENGTH_SHORT).show();
                addNote();
                break;
                default:
                    break;
        }
        return true;
    }
}
