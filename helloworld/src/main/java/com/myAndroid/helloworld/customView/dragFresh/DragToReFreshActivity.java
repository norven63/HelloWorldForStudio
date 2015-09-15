package com.myAndroid.helloworld.customView.dragFresh;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.adapter.MyExpandableListAdapter;
import com.myAndroid.helloworld.customView.dragFresh.DragToReFreshView.OnRefreshListener;

public class DragToReFreshActivity extends Activity {
	private DragToReFreshView dragToFreshLayout;

	public class MyBaseAdapter extends BaseAdapter {
		private List<TextView> dateSource = Lists.newArrayList();

		public MyBaseAdapter() {
			super();

			for (int i = 0; i < 9 * 3; i++) {
				TextView textView = (TextView) getLayoutInflater().inflate(R.layout.drag_to_refresh_item, null);
				dateSource.add(textView);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getItem(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getItem(int position) {
			return dateSource.get(position);
		}

		@Override
		public int getCount() {
			return dateSource.size();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_drag_to_refresh_layout);

		BaseAdapter adapter = new MyBaseAdapter();

		List<String> titles = Arrays.asList("动物", "水果", "英雄");
		List<String> itemsA = Arrays.asList("老鼠", "豹子", "老陈");
		List<String> itemsB = Arrays.asList("苹果", "香蕉");
		List<String> itemsC = Arrays.asList("盖伦", "斧王", "翟江");

		MyExpandableListAdapter expandableListAdapter = new MyExpandableListAdapter(this, titles, itemsA, itemsB, itemsC);

		dragToFreshLayout = (DragToReFreshView) findViewById(R.id.dragToFreshListView);
//		dragToFreshLayout.setAdapter(adapter);
		dragToFreshLayout.setExpandableListAdapter(expandableListAdapter);

		/**
		 * 下拉刷新监听
		 */
		dragToFreshLayout.setOnTopDragRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				Toast.makeText(DragToReFreshActivity.this, "下拉刷新!", Toast.LENGTH_SHORT).show();
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					@Override
					public void run() {
						dragToFreshLayout.taskFinished();// 标记任务完成(一般在异步任务回调中执行此接口)
					}
				}, 1500);

			}
		});

		/**
		 * 上拉刷新监听
		 */
		dragToFreshLayout.setOnBottomDragRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				Toast.makeText(DragToReFreshActivity.this, "上拉刷新!", Toast.LENGTH_SHORT).show();
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					@Override
					public void run() {
						dragToFreshLayout.taskFinished();// 标记任务完成(一般在异步任务回调中执行此接口)
					}
				}, 1500);
			}
		});

		/**
		 * item点击事件监听
		 */
		dragToFreshLayout.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(DragToReFreshActivity.this, position + "号位被点击,点击内容为:" + ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
