package com.myAndroid.helloworld.activity.recyclerView;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author ZhouXinXing
 * @date 2016年03月03日 15:15
 * @Description
 */
public abstract class BaseRecyclerViewAdapter<ItemClass, ViewHolder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<ViewHolder> {
    public interface OnItemClickListener<ClickedItem> {
        void onItemClick(int itemType, View itemView, ClickedItem clickedItem, int postion);
    }

    protected List<ItemClass> dataSource;
    protected OnItemClickListener<ItemClass> onItemClickListener;

    public BaseRecyclerViewAdapter(List<ItemClass> dataSource) {
        this.dataSource = dataSource;
    }

    public final void setOnItemClickListener(OnItemClickListener<ItemClass> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return dataSource == null ? 0 : dataSource.size();
    }

    protected ItemClass getItem(int position) {
        return dataSource.get(position);
    }
}