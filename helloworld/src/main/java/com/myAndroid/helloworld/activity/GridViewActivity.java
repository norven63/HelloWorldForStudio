package com.myAndroid.helloworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.adapter.MyBaseAdapter;

public class GridViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_view);

		List<String> data = new ArrayList<String>();
		data.add("textA");
		data.add("textB");
		data.add("textC");
		data.add("textD");
		data.add("textE");
		data.add("textF");

		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new MyBaseAdapter<String>(data) {
			class ViewHolder {
				TextView textView;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder;
				if (convertView == null) {
					convertView = LayoutInflater.from(GridViewActivity.this).inflate(R.layout.item_gridview, null);
					holder = new ViewHolder();
					holder.textView = (TextView) convertView.findViewById(R.id.text);

					convertView.setTag(holder);

					// 设置item宽高
					// int width = (int) getResources().getDimension(R.dimen.kunlun_add_friend_item_width);
					// int height = (int)
					// getResources().getDimension(R.dimen.kunlun_add_friend_rank_item_height);
					// convertView.setLayoutParams(new LayoutParams(width, height));
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.textView.setText(getItem(position));

				return convertView;
			}
		});
	}
}
