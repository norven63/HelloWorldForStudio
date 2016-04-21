package com.myAndroid.helloworld.activity.shareElement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharedElementActivity2 extends Activity {

    @Bind(R.id.share_view)
    TextView shareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_element2);
        ButterKnife.bind(this);

        if (getIntent() == null) {
            return;
        }

        shareView.setText(getIntent().getStringExtra("item"));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.share_view)
    public void share() {
        finishAfterTransition();
    }
}
