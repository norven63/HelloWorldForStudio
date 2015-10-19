package com.myAndroid.helloworld.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

public class AnimationActivity extends Activity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        /*
        * 缩放动画
        */
        View scaleView = findViewById(R.id.scale);
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleView.startAnimation(scaleAnimation);

        /*
        * 旋转动画
        */
        View rotateView = findViewById(R.id.rotate);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        rotateView.startAnimation(rotateAnimation);

        /*
         * 移动动画
         */
        View translate = findViewById(R.id.translate);
        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        translate.startAnimation(translateAnimation);

        /*
         * 翻转动画
         */
        final TextView rotationYView = (TextView) findViewById(R.id.rotationY);
        rotationYView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rotation(rotationYView);
            }
        });
        rotation(rotationYView);
    }

    /*
     * 翻转动画
     */
    private void rotation(final View rotationYView) {
        rotationYView.animate().rotationY(90).setDuration(1000)
                // 设置加速器，为了动画衔接时保证良好的流畅度
                .setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                rotationYView.setRotationY(-90);// 需要先置为-90度，否者整个View将会反过来
                rotationYView.animate().rotationY(0).setDuration(1000)
                        // 设置加速器，为了动画衔接时保证良好的流畅度
                        .setInterpolator(new DecelerateInterpolator()).setListener(null);// 由-90度转到0度，则View是正的
            }
        });
    }
}
