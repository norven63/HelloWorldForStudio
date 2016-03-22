package com.myAndroid.helloworld.activity.shadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.myAndroid.helloworld.R;

/**
 * @author ZhouXinXing
 * @date 2016年03月11日 20:12
 * @Description
 */
public class ShadowView extends ImageView {
    private Paint paint;
    private Bitmap bitmap;

    public ShadowView(Context context) {
        super(context);

        init();
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        paint = new Paint();
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)  
        paint.setShadowLayer(5, 3, 3, 0x000000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        Log.e("test", "bitmap = " + (bitmap == null));
        canvas.drawBitmap(bitmap, getWidth() / 2, getHeight() / 2, paint);
    }
}
