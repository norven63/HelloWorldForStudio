package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class GlideActivity extends Activity {
    @Bind(R.id.test_textView)
    TextView testTextView;
    @Bind(R.id.test_button)
    Button testButton;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.imageView_2)
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_glide);
        ButterKnife.bind(this);

        Glide.with(this).load("http://img3.imgtn.bdimg.com/it/u=533786160,1852532877&fm=21&gp=0.jpg").centerCrop().placeholder(R.drawable.ic_launcher).crossFade().into(imageView);
        Glide.with(this).load("http://a.hiphotos.baidu.com/image/w%3D310/sign=be191b5ef4246b607b0eb475dbf81a35/b3b7d0a20cf431adcbff06344936acaf2edd9889.jpg").centerCrop().animate(R.anim.test).placeholder(R.drawable.ic_launcher).into(imageView2);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testButton.startAnimation(AnimationUtils.loadAnimation(GlideActivity.this, R.anim.test));
            }
        });
    }
}
