package com.myAndroid.helloworld.activity.recyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.myAndroid.helloworld.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends Activity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.radioLinerHorizontal)
    RadioButton radioLinerHorizontal;
    @Bind(R.id.radioLinerVertical)
    RadioButton radioLinerVertical;
    @Bind(R.id.radioGrid)
    RadioButton radioGrid;
    @Bind(R.id.radioStaggeredGridHorizontal)
    RadioButton radioStaggeredGridHorizontal;
    @Bind(R.id.radioStaggeredGridVertical)
    RadioButton radioStaggeredGridVertical;

    //线性布局 - 水平
    private LinearLayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

    //线性布局 - 垂直
    private LinearLayoutManager linearLayoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    //网格布局
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

    //错列布局 - 2行，可以水平滑动
    private StaggeredGridLayoutManager staggeredGridLayoutManagerHorizontal = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);

    //错列布局 - 2列，可以垂直滑动
    private StaggeredGridLayoutManager staggeredGridLayoutManagerVertical = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

    private String[] mDataset = new String[20];

    {
        for (int i = 0; i < 20; i++) {
            mDataset[i] = "Item_" + i;
        }
    }

    private MyRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置默认布局动画(当有元素新增、删除时的动画)
        recyclerView.addItemDecoration(new MyItemDecoration(this));//设置分割符
        mAdapter = new MyRecyclerAdapter(mDataset);
        recyclerView.setAdapter(mAdapter);

        radioLinerHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setLayoutManager(linearLayoutManagerHorizontal);
            }
        });

        radioLinerVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setLayoutManager(linearLayoutManagerVertical);
            }
        });

        radioGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        });

        radioStaggeredGridHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setLayoutManager(staggeredGridLayoutManagerHorizontal);
            }
        });

        radioStaggeredGridVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setLayoutManager(staggeredGridLayoutManagerVertical);
            }
        });
    }
}
