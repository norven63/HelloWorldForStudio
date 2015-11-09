package com.myAndroid.helloworld.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.customView.MyCanvasView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AnimationActivity extends Activity {
    @Bind(R.id.rotate)
    View rotate;
    @Bind(R.id.rotationY)
    TextView rotationY;
    @Bind(R.id.scale)
    View scale;
    @Bind(R.id.translate)
    View translate;
    @Bind(R.id.objectAnimator)
    MyCanvasView objectAnimator;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        ButterKnife.bind(this);

        /*
        * 缩放动画
        */
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scale.startAnimation(scaleAnimation);

        /*
        * 旋转动画
        */
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        rotate.startAnimation(rotateAnimation);

        /*
         * 移动动画
         */
        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        translate.startAnimation(translateAnimation);

        /*
         * 翻转动画
         */
        rotationY.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rotation(rotationY);
            }
        });
        rotation(rotationY);

        /*
         * 动画集
         */
        ValueAnimator colorAnim = ObjectAnimator.ofInt(objectAnimator.getBackground(), "color", getResources().getColor(android.R.color.holo_red_dark), getResources().getColor(android.R.color.holo_orange_light));
        colorAnim.setEvaluator(new ArgbEvaluator());//该接口可以使眼色柔和的渐变

        final int animFromWidth = (int) getResources().getDimension(R.dimen.animationFromWidth);
        ValueAnimator scalXAnim = ValueAnimator.ofInt(animFromWidth, animFromWidth / 4);
        scalXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                objectAnimator.getBackground().setBounds(animFromWidth - (int) animation.getAnimatedValue(), 0, animFromWidth, animFromWidth / 4);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(colorAnim, scalXAnim);
        animatorSet.setDuration(1800);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                objectAnimator.setState(MyCanvasView.StateEnum.PROGRESS);
                objectAnimator.invalidate();
            }
        });
        animatorSet.start();
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
