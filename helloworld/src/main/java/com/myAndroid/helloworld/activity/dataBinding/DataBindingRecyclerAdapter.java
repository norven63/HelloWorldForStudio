package com.myAndroid.helloworld.activity.dataBinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.myAndroid.helloworld.BR;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.databinding.ItemDataBindRecyclerViewBinding;

import java.util.Arrays;
import java.util.List;

public class DataBindingRecyclerAdapter extends RecyclerView.Adapter<DataBindingRecyclerAdapter.ViewHolder> {
    private List<String> dataSource = Lists.newArrayList();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDataBindRecyclerViewBinding binding;

        public ViewHolder(View itemView, ItemDataBindRecyclerViewBinding binding) {
            super(itemView);

            this.binding = binding;
        }
    }

    public DataBindingRecyclerAdapter(String[] dataSet) {
        dataSource.addAll(Arrays.asList(dataSet));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ItemDataBindRecyclerViewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_data_bind_recycler_view,
                viewGroup,
                false);

        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String item = dataSource.get(position);
        viewHolder.binding.setItem(item);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}