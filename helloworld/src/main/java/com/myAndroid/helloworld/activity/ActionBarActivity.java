package com.myAndroid.helloworld.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

import java.lang.reflect.Method;

public class ActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);

        /**
         * 记住这里一定要用getSupportActionBar()，否则如果用传统的getActionBar()会返回null
         */
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);//设置是否需要显示返回键
        actionBar.setTitle("主标题");
        actionBar.setSubtitle("副标题");

        /**
         * 显示自定义布局，替代标题
         */
        actionBar.setDisplayShowCustomEnabled(true);//显示自定义视图
        actionBar.setDisplayShowTitleEnabled(false);//关闭标题栏
        TextView textView = new TextView(this);
        textView.setText("@@@@@@@@");
        textView.setTextColor(0xffffff);
        actionBar.setCustomView(textView);
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
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();

                finish();
                break;
            case R.id.menu_refresh:
                Toast.makeText(this, "bar：刷新", Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_settings:
                Toast.makeText(this, "bar：设置", Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_opt_a:
                Toast.makeText(this, "bar：opt_a", Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_opt_b:
                Toast.makeText(this, "bar：opt_b", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }
}
