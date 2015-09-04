package com.myAndroid.helloworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groups;
    private List<String> itemsA;
    private List<String> itemsB;
    private List<String> itemsC;

    public MyExpandableListAdapter(Context context, List<String> titles, List<String> groupA, List<String> groupB, List<String> groupC) {
        this.context = context;
        this.groups = titles;
        this.itemsA = groupA;
        this.itemsB = groupB;
        this.itemsC = groupC;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        String value = "";

        switch (groupPosition) {
            case 0:
                value = itemsA.get(childPosition);
                break;
            case 1:
                value = itemsB.get(childPosition);
                break;
            case 2:
                value = itemsC.get(childPosition);
                break;
            default:
                break;
        }

        return value;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;

        switch (groupPosition) {
            case 0:
                count = itemsA == null ? 0 : itemsA.size();

                break;
            case 1:
                count = itemsB == null ? 0 : itemsB.size();

                break;
            case 2:
                count = itemsC == null ? 0 : itemsC.size();

                break;
            default:
                break;
        }

        return count;
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

        rowTextView.setText(getChild(groupPosition, childPosition));

        return rowTextView;
    }

    @Override
    public String getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups == null ? 0 : groups.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_group_view, null);
        TextView textView = (TextView) convertView.findViewById(R.id.group_name);
        textView.setText(getGroup(groupPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
