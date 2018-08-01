package com.yndongyong.guideview;


import android.os.Looper;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

/**
 *  created by yndongyong at 2018.08.01
 */
public abstract class ItemDecoration {

    protected int offsetX;
    protected int offsetY;
    @LayoutRes
    protected int layoutResId;
    protected View decoView;

    public ItemDecoration(@LayoutRes int layoutResId) {
        this(layoutResId, 0, 0);
    }

    public ItemDecoration(@LayoutRes int layoutResId, int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.layoutResId = layoutResId;
    }

    public void showDecorationView(final CutoutViewInfo cutoutViewInfo, final ViewGroup parent) {

        if (decoView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            decoView = inflater.inflate(layoutResId, parent, false);
        }
        if (decoView.getParent() != null) {
            ((ViewGroup) decoView.getParent()).removeView(decoView);
        }

        decoView.setVisibility(View.INVISIBLE);
        parent.addView(decoView);
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) decoView.getLayoutParams();
                int[] offsetLeftAndTop = getOffsetLeftAndTop(cutoutViewInfo, offsetX, offsetY);
                layoutParams.leftMargin = offsetLeftAndTop[0];
                layoutParams.topMargin = offsetLeftAndTop[1];
                decoView.requestLayout();
                decoView.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }

    /**
     * @param cutoutViewInfo
     * @param offsetX
     * @param offsetY
     * @return int[] int[0] left axis_x,int[1] top axis_y
     */
    public abstract int[] getOffsetLeftAndTop(CutoutViewInfo cutoutViewInfo, int offsetX, int offsetY);

}
