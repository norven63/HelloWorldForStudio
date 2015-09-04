package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.customView.FloatView;

public class FloatWindowActivity extends Activity {
    private Bitmap bitmap;
    private FloatView floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_button1);
        floatView = new FloatView(this);
        floatView.setImageBitmap(bitmap);
        floatView.show();
    }
}
