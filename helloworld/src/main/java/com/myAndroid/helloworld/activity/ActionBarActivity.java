package com.myAndroid.helloworld.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

public class ActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        /**
         * 记住这里一定要用getSupportActionBar()，否则如果用传统的getActionBar()会返回null
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //根据配置文件渲染菜单项
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * 菜单项点击事件
         */
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(ActionBarActivity.this, "Home", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_refresh:
                Toast.makeText(ActionBarActivity.this, "bar：刷新", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_settings:
                Toast.makeText(ActionBarActivity.this, "bar：设置", Toast.LENGTH_SHORT).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
