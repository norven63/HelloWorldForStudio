package com.myAndroid.helloworld.customView.dragFresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.myAndroid.helloworld.R;


/**
 * 下拉刷新控件
 */
@SuppressLint("ClickableViewAccessibility")
public class DragToReFreshView extends LinearLayout {
    private final int LISTVIEW = 1;
    private final int GRIDVIEW = 2;
    private final int EXPANDABLELISTVIEW = 3;
    private final int NO_DIVIDER_RES_ID = 299992;
    private final int NO_DIVIDER_HEIGHT = 399993;
    private final int NO_HORIZONTAL_SPACING = 499994;
    private final int NO_VERTICAL_SPACING = 599995;

    private final float MOVE_SCALE = 0.5f;// 位移比例,用来调整下拉效果的体验度

    private boolean stopSubViewScroll = false;// 是否需要阻止子控件自身的滑动行为（拖拽时打开,手指放开时关闭）
    private boolean isRefreshing = false;// 是否正在执行刷新操作
    private boolean shouldRefresh = true;// 是否可以刷新
    private boolean isFirstLayout = true;// 是否为头一次渲染至屏幕之上
    private boolean canDrag;// 是否可以拖拽（子布局拉到最顶或者最底时打开）
    private float currentY;// 当前手指触屏的Y坐标

    private OnRefreshListener onTopDragRefreshListener;// 顶部拖拽刷新监听
    private OnRefreshListener onBottomDragRefreshListener;// 底部拖拽刷新监听
    private View headView;
    private View footView;
    private BaseAdapter adapter;
    private ExpandableListAdapter expandableListAdapter;
    private OnItemClickListener onItemClickListener;
    private OnTouchListener onTouchListener;
    private AbsListView contentListView;

    /**
     * 刷新操作监听接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * ListView
     */
    private class DragToFreshListView extends ListView {
        public DragToFreshListView(Context context) {
            super(context);

            this.setOnTouchListener(onTouchListener);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            // 判断是否需要阻止滑动
            if (stopSubViewScroll) {
                return false;
            }

            return super.onTouchEvent(ev);
        }

        @Override
        protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
            canDrag = clampedY;// 捕捉滑动到顶部或者底部的时机
            super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        }

        /**
         * 为防止子控件拦截DOWN事件，所以在onInterceptTouchEvent中来缓存手势点击的坐标以及headView、footView的Y坐标
         */
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                new MotionEventStrategyForDown().handleMotionEvent(this, ev);

