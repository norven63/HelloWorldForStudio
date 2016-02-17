package com.myAndroid.helloworld.activity;

import java.util.Arrays;
import java.util.List;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.adapter.MyExpandableListAdapter;

public class DataListActivity extends AppCompatActivity {
    private ListView headListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datalist);

        headListView = (ListView) findViewById(R.id.listHead);

        //        // 设置Header
        //        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //        final View headView = layoutInflater.inflate(R.layout.list_head_layout, null);
        //        headListView.addHeaderView(headView);

        //设置空试图，当ListView没有数据时，显示该视图
        headListView.setEmptyView(findViewById(R.id.empty_view));

        // 设置Adapter
        String[] DATALIST = { "aaa", "bbb", "ccc", "ddd" };
        headListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DATALIST));

        /**
         * 设置多选模式。注意：当所有item的check状态都为false，会自动关闭action bar
         */
        headListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        headListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Toast.makeText(DataListActivity.this, "position: " + position + "\nchecked: " + checked,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Toast.makeText(DataListActivity.this, "onCreateActionMode", Toast.LENGTH_SHORT).show();
                mode.getMenuInflater().inflate(R.menu.action_bar_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                Toast.makeText(DataListActivity.this, "打开溢出菜单", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                /*
                 * 菜单项点击事件
                 */
                switch (item.getItemId()) {
                case android.R.id.home:
                    Toast.makeText(DataListActivity.this, "Home", Toast.LENGTH_SHORT).show();

                    finish();
                    break;
                case R.id.menu_refresh:
                    Toast.makeText(DataListActivity.this, "bar：刷新", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.menu_settings:
                    Toast.makeText(DataListActivity.this, "bar：设置", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.menu_opt_a:
                    Toast.makeText(DataListActivity.this, "bar：opt_a", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.menu_opt_b:
                    Toast.makeText(DataListActivity.this, "bar：opt_b", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        /**
         * 可展开的ListView
         */
        List<String> titles = Arrays.asList("动物", "水果", "英雄");
        List<String> itemsA = Arrays.asList("老鼠", "豹子", "老陈");
        List<String> itemsB = Arrays.asList("苹果", "香蕉");
        List<String> itemsC = Arrays.asList("盖伦", "斧王", "翟江");

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, titles, itemsA, itemsB, itemsC);

        final ExpandableListView exListView = (ExpandableListView) findViewById(R.id.exListView);

        //去除item点击时，变换背景色的机制;也可以通过在xml布局文件中，设置listSelector:"#00000000"属性来实现
        exListView.setSelector(R.color.listSelector);

        //去除Group的指针图标;也可以通过在xml布局文件中，设置groupIndicator="@null"来实现
        exListView.setGroupIndicator(null);

        //展开某一个组项。注意：需要在加载完视图后调用，否则会报空指针异常
        exListView.getViewTreeObserver()
                .addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
                    boolean isFirst = true;

                    @Override
                    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                        if (isFirst) {
                            exListView.expandGroup(1,true);
                            isFirst = false;
                        }
                    }
                });

        exListView.setAdapter(adapter);
    }

    // 新增一个碎片,并覆盖当前页面.
    public void addF(View view) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        Fragment1 df = new Fragment1();
        ft.replace(R.id.popFragment, df);

        ft.addToBackStack(null);// 可以被BACK键返回;或者被popBackStack()弹出
        ft.commit();
    }

    public void popF(View view) {
        getFragmentManager().popBackStack();
    }
}
