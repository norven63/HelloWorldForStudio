package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

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
    private RecyclerView.LayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

    //线性布局 - 垂直
    private RecyclerView.LayoutManager linearLayoutManagerVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    //网格布局
    private RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

    //错列布局 - 2行，可以水平滑动
    private RecyclerView.LayoutManager staggeredGridLayoutManagerHorizontal = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);

    //错列布局 - 2列，可以垂直滑动
    private RecyclerView.LayoutManager staggeredGridLayoutManagerVertical = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

    private String[] mDataset = new String[20];

    {
        for (int i = 0; i < 20; i++) {
            mDataset[i] = "Item_" + i;
        }
    }

    private CustomAdapter mAdapter;

    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";

        private List<String> dataSource = Lists.newArrayList();

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final View itemView;

            private final TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.textView);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        public CustomAdapter(String[] dataSet) {
            for (String item : dataSet) {
                dataSource.add(item);
            }
        }

        /**
         * 渲染每个item视图的布局
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view, viewGroup, false);

            return new ViewHolder(v);
        }

        /**
         * 将业务逻辑绑定每个item视图
         */
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getTextView().setText(dataSource.get(position));

            //单击添加
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataSource.add(position, "add_" + position);
                    notifyItemInserted(position);
                }
            });

            //长按删除
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dataSource.remove(position);
                    notifyItemRemoved(position);

                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSource.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(linearLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置默认布局动画(当有元素新增、删除时的动画)
        mAdapter = new CustomAdapter(mDataset);
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
