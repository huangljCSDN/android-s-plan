package com.markLove.Xplan.module.CircleRecyclerView;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CircularViewMode implements ItemViewMode {

    private float mCircleOffset = 500;
    private float mDegToRad = 1.0f / 180.0f * (float) Math.PI;
    private float mScalingRatio = 0.001f;
    //    private float mTranslationRatio = 0.09f;
    private float mTranslationRatio = 0.08f;

    private int xOffset = 200;
    private float mRadiu;
    private int xOffset11 = 0;

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public CircularViewMode() {
    }

    public CircularViewMode(int circleOffset, float degToRad, float scalingRatio, float translationRatio) {
        mCircleOffset = circleOffset;
        mDegToRad = degToRad;
        mScalingRatio = scalingRatio;
        mTranslationRatio = translationRatio;

    }

    public void setOffset11(int xOffset11) {
        this.xOffset11 = xOffset11;
    }

    @Override
    public void applyToView(View v, RecyclerView parent) {
        float halfHeight = v.getHeight() * 0.5f;
        float halfWidth = v.getWidth() * 0.5f;
        float parentHalfHeight = parent.getHeight() * 0.5f;
        float y = v.getY();
        float rot = parentHalfHeight - halfHeight - y;

//        ViewCompat.setPivotX(v, 0.0f);
//        ViewCompat.setPivotY(v, halfHeight);
//        ViewCompat.setRotation(v, rot * 0.05f);
//        ViewCompat.setTranslationX(v, (float)(Math.cos(rot * mTranslationRatio * mDegToRad) + 1) * mCircleOffset);

//        ViewCompat.setTranslationX(v, ((float) (Math.cos(rot * mTranslationRatio * mDegToRad) + 1) * mCircleOffset) - xOffset);

        mRadiu = parentHalfHeight + xOffset;

        float adjustY = y - parentHalfHeight + halfHeight;
        float x = (float) Math.sqrt(Math.abs(mRadiu * mRadiu - adjustY * adjustY)) - xOffset - xOffset11 - halfWidth + 200;

        ViewCompat.setTranslationX(v, x);
//      LogUtils.i("huang","x = "+x);

        //不缩放
//        float scale = 1.0f - Math.abs(parentHalfHeight - halfHeight - y) * mScalingRatio;
////        if (scale < 0.8) {
////            scale = 0.8f;
////        }
////
////        ViewCompat.setScaleX(v, scale);
////        ViewCompat.setScaleY(v, scale);

        float alpha = 1.0f - (int) Math.abs(parentHalfHeight - halfHeight - y) * (mScalingRatio);
        if (alpha < 0.6) {
            alpha = 0.6f;
        }
        ViewCompat.setAlpha(v, alpha);
//        LogUtils.i("huang","alpha"+alpha);
        if (alpha == 1) {
            if (this.onScrollCenterListener != null) {
                this.onScrollCenterListener.onCenterView(v);
            }
        }
    }

    public interface OnScrollCenterListener {
        void onCenterView(View view);
    }

    public OnScrollCenterListener onScrollCenterListener;

    public void setOnScrollCenterListener(OnScrollCenterListener onScrollCenterListener) {
        this.onScrollCenterListener = onScrollCenterListener;
    }

}
