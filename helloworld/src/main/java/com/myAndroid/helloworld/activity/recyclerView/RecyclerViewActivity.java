package com.myAndroid.helloworld.activity.recyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RadioButton;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

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
        recyclerView.setLayoutManager(linearLayoutManagerVertical);

        recyclerView.addItemDecoration(new MyItemDecoration(this));//设置分割符

        //设置默认布局动画(此处运用了第三方库)
        mAdapter = new MyRecyclerAdapter(mDataset);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mAdapter);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(scaleInAnimationAdapter);

        recyclerView.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);

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
