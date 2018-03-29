package com.example.wristband;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wristband.adapter.MyStackAdapter;
import com.example.wristband.bean.Doing;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;

import org.litepal.crud.DataSupport;

import java.util.Arrays;
import java.util.List;

public class StackTestActivity extends AppCompatActivity implements CardStackView.ItemExpendListener {

    private CardStackView mStackView;
    private MyStackAdapter myStackAdapter;
    private List<Doing> doingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_test);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.phoneToolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        FloatingActionButton phoneFab = (FloatingActionButton) findViewById(R.id.phone_fab);
        phoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addDoing();
            }
        });

        mStackView = (CardStackView) findViewById(R.id.card_stack);
        mStackView.setItemExpendListener(this);
        //设置动画
        mStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(mStackView));
        myStackAdapter = new MyStackAdapter(this);
        mStackView.setAdapter(myStackAdapter);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        myStackAdapter.updateData(doingList);
                    }
                }
                , 200
        );
    }

    //监听item打开的动作
    @Override
    public void onItemExpend(boolean expend) {
//        Toast.makeText(StackTestActivity.this,"item打开",Toast.LENGTH_SHORT).show();
    }

    //从数据库中获得数据
    private void getData() {
        List<Doing> doings = DataSupport.findAll(Doing.class);
        doingList = doings;
        System.out.println(doingList);
    }
}
