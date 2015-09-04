package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.myAndroid.helloworld.R;

/**
 * 由于是FrameLayout布局，所以在onTouch事件里如果对布局下的其他View(那些TextView)进行操作，那么会导致整个布局重绘，从而使得图片再次复位
 * ，效果即是图片原地不动。所以针对此现象，需要将TextView的宽和高设定死。
 */
public class ViewMoveActivity extends Activity {
    private TextView screenWidthView;
    private TextView screenHeightView;
    private TextView leftView;
    private TextView topView;
    private TextView rightView;
    private TextView bottomView;
    private View moveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.move_view);

        screenWidthView = (TextView) findViewById(R.id.screen_width);
        screenHeightView = (TextView) findViewById(R.id.screen_height);
        leftView = (TextView) findViewById(R.id.moveView_left);
        topView = (TextView) findViewById(R.id.moveView_top);
        rightView = (TextView) findViewById(R.id.moveView_right);
        bottomView = (TextView) findViewById(R.id.moveView_bottom);
        moveView = findViewById(R.id.moveView);

        moveView.setOnTouchListener(new OnTouchListener() {
            int screenWidth;
            int screenHeight;

            {
                screenWidth = ViewMoveActivity.this.getResources().getDisplayMetrics().widthPixels;
                screenHeight = ViewMoveActivity.this.getResources().getDisplayMetrics().heightPixels - 50;
                screenWidthView.setText("屏宽: " + screenWidth);
                screenHeightView.setText("屏高: " + screenHeight + "+50");
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 注意算法
                int vx = v.getWidth() / 2;
                int vy = v.getHeight() / 2;

                int dx = (int) (event.getX() - vx);
                int dy = (int) (event.getY() - vy);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                            int top = v.getTop() + dy;
                            int bottom = v.getBottom() + dy;
                            int left = v.getLeft() + dx;
                            int rigth = v.getRight() + dx;

                            // 边界处理
                            if (top < 0) {
                                top = 0;
                                bottom = v.getHeight();
                            }

                            if (bottom > screenHeight) {
                                bottom = screenHeight;
                                top = screenHeight - v.getHeight();
                            }

                            if (left < 0) {
                                left = 0;
                                rigth = v.getWidth();
                            }

                            if (rigth > screenWidth) {
                                rigth = screenWidth;
                                left = screenWidth - v.getWidth();
                            }

                            v.layout(left, top, rigth, bottom);

                            leftView.setText("左: " + moveView.getLeft());
                            topView.setText("上: " + moveView.getTop());
                            rightView.setText("右: " + moveView.getRight());
                            bottomView.setText("下: " + moveView.getBottom());
                        }

                        break;

                    case MotionEvent.ACTION_UP:

                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }
}
