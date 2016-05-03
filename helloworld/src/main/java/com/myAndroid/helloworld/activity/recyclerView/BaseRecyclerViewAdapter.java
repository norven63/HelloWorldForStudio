package com.myAndroid.helloworld.activity.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * @author ZhouXinXing
 * @date 2016年03月03日 15:15
 * @Description
 */
public abstract class BaseRecyclerViewAdapter<ItemClass, ViewHolder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<ViewHolder> {
    public interface OnItemClickListener<ItemClass> {
        void onItemClick(int itemType, View itemView, ItemClass clickedItem, int postion);//注意：postion的值请务必使用holder.getAdapterPosition()来传递
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