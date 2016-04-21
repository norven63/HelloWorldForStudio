package com.myAndroid.helloworld.activity.shareElement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharedElementActivity2 extends Activity {

    @Bind(R.id.share_view)
    View shareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_element2);
        ButterKnife.bind(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.share_view)
    public void share() {
        finishAfterTransition();
    }
}
