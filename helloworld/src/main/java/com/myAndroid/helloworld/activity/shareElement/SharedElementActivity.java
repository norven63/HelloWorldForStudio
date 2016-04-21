package com.myAndroid.helloworld.activity.shareElement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharedElementActivity extends Activity {

    @Bind(R.id.share_view)
    View shareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Explode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(true);
        }
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_element);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.share_view)
    public void share() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, SharedElementActivity2.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, shareView, "share");//android:transitionName="share"
            startActivity(intent, options.toBundle());
        }
    }
}
