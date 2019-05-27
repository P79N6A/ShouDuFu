package com.futuretongfu.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class NumberView1 extends View {

    Paint backgroudPaint;
    Paint mPaint;
    Paint txtPaint;

    //宽高
    int mwidth, mheight;
    int bheight, txtheight;

    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;
    private float onewidth;

    //文字显示的宽高
    int textheight;
    int textwidth;

    float num8;
    float num2;

    //    30 10  20

    public NumberView1(Context context) {
        super(context);
        init();
    }

    public NumberView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroudPaint = new Paint();
        backgroudPaint.setAntiAlias(true);
        backgroudPaint.setStyle(Paint.Style.STROKE);
        backgroudPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroudPaint.setColor(Color.rgb(51, 197, 167));

        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setStyle(Paint.Style.FILL);
        txtPaint.setColor(Color.rgb(51, 197, 167));
        txtPaint.setTextSize(sp2px(12));
        txtPaint.setStrokeWidth(2);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.rgb(51, 197, 167));

        num8 = dp2px(8);
        num2 = dp2px(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mwidth = w;
        mheight = h;
        bheight = mheight / 3;
        txtheight = bheight * 2;
        onewidth = mwidth / mMaxProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = onewidth * mCurrentProgress;
        //画背景
        RectF rectF = new RectF(0, 0, mwidth, bheight);
        canvas.drawRoundRect(rectF, num8, num8, backgroudPaint);
        //画进度条
        RectF rectFm;
        if (mCurrentProgress == mMaxProgress) {
            rectFm = new RectF(num2, num2, mwidth - num2, bheight - num2);
        } else {
            rectFm = new RectF(num2, num2, x + num2, bheight - num2);
        }
        canvas.drawRoundRect(rectFm, num8, num8, mPaint);
        //画文字
        //获取文字的宽度及其高度
        Rect rect = new Rect();
        String speed = mCurrentProgress + "%";
        txtPaint.getTextBounds(speed, 0, speed.length(), rect);
        textheight = rect.height();
        textwidth = rect.width();
        if (mCurrentProgress == mMaxProgress) {
            canvas.drawText(speed, mwidth - textwidth, txtheight, txtPaint);
        } else {

            //起始位置 <= 文字宽度
            if (x <= textwidth) {
                canvas.drawText(speed, textwidth / 2, txtheight, txtPaint);
                //当前位置 +文字宽度>=宽度
            } else if (x + textwidth >= mwidth) {
                canvas.drawText(speed, mwidth - textwidth - 2 * num8, txtheight, txtPaint);
            } else {
                canvas.drawText(speed, x - textwidth / 2, txtheight, txtPaint);
            }
        }

    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public void setProgress(int progress) {
        if (progress <= 100 && progress >= 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }


}
