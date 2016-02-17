package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerLayoutActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_layout);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(DrawerLayoutActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(DrawerLayoutActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                String state = "unknow";
                switch (newState) {
                case ViewDragHelper.STATE_DRAGGING:
                    state = "DRAGGING";//表示正在拖拽拖拉菜单

                    break;
                case ViewDragHelper.STATE_IDLE:
                    state = "IDLE";//表示已经触发完毕拖拉菜单

                    break;
                case ViewDragHelper.STATE_SETTLING:
                    state = "SETTLING";//表示刚刚触发拖拉菜单

                    break;
                default:
                    break;
                }

                Toast.makeText(DrawerLayoutActivity.this, "onDrawerStateChanged -state: " + state, Toast.LENGTH_SHORT)
                        .show();
                super.onDrawerStateChanged(newState);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //根据配置文件渲染菜单项
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        }

        return true;
    }
}
