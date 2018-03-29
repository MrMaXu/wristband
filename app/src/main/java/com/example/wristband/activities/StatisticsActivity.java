package com.example.wristband.activities;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wristband.R;
import com.example.wristband.charts.BubblePlaceholderFragment;
import com.example.wristband.charts.ColumnPlaceholderFragment;
import com.example.wristband.charts.LinePlaceholderFragment;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //默认加载线型
        getSupportFragmentManager().beginTransaction().add(R.id.container, new com.example.wristband.charts.LinePlaceholderFragment()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container2, new com.example.wristband.charts.ColumnPlaceholderFragment()).commit();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.sta_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart_style,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:             //点击返回
                finish();
                break;
            case R.id.line_chart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new LinePlaceholderFragment()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,new LinePlaceholderFragment()).commit();
                break;
            case R.id.bubble_chart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new BubblePlaceholderFragment()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, new BubblePlaceholderFragment()).commit();
                break;
            case R.id.column_chart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ColumnPlaceholderFragment()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, new ColumnPlaceholderFragment()).commit();
            default:
                break;
        }
        return true;
    }
}
