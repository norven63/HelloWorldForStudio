package com.myAndroid.helloworld.activity.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.myAndroid.helloworld.R;

/**
 * @author ZhouXinXing
 * @date 2016年03月11日 20:12
 * @Description
 */
public class ShadowView extends View {
    private final int BG_TYPE_RECT = -1;
    private final int BG_TYPE_CIRCLE = -2;

    private float sdDx = 0;
    private float sdDy = 0;
    private float sdRadius = 0;
    private int bgType = -1;
    private int sdColor = 0x00ffffff;
    private int bgColor = 0x00ffffff;
    private Bitmap srcBitmap;

    private Paint bgPaintWithShadow;
    private Paint srcPaint;

    public ShadowView(Context context) {
        super(context);

        init(null);
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.shadowView);

            sdDx = typedArray.getDimension(R.styleable.shadowView_sd_dx, sdDx);
            sdDy = typedArray.getDimension(R.styleable.shadowView_sd_dy, sdDy);
            sdRadius = typedArray.getDimension(R.styleable.shadowView_sd_radius, sdRadius);
            sdColor = typedArray.getColor(R.styleable.shadowView_sd_color, sdColor);
            bgColor = typedArray.getColor(R.styleable.shadowView_bg_color, sdColor);
            bgType = typedArray.getInteger(R.styleable.shadowView_bg_type, bgType);

            Drawable drawable = typedArray.getDrawable(R.styleable.shadowView_src);
            if (drawable != null) {
                srcBitmap = ((BitmapDrawable) drawable).getBitmap();
            }

            typedArray.recycle();
        }

        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速。此方法一定要调用，否则阴影无效。

        srcPaint = new Paint();

        bgPaintWithShadow = new Paint();
        bgPaintWithShadow.setColor(bgColor);

        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        bgPaintWithShadow.setShadowLayer(sdRadius, sdDx, sdDy, sdColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bgType == BG_TYPE_RECT) {
            float left = sdRadius - sdDx + 10;
            float top = sdRadius - sdDy + 10;
            float right = getWidth() - sdDx - sdRadius;
            float bottom = getHeight() - sdDy - sdRadius;

            canvas.drawRect(left, top, right, bottom, bgPaintWithShadow);
        } else if (bgType == BG_TYPE_CIRCLE) {
            float diameter = getWidth() <= getHeight() ? (getWidth() - sdRadius * 2) : (getHeight() - sdRadius * 2);

            canvas.drawCircle(getWidth() / 2 - sdDx, getHeight() / 2 - sdDy, diameter / 2, bgPaintWithShadow);
        }

//        super.onDraw(canvas);
//
//
//
//        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//        if (srcBitmap != null) {
//            canvas.drawBitmap(srcBitmap, getWidth() / 2 - srcBitmap.getWidth() / 2, getHeight() / 2 - srcBitmap.getHeight() / 2, bgPaintWithShadow);
//        }
    }
}
