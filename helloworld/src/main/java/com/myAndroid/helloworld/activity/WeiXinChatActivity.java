package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 这里自定义了一个ScrollView控件，并通过重写其onSizeChanged方法实现将界面往上顶，并滑动到最底部内容的效果。详情参阅MyScrollView类
 */
public class WeiXinChatActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weixin_chat);
    }
}
