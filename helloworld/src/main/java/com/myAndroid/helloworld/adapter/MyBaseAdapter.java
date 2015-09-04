package com.myAndroid.helloworld.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	private List<T> dataSource;

	public MyBaseAdapter(List<T> dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public final int getCount() {
		return dataSource != null ? dataSource.size() : 0;
	}

	@Override
	public final T getItem(int position) {
		return dataSource != null && getCount() > position ? dataSource.get(position) : null;
	}

	@Override
	public final long getItemId(int position) {
		return position;
	}

	public final List<T> getDataSource() {
		return dataSource;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
