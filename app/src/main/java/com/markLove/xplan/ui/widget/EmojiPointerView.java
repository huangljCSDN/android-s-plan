package com.markLove.xplan.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.markLove.xplan.R;



/**
 * Created by luoyunmin on 2017/11/1.
 */

public class EmojiPointerView extends View {

    private float mRadius = 10;
    private float mDistance = 10;
    private int mDefaultColor = Color.GRAY;
    private int mSelectColor = Color.YELLOW;
    private int mCount = 3;
    private int mSelectPoint = 1;
    private Paint mPaint;

    public EmojiPointerView(Context context) {
        this(context, null);
    }

    public EmojiPointerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EmojiPointerView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.EmojiPointerView_radius:
                    mRadius = a.getDimension(attr, mRadius);
                    break;
                case R.styleable.EmojiPointerView_distance:
                    mDistance = a.getDimension(attr, mDistance);
                    break;
                case R.styleable.EmojiPointerView_defaultColor:
                    mDefaultColor = a.getColor(attr, mDefaultColor);
                    break;
                case R.styleable.EmojiPointerView_selectColor:
                    mSelectColor = a.getColor(attr, mSelectColor);
                    break;
                case R.styleable.EmojiPointerView_count:
                    mCount = a.getInteger(attr, mCount);
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mPaint.setColor(mDefaultColor);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (getPaddingLeft() + getPaddingRight() + mRadius * 2 * mCount + (mCount - 1) * mDistance);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (getPaddingTop() + getPaddingBottom() + mRadius * 2);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mCount; i++) {
            if ((mSelectPoint - 1) == i) {
                mPaint.setColor(mSelectColor);
            } else {
                mPaint.setColor(mDefaultColor);
            }
            float x = getPaddingLeft() + mRadius * (i * 2 + 1) + mDistance * i;
            float y = getPaddingTop() + mRadius;
            canvas.drawCircle(x, y, mRadius, mPaint);
        }
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
    }

    public void setDistance(float distance) {
        this.mDistance = distance;
    }

    public void setDefaultColor(@ColorInt int defaultColor) {
        this.mDefaultColor = defaultColor;
    }

    public void setSelectColor(@ColorInt int selectColor) {
        this.mSelectColor = selectColor;
    }

    public void setSelectPoint(int selectPoint) {
        this.mSelectPoint = selectPoint;
    }

    public void setCount(int count) {
        this.mCount = count;
    }
}
