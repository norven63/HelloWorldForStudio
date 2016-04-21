package com.myAndroid.helloworld.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LayoutTransitionActivity extends Activity {
    @Bind(R.id.button_add)
    Button button;
    @Bind(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_transition);
        ButterKnife.bind(this);

        LayoutTransition transition = new LayoutTransition();
        container.setLayoutTransition(transition);

        //进入动画：翻转
        PropertyValuesHolder appearSlide = PropertyValuesHolder.ofFloat("x", 0, 1);
        PropertyValuesHolder appearScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        PropertyValuesHolder appearScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);

        Animator appearAnim = ObjectAnimator.ofPropertyValuesHolder("Just a parameter", appearSlide, appearScaleY, appearScaleX);
        appearAnim.setDuration(transition.getDuration(LayoutTransition.APPEARING));

        transition.setAnimator(LayoutTransition.APPEARING, appearAnim);

        //退出动画：翻转
        Animator disAppearAnim = ObjectAnimator.ofFloat(null, "rotationY", 0f, 90f).setDuration(transition.getDuration(LayoutTransition.APPEARING));
        transition.setAnimator(LayoutTransition.DISAPPEARING, disAppearAnim);

        //改变动画：...
        PropertyValuesHolder pvhSlide = PropertyValuesHolder.ofFloat("y", 1, 0);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f, 1f);
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f, 1f);

        Animator changingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder("Just a parameter", pvhSlide, pvhScaleY, pvhScaleX);
        changingAppearingAnim.setDuration(transition.getDuration(LayoutTransition.CHANGE_DISAPPEARING));

        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changingAppearingAnim);
    }

    @OnClick(R.id.button_add)
    public void addItem(View view) {
        final Button button = new Button(this);
//        button.setPivotX(1f);
//        button.setPivotY(1f);
        button.setText("New Button");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(button);
            }
        });

        container.addView(button, 0);
    }
}
