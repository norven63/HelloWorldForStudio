package com.myAndroid.helloworld.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyCanvasView extends View {
    private Paint paint;

    private StateEnum state = StateEnum.DEFAULT;

    public enum StateEnum {
        DEFAULT,
        PROGRESS;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public MyCanvasView(Context context) {
        super(context);

        intView();
    }

    public MyCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        intView();
    }

    public MyCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        intView();
    }

    private void intView() {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (state == StateEnum.DEFAULT) {
            return;
        }

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2f);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(70, 70, 30, paint);
    }
}
