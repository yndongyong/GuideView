package com.yndongyong.guideview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntRange;

/**
 * 承载引导层内容的view
 * created by yndongyong at 2018.08.01
 */
public class GuideView extends FrameLayout implements View.OnClickListener {

    private List<CutoutViewInfo> mCutoutViewInfos;
    private List<ItemDecoration> mItemDecorations;
    private PorterDuffXfermode mPorterDuffXfermode;
    private Paint mPaint;
    private int mMaskBackgroundColor = Color.argb(0x80, 0, 0, 0);

    private OnGuideViewDismissListener mOnGuideViewDismissListener;

    private int mCurrentPosition = -1;
    private boolean isShowAll = false;

    private BlurMaskFilter mBlurMaskFilter;
    private float blurRadius;

    @HighLightShape
    private int highLightShape;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
        setClickable(true);
        setOnClickListener(this);
        mCutoutViewInfos = new ArrayList<>();
        mItemDecorations = new ArrayList<>();
        highLightShape = HighLightShape.TYPE_RECTANGLE;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(mMaskBackgroundColor);

        if (blurRadius > 0) {
            mPaint.setMaskFilter(mBlurMaskFilter);
            if (isShowAll) {
                for (int i = 0; i < mCutoutViewInfos.size(); i++) {
                    CutoutViewInfo view = mCutoutViewInfos.get(i);
                    drawSrcView(view, canvas);
                }
            } else {
                CutoutViewInfo cutoutViewInfo = mCutoutViewInfos.get(mCurrentPosition);
                drawSrcView(cutoutViewInfo, canvas);
            }
            mPaint.setMaskFilter(null);
        }

        if (isShowAll) {
            for (int i = 0; i < mCutoutViewInfos.size(); i++) {
                CutoutViewInfo view = mCutoutViewInfos.get(i);
                drawSrcView(view, canvas);
            }
            mPaint.setXfermode(mPorterDuffXfermode);
            for (int i = 0; i < mCutoutViewInfos.size(); i++) {
                CutoutViewInfo view = mCutoutViewInfos.get(i);
                drawSrcView(view, canvas);
            }
        } else {
            CutoutViewInfo cutoutViewInfo = mCutoutViewInfos.get(mCurrentPosition);
            drawSrcView(cutoutViewInfo, canvas);
            mPaint.setXfermode(mPorterDuffXfermode);
            drawSrcView(cutoutViewInfo, canvas);
        }

        mPaint.setXfermode(null);
        canvas.restoreToCount(saveCount);
    }

    private void drawSrcView(CutoutViewInfo cutoutViewInfo, Canvas canvas) {
        switch (highLightShape) {
            case HighLightShape.TYPE_OVAL:
                canvas.drawOval(new RectF(cutoutViewInfo.offsetLeft, cutoutViewInfo.offsetTop
                        , cutoutViewInfo.offsetLeft + cutoutViewInfo.width, cutoutViewInfo.offsetTop + cutoutViewInfo.height), mPaint);
                break;
            case HighLightShape.TYPE_CIRCULAR:
                float cx, cy, radius;
                cy = cutoutViewInfo.offsetTop + cutoutViewInfo.height / 2;
                cx = cutoutViewInfo.offsetLeft + cutoutViewInfo.width / 2;
                radius = Math.max(cutoutViewInfo.height / 2, cutoutViewInfo.width / 2);
                canvas.drawCircle(cx, cy, radius, mPaint);
                break;
            case HighLightShape.TYPE_ROUNDRECT:
                canvas.drawRoundRect(new RectF(cutoutViewInfo.offsetLeft, cutoutViewInfo.offsetTop
                        , cutoutViewInfo.offsetLeft + cutoutViewInfo.width, cutoutViewInfo.offsetTop + cutoutViewInfo.height), 10, 10, mPaint);
                break;
            case HighLightShape.TYPE_RECTANGLE:
                canvas.drawRect(cutoutViewInfo.offsetLeft, cutoutViewInfo.offsetTop
                        , cutoutViewInfo.offsetLeft + cutoutViewInfo.width, cutoutViewInfo.offsetTop + cutoutViewInfo.height, mPaint);
                break;
        }

    }

    @Override
    public void onClick(View view) {
        if (isShowAll) {
            ((ViewGroup) this.getParent()).removeView(this);
            if (mOnGuideViewDismissListener != null) {
                mOnGuideViewDismissListener.onDisMiss();
            }
        } else {
            showNextGuideView();

        }
    }

    public void showNextGuideView() {
        mCurrentPosition++;
        if (mCurrentPosition >= mCutoutViewInfos.size()) {
            ((ViewGroup) this.getParent()).removeView(this);
            if (mOnGuideViewDismissListener != null) {
                mOnGuideViewDismissListener.onDisMiss();
            }
            return;
        }
        removeAllViews();
        mItemDecorations.get(mCurrentPosition).showDecorationView(mCutoutViewInfos.get(mCurrentPosition), this);

    }

    public void showAllGuideView() {
        removeAllViews();
        for (int i = 0; i < mItemDecorations.size(); i++) {
            mItemDecorations.get(i).showDecorationView(mCutoutViewInfos.get(i), this);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCutoutViewInfos = null;
        mItemDecorations = null;
        mPorterDuffXfermode = null;
        mPaint = null;
        mOnGuideViewDismissListener = null;
        mBlurMaskFilter = null;

    }

    public interface OnGuideViewDismissListener {
        void onDisMiss();

    }

    //<editor-folder desc="暴露的方法">

    protected void setInfoViewList(List<CutoutViewInfo> cutoutViewInfos) {
        mCutoutViewInfos.addAll(cutoutViewInfos);
    }

    protected void setItemDecorationList(List<ItemDecoration> itemDecorationList) {
        mItemDecorations.addAll(itemDecorationList);
    }

    protected void setShowAll(boolean showAll) {
        isShowAll = showAll;
    }

    protected void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mMaskBackgroundColor = Color.argb(alpha, 0, 0, 0);
    }

    protected void setHighLightShape(@HighLightShape int highLightShape) {
        this.highLightShape = highLightShape;
    }

    protected void setBlurRadius(float radius) {
        this.blurRadius = radius;
        mBlurMaskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
    }

    protected void setOnGuideViewDismissListener(OnGuideViewDismissListener onGuideViewDismissListener) {
        mOnGuideViewDismissListener = onGuideViewDismissListener;
    }

    //</editor-folder desc="暴露的方法">

}
