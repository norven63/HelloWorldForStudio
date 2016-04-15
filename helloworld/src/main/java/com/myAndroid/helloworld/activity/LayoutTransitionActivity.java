package com.myAndroid.helloworld.activity;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
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
    @Bind(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_transition);
        ButterKnife.bind(this);

        LayoutTransition transition = new LayoutTransition();
        container.setLayoutTransition(transition);

        //进入动画：反转
        Animator appearAnim = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).setDuration(transition.getDuration(LayoutTransition.APPEARING));
        transition.setAnimator(LayoutTransition.APPEARING, appearAnim);

        
    }

    @OnClick(R.id.button_add)
    public void addItem(View view) {
        final Button button = new Button(this);
        button.setText("New Button");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(button);
            }
        });

        container.addView(button);
    }
}
