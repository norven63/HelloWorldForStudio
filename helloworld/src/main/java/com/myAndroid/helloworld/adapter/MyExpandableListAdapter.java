package com.myAndroid.helloworld.adapter;

import android.graphics.Typeface;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private String[][] contents;
    private String[] titles;

    public MyExpandableListAdapter(Context context, String[][] contents, String[] titles) {
        super();

        if (titles.length != contents.length) {
            throw new IllegalArgumentException("Check your argument's length!");
        }

        this.context = context;
        this.contents = contents;
        this.titles = titles;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return contents[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return contents[groupPosition].length;
    }

    /**
     * 获取每个条目的视图
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView rowTextView = (TextView) convertView;
        if (null == rowTextView) {
            rowTextView = new TextView(context);
            rowTextView.setPadding(15, 0, 0, 0);
        }

        rowTextView.setText(contents[groupPosition][childPosition]);

        return rowTextView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return contents[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return contents.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取每个分组条目的视图
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView rowTextView = (TextView) convertView;
        if (null == rowTextView) {
            rowTextView = new TextView(context);
        }

        rowTextView.setTypeface(Typeface.DEFAULT_BOLD);
        rowTextView.setText(titles[groupPosition]);

        return rowTextView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