                currentY = ev.getRawY();
            }

            return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * GridView
     */
    private class DragToFreshGridView extends GridView {
        public DragToFreshGridView(Context context) {
            super(context);

            this.setOnTouchListener(onTouchListener);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            // 判断是否需要阻止滑动
            if (stopSubViewScroll) {
                return false;
            }

            return super.onTouchEvent(ev);
        }

        @Override
        protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
            canDrag = clampedY;// 捕捉滑动到顶部或者底部的时机
            super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        }

        /**
         * 为防止子控件拦截DOWN事件，所以在onInterceptTouchEvent中来缓存手势点击的坐标以及headView、footView的Y坐标
         */
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                new MotionEventStrategyForDown().handleMotionEvent(this, ev);

                currentY = ev.getRawY();
            }

            return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * ExpandableListView
     */
    private class DragToFreshExpandableListView extends android.widget.ExpandableListView {
        public DragToFreshExpandableListView(Context context) {
            super(context);

            this.setOnTouchListener(onTouchListener);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            // 判断是否需要阻止滑动
            if (stopSubViewScroll) {
                return false;
            }

            return super.onTouchEvent(ev);
        }

        @Override
        protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
            canDrag = clampedY;// 捕捉滑动到顶部或者底部的时机
            super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        }

        /**
         * 为防止子控件拦截DOWN事件，所以在onInterceptTouchEvent中来缓存手势点击的坐标以及headView、footView的Y坐标
         */
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                new MotionEventStrategyForDown().handleMotionEvent(this, ev);

                currentY = ev.getRawY();
            }

            return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * MotionEvent策略接口
     */
    private interface MotionEventStrategy {
        void handleMotionEvent(View contentListView, MotionEvent event);
    }

    /**
     * MotionEvent按下事件策略类
     */
    private class MotionEventStrategyForDown implements MotionEventStrategy {
        @Override
        public void handleMotionEvent(View contentListView, MotionEvent event) {
            if (null != headView && null == headView.getTag(R.id.firstY)) {
                headView.setTag(R.id.firstY, headView.getY());
            }

            if (null != footView && null == footView.getTag(R.id.firstY)) {
                footView.setTag(R.id.firstY, footView.getY());
            }

            currentY = event.getRawY();
        }
    }

    /**
     * MotionEvent抬起事件策略类
     */
    private class MotionEventStrategyForUp implements MotionEventStrategy {
        @Override
        public void handleMotionEvent(View contentListView, MotionEvent event) {
            // 刷新操作========start=========
            boolean hasHeadViewMoved = false;// 标记头图标是否向下移动了足够的距离
            if (headView != null && headView.getTag(R.id.firstY) != null) {
                float totalDistance = headView.getY() - ((Float) headView.getTag(R.id.firstY));
                if (totalDistance >= headView.getHeight()) {
                    hasHeadViewMoved = true;
                }
            }

            boolean hasFootViewMoved = false;// 标记尾图标是否向上移动了足够的距离
            if (footView != null && footView.getTag(R.id.firstY) != null) {
                float totalDistance = ((Float) footView.getTag(R.id.firstY)) - footView.getY();
                if (totalDistance >= footView.getHeight()) {
                    hasFootViewMoved = true;
                }
            }

            if (!isRefreshing && shouldRefresh && canDrag && (hasHeadViewMoved || hasFootViewMoved)) {
                isRefreshing = true;// 标记正在执行刷新操作

                if (hasHeadViewMoved) {
                    if (onTopDragRefreshListener != null) {
                        onTopDragRefreshListener.onRefresh();
                    } else {
                        taskFinished();
                    }
                } else if (hasFootViewMoved) {
                    if (onBottomDragRefreshListener != null) {
                        onBottomDragRefreshListener.onRefresh();
                    } else {
                        taskFinished();
                    }
                }
            } else if (canDrag || !shouldRefresh) {
                taskFinished();
            }
            // 刷新操作========end=========
        }
    }

    /**
     * MotionEvent移动事件策略类
     */
    private class MotionEventStrategyForMove implements MotionEventStrategy {
        @Override
        public void handleMotionEvent(View contentListView, MotionEvent event) {
            // 计算位移量
            float distanceY = (event.getRawY() - currentY) * MOVE_SCALE;
            currentY = event.getRawY();

			/*
             * 如果正在执行刷新操作，或者仍未将子布局拉到最顶或者最底，则直接退出不执行位移操作
			 */
            if (isRefreshing || !canDrag) {
                return;
            }

			/*
             * 只有拖拽到了ListView的极限位置(最顶或最底位置 )才允许执行位移
			 */
            if (canDrag) {
                // 阻止子控件继续滑动
                stopSubViewScroll = true;

				/*
                 * 执行位移
				 */
                contentListView.setTranslationY(contentListView.getTranslationY() + distanceY);
                if (null != headView) {
                    headView.setY(headView.getY() + distanceY);
                }
                if (null != footView) {
                    footView.setY(footView.getY() + distanceY);
                }
            }
        }
    }

    public DragToReFreshView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(LinearLayout.VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragToReFreshLayout);
        final int type = typedArray.getInt(R.styleable.DragToReFreshLayout_type, LISTVIEW);
        final int numColumns = typedArray.getInt(R.styleable.DragToReFreshLayout_column, LISTVIEW);
        final int dividerId = typedArray.getResourceId(R.styleable.DragToReFreshLayout_divider, NO_DIVIDER_RES_ID);
        final float dividerHeight = typedArray.getDimension(R.styleable.DragToReFreshLayout_dividerHeight, NO_DIVIDER_HEIGHT);
        final float horizontalSpacing = typedArray.getDimension(R.styleable.DragToReFreshLayout_horizontalSpacing, NO_HORIZONTAL_SPACING);
        final float verticalSpacing = typedArray.getDimension(R.styleable.DragToReFreshLayout_verticalSpacing, NO_VERTICAL_SPACING);
        typedArray.recycle();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirstLayout) {
                    isFirstLayout = false;// 防止循环加载

                    headView = findViewById(R.id.dragToReFresh_headView);
                    footView = findViewById(R.id.dragToReFresh_footView);

                    if (null != headView) {
                        // 向上隐藏headView
                        LayoutParams params = (LayoutParams) headView.getLayoutParams();
                        params.topMargin = -headView.getHeight();
                        headView.setLayoutParams(params);
                    }

                    if (null != footView) {
                        // 临时移除footView，下面的数据加载会影响到布局的高度，在数据填充完之后再将footView加回来
                        removeView(footView);
                    }

                    // 根据type属性的值来判断哪一种控件加载数据
                    if (type == LISTVIEW) {
                        contentListView = new DragToFreshListView(getContext());

                        if (dividerId != NO_DIVIDER_RES_ID) {
                            ((DragToFreshListView) contentListView).setDivider(getResources().getDrawable(dividerId));
                        } else {
                            ((DragToFreshListView) contentListView).setDivider(null);
                        }

                        if (dividerHeight != NO_DIVIDER_HEIGHT) {
                            ((DragToFreshListView) contentListView).setDividerHeight((int) (dividerHeight));
                        }

                        if (adapter != null) {
                            contentListView.setAdapter(adapter);
                        }
                    } else if (type == GRIDVIEW) {
                        contentListView = new DragToFreshGridView(getContext());

                        ((DragToFreshGridView) contentListView).setNumColumns(numColumns);

                        if (horizontalSpacing != NO_HORIZONTAL_SPACING) {
                            ((DragToFreshGridView) contentListView).setHorizontalSpacing((int) horizontalSpacing);
                        }

                        if (verticalSpacing != NO_VERTICAL_SPACING) {
                            ((DragToFreshGridView) contentListView).setVerticalSpacing((int) verticalSpacing);
                        }

                        if (adapter != null) {
                            contentListView.setAdapter(adapter);
                        }
                    } else if (type == EXPANDABLELISTVIEW) {
                        contentListView = new DragToFreshExpandableListView(getContext());

                        contentListView.setSelector(R.color.listSelector);
                        ((DragToFreshExpandableListView) contentListView).setGroupIndicator(null);

                        if (dividerId != NO_DIVIDER_RES_ID) {
                            ((DragToFreshExpandableListView) contentListView).setDivider(getResources().getDrawable(dividerId));
                            ((DragToFreshExpandableListView) contentListView).setChildDivider(getResources().getDrawable(dividerId));
                        } else {
                            ((DragToFreshExpandableListView) contentListView).setDivider(null);
                            ((DragToFreshExpandableListView) contentListView).setChildDivider(null);
                        }

                        if (expandableListAdapter != null) {
                            ((DragToFreshExpandableListView) contentListView).setAdapter(expandableListAdapter);
                        }
                    }

                    addView(contentListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                    if (onItemClickListener != null) {
                        contentListView.setOnItemClickListener(onItemClickListener);
                    }

                    contentListView.setVerticalScrollBarEnabled(false);

                    if (null != footView) {
                        // 将footView加回来
                        addView(footView);
                    }
                }
            }
        });

        onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(final View contentListView, final MotionEvent event) {
                MotionEventStrategy strategy = createStrategyWithMotionEvent(event);
                if (strategy != null) {
                    strategy.handleMotionEvent(contentListView, event);
                }

                return false;
            }
        };
    }

    /**
     * 根据MotionEvent构建对应的事件策略类
     */
    private MotionEventStrategy createStrategyWithMotionEvent(MotionEvent event) {
        MotionEventStrategy motionEventStrategy = null;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                motionEventStrategy = new MotionEventStrategyForMove();

                break;
            case MotionEvent.ACTION_UP:
                motionEventStrategy = new MotionEventStrategyForUp();

                break;
            default:
                break;
        }

        return motionEventStrategy;
    }

    public void setOnTopDragRefreshListener(OnRefreshListener onTopDragRefreshListener) {
        this.onTopDragRefreshListener = onTopDragRefreshListener;
    }

    public void setOnBottomDragRefreshListener(OnRefreshListener onBottomDragRefreshListener) {
        this.onBottomDragRefreshListener = onBottomDragRefreshListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void setExpandableListAdapter(ExpandableListAdapter expandableListAdapter) {
        this.expandableListAdapter = expandableListAdapter;
    }

    /**
     * 显示headView和footView
     */
    public void showHeadAndFootView() {
        if (null != headView) {
            headView.setVisibility(View.VISIBLE);
        }

        if (null != footView) {
            footView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏headView和footView
     */
    public void hideHeadAndFootView() {
        if (null != headView) {
            headView.setVisibility(View.INVISIBLE);
        }

        if (null != footView) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 关闭刷新功能
     */
    public void closeRefresh() {
        shouldRefresh = false;
    }

    /**
     * 打开刷新功能
     */
    public void openRefresh() {
        shouldRefresh = true;
    }

    /**
     * 任务完成
     */
    public void taskFinished() {
        if (contentListView == null) {
            return;
        }

		/*
		 * 复位相关动画
		 */
        if (null != headView && null != headView.getTag(R.id.firstY)) {
            headView.animate().setInterpolator(new LinearInterpolator()).y((Float) headView.getTag(R.id.firstY));
        }

        if (null != footView && null != footView.getTag(R.id.firstY)) {
            footView.animate().setInterpolator(new LinearInterpolator()).y((Float) footView.getTag(R.id.firstY));
        }

        contentListView.animate().setInterpolator(new LinearInterpolator()).y(0f);

		/*
		 * 重置各个标记位
		 */
        canDrag = false;
        isRefreshing = false;
        stopSubViewScroll = false;
    }
}