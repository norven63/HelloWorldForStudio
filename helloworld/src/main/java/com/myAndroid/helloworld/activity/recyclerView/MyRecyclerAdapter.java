package com.myAndroid.helloworld.activity.recyclerView;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.myAndroid.helloworld.R;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    public static final int TYPE_HEADER = 0;//表头item类型
    public static final int TYPE_NORMAL = 1;//普通item类型

    private List<String> dataSource = Lists.newArrayList();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;

        private final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            textView = (TextView) itemView.findViewById(R.id.textViewRecyclerItem);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public MyRecyclerAdapter(String[] dataSet) {
        for (String item : dataSet) {
            dataSource.add(item);
        }
    }

    /**
     * 重新该方法，将会影响onCreateViewHolder方法的viewType入参
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }

        return TYPE_NORMAL;
    }

    /**
     * 渲染每个item视图的布局
     * 注：此处的viewType入参将会受getItemViewType方法的影响
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view, viewGroup, false);

        //TODO 这里的业务逻辑可以考虑挪到onBindViewHolder方法中。如果需要用到viewType作为判断阈值，则可以将viewType设置为view的Tag属性来携带
        switch (viewType) {
            case TYPE_HEADER:
                ((TextView) itemView.findViewById(R.id.textViewRecyclerItem)).setText("> 表头 <");
                break;
            default:
                break;
        }

        return new ViewHolder(itemView);
    }

    /**
     * 将业务逻辑绑定每个item视图
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (!viewHolder.getTextView().getText().equals("> 表头 <")) {
            viewHolder.getTextView().setText(dataSource.get(position));
        }

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

    /**
     * 注意该方法的调用时机，可在其他需求下灵活运用。但是需要注意，该方法只会在渲染时回调一次，若使用RecyclerView.setLayoutManager()方法是不会回掉该方法的
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

            /**
             * 设置Grid布局下，item的横向跨度
             */
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //如果是表头，则占满一行
                    return getItemViewType(position) == TYPE_HEADER ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 为了拿到StaggeredGridLayoutManager需要的属性，重写了该方法
     */
    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams && getItemViewType(holder.getLayoutPosition()) == TYPE_HEADER) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}