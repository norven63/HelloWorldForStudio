package com.myAndroid.helloworld.activity.shadow;

import android.app.Activity;
import android.os.Bundle;

import com.myAndroid.helloworld.R;

public class ShadowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
    }
}
