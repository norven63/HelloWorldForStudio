package com.myAndroid.helloworld.activity.recyclerView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ZhouXinXing
 * @date 2016年03月09日 17:15
 * @Description
 */
public abstract class BaseGroupRecyclerAdapter<GroupItem, ChildItem, GroupViewHolder extends BaseGroupRecyclerAdapter.BaseGroupViewHolder, ChildViewHolder extends BaseGroupRecyclerAdapter.BaseChildViewHolder>
        extends RecyclerView.Adapter<BaseGroupRecyclerAdapter.BaseGroupRecyclerViewHolder> {
    /**
     * 条目类型枚举
     */
    public static enum ItemType {
        //组item
        GROUP(1),
        //子item
        CHILD(2);

        private final int type;

        ItemType(int type) {
            this.type = type;
        }
    }

    /**
     * 条目点击事件监听回调
     */
    public interface OnItemClickListener<GroupItem, ChildItem> {
        void onGroupItemClick(View itemView, GroupItem clickedItem, int postion);

        void onChildItemClick(View itemView, ChildItem clickedItem, int groupPostion, int postion);
    }

    public static abstract class BaseGroupRecyclerViewHolder extends RecyclerView.ViewHolder {
        public BaseGroupRecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static abstract class BaseGroupViewHolder extends BaseGroupRecyclerViewHolder {
        public BaseGroupViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static abstract class BaseChildViewHolder extends BaseGroupRecyclerViewHolder {
        public BaseChildViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected List<GroupItem> groupDataSource;
    protected List<List<ChildItem>> childDataSource;
    protected OnItemClickListener<GroupItem, ChildItem> onItemClickListener;

    private List<Integer> groupItemIndexList = new ArrayList<Integer>();//缓存组item的总索引

    public BaseGroupRecyclerAdapter(List<GroupItem> groupDataSource, List<List<ChildItem>> childDataSource) {
        if (groupDataSource == null) {
            throw new IllegalArgumentException("groupDataSource为空");
        }

        if (childDataSource == null) {
            throw new IllegalArgumentException("childDataSource为空");
        }

        if (groupDataSource.size() != childDataSource.size()) {
            throw new IllegalArgumentException("传入的资源集合长度不相同");
        }

        this.groupDataSource = groupDataSource;
        this.childDataSource = childDataSource;

        calculateGroupIndexs();
    }

    /**
     * 计算出并缓存属于组item目录的下标
     */
    private void calculateGroupIndexs() {
        groupItemIndexList.clear();

        for (int i = 0; i < groupDataSource.size(); i++) {
            /*
             * 先把0下标保存起来
             */
            if (i == 0) {
                groupItemIndexList.add(0);

                continue;
            }

            /*
             * 非0的组item下标计算思路：通过累加之前所有组item对应的子item数量，再加上之前所有组item个数，来计算当前组item的下标
             */
            int j = i;
            int childCountTemp = 0;
            while (--j >= 0) {
                childCountTemp += childDataSource.get(j).size();
            }

            groupItemIndexList.add(childCountTemp + i);
        }
    }

    public final void setOnItemClickListener(OnItemClickListener<GroupItem, ChildItem> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public final int getItemViewType(int position) {
        if (groupItemIndexList.contains(position)) {
            return ItemType.GROUP.type;
        } else {
            return ItemType.CHILD.type;
        }
    }

    @Override
    public final BaseGroupRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ItemType.GROUP.type) {
            return onCreateGroupViewHolder(viewGroup);
        } else {
            return onCreateChildViewHolder(viewGroup);
        }
    }

    @Override
    public final void onBindViewHolder(final BaseGroupRecyclerViewHolder viewHolder, final int position) {
        if (viewHolder instanceof BaseGroupViewHolder) {
            onBindGroupViewHolder((GroupViewHolder) viewHolder, position, groupItemIndexList.indexOf(position));
        } else if (viewHolder instanceof BaseChildViewHolder) {
            int[] postions = getPositions(position);

            onBindChildViewHolder((ChildViewHolder) viewHolder, position, postions[0], postions[1]);
        }
    }

    /**
     * 根据<总索引>，计算出<组item>于所在集合中的索引、<子item>于所在集合中的索引
     *
     * @param position
     *            总索引
     * @return
     */
    private int[] getPositions(final int position) {
        int[] postions = new int[] { childDataSource.size() - 1, 0 };

        int indexTemp = getItemCount();
        for (int i = 0; i < groupItemIndexList.size(); i++) {
            if (groupItemIndexList.get(i) > position) {
                indexTemp = groupItemIndexList.get(i);//获取第一个大于当前总索引的组item总索引

                postions[0] = i - 1;

                break;
            }
        }

        postions[1] = childDataSource.get(postions[0]).size() - (indexTemp - position);// 子item所属集合的size - （第一个大于 总索引 的 组item索引 - 总索引）

        return postions;
    }

    @Override
    public final int getItemCount() {
        return getGroupCount() + getChildCount();
    }

    public int getGroupCount() {
        return groupDataSource == null ? 0 : groupDataSource.size();
    }

    public int getChildCount() {
        int childCount = 0;

        if (childDataSource == null || childDataSource.isEmpty()) {
            return childCount;
        }

        for (List<ChildItem> childList : childDataSource) {
            if (childList != null && !childList.isEmpty()) {
                childCount += childList.size();
            }
        }

        return childCount;
    }

    protected GroupItem getGroupItem(int groupPosition) {
        return groupDataSource.get(groupPosition);
    }

    protected ChildItem getChildItem(int groupPosition, int childPosition) {
        return childDataSource.get(groupPosition).get(childPosition);
    }

    protected abstract GroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup);

    protected abstract ChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup);

    /**
     * 绑定组item视图
     *
     * @param viewHolder
     * @param position
     *            总索引
     * @param groupPosition
     *            组item于所在集合的索引
     */
    protected abstract void onBindGroupViewHolder(final GroupViewHolder viewHolder, final int position,
            final int groupPosition);

    /**
     * 绑定子item视图
     *
     * @param viewHolder
     * @param position
     *            总索引
     * @param groupPosition
     *            组item于所在集合的索引
     * @param childPosition
     *            子item于所在集合的索引
     */
    protected abstract void onBindChildViewHolder(final ChildViewHolder viewHolder, final int position,
            final int groupPosition, final int childPosition);
}