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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.customView.MyCanvasView;

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
    @Bind(R.id.button_entity)
    ImageView buttonEntity;
    @Bind(R.id.button_shadow)
    ImageView buttonShadow;
    @Bind(R.id.elastic)
    RelativeLayout elastic;

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
        //注意，这里渐变的是scale的背景
        ValueAnimator colorAnim = ObjectAnimator.ofInt(scale.getBackground(), "color", getResources().getColor(android.R.color.holo_red_dark), getResources().getColor(R.color.blue));
        colorAnim.setEvaluator(new ArgbEvaluator());//该接口可以使眼色柔和的渐变

        final int animFromWidth = (int) getResources().getDimension(R.dimen.animationFromWidth);
        ValueAnimator scalXAnim = ValueAnimator.ofInt(animFromWidth, animFromWidth / 4);
        scalXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//当前动画帧回调的值
                int fraction = (int) animation.getAnimatedFraction();//当前动画完成的百分比
                objectAnimator.getBackground().setBounds(animFromWidth - value, 0, animFromWidth, animFromWidth / 4);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        //如果是加载xml布局中的动画系，可以调用如下接口
        AnimatorSet animatorSet2 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.object_animator);

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

        /*
         * 弹性动画
         */
        ObjectAnimator elasticAnimatorX = ObjectAnimator.ofFloat(elastic, "scaleX", 1.1f, 0.4f, 1.1f, 0.8f, 1.0f, 1.0f);//该方法传入的若干个浮点参数，表示该动画将使目标控件的scaleX属性在这若干个值之间来回变动
        elasticAnimatorX.setDuration(500);
        ObjectAnimator elasticAnimatorY = ObjectAnimator.ofFloat(elastic, "scaleY", 0.9f, 1.0f, 0.6f, 1.0f, 0.9f, 1.0f);
        elasticAnimatorY.setDuration(500);

        final AnimatorSet elasticAnimatorSet = new AnimatorSet();
        elasticAnimatorSet.setInterpolator(new LinearInterpolator());
        elasticAnimatorSet.playTogether(elasticAnimatorX, elasticAnimatorY);

        elastic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 切记要在绘制完成UI后再开始执行动画
                 */
                elasticAnimatorSet.start();
            }
        });
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
