package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerLayoutActivity extends Activity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_layout);
        ButterKnife.bind(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

//        getActionBar().setHomeButtonEnabled(true);
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
