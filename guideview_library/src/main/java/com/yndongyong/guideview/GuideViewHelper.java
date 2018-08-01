package com.yndongyong.guideview;

import android.app.Activity;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 *  created by yndongyong at 2018.08.01
 */
public class GuideViewHelper {

    private List<CutoutViewInfo> mCutoutViewInfos;
    private GuideView guideView;

    private GuideViewParams P;

    public void show(Fragment fragment) {
        show(fragment, null);
    }

    public void show(Fragment fragment, GuideView.OnGuideViewDismissListener listener) {
        show(fragment.getActivity(), null);
    }

    public void show(Activity activity) {
        show(activity, null);
    }

    public void show(final Activity activity, final GuideView.OnGuideViewDismissListener listener) {
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                showGuideView(activity.getWindow().getDecorView(), listener);
                return false;
            }
        });
    }

    private void showGuideView(View decorView, GuideView.OnGuideViewDismissListener listener) {
        guideView = new GuideView(decorView.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ((ViewGroup) decorView).addView(guideView, layoutParams);
        guideView.setItemDecorationList(P.mItemDecorations);
        guideView.setShowAll(P.isShowAll);
        if (P.blurRadius != -1.0f) {
            guideView.setBlurRadius(P.blurRadius);
        }
        guideView.setHighLightShape(P.highLightShape);
        //padding
        mCutoutViewInfos = new ArrayList<>();
        for (int i = 0; i < P.mViews.size(); i++) {
            View view = P.mViews.get(i);
            CutoutViewInfo cutoutViewInfo = getViewInfo(view, P.padding, P.highLightShape);
            mCutoutViewInfos.add(cutoutViewInfo);
        }
        guideView.setInfoViewList(mCutoutViewInfos);
        if (listener != null) {
            guideView.setOnGuideViewDismissListener(listener);
        }
        guideView.requestLayout();
        if (P.isShowAll) {
            guideView.showAllGuideView();
        } else {
            guideView.showNextGuideView();
        }

    }

    private CutoutViewInfo getViewInfo(View view, int padding, @HighLightShape int highLightType) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        CutoutViewInfo cutoutViewInfo = new CutoutViewInfo();

        switch (highLightType) {
            case HighLightShape.TYPE_RECTANGLE:
            case HighLightShape.TYPE_ROUNDRECT:
            case HighLightShape.TYPE_OVAL:
                cutoutViewInfo.offsetLeft = location[0] - padding;
                cutoutViewInfo.offsetTop = location[1] - padding;
                cutoutViewInfo.width = view.getWidth() + padding * 2;
                cutoutViewInfo.height = view.getHeight() + padding * 2;
                break;
            case HighLightShape.TYPE_CIRCULAR:
                int diameter = Math.max(view.getWidth() + 2 * padding, view.getHeight() + 2 * padding);
                cutoutViewInfo.offsetLeft = location[0] - (diameter - view.getWidth()) / 2;
                cutoutViewInfo.offsetTop = location[1] - (diameter - view.getHeight()) / 2;
                cutoutViewInfo.width = diameter;
                cutoutViewInfo.height = diameter;
            default:
                break;

        }
        return cutoutViewInfo;
    }

    private void apply(GuideViewParams params) {
        P = params;
    }


    public static class Builder {

        GuideViewParams mParams;

        public Builder() {
            mParams = new GuideViewParams();
        }

        public Builder addView(View view, ItemDecoration itemDecoration) {
            mParams.mViews.add(view);
            mParams.mItemDecorations.add(itemDecoration);
            return this;
        }

        /**
         * 是否全部一起显示
         * @param showAll true:全部一起显示，false：一个接一个显示
         * @return
         */
        public Builder showAll(boolean showAll) {
            mParams.isShowAll = showAll;
            return this;
        }

        /**
         * 要高亮的view的内容偏移
         * @param padding
         * @return
         */
        public Builder padding(int padding) {
            mParams.padding = padding;
            return this;
        }

        /**
         * 高亮的view的形状
         * @param highLightShape
         */
        public Builder setHighLightShape(@HighLightShape int highLightShape) {
            mParams.highLightShape = highLightShape;
            return this;
        }

        /**
         * blur 风格，blur的半径
         * @param blurRadius
         */
        public void setBlurRadius(float blurRadius) {
            mParams.blurRadius = blurRadius;
        }

        public GuideViewHelper build() {
            GuideViewHelper guideViewHelper = new GuideViewHelper();
            guideViewHelper.apply(mParams);
            return guideViewHelper;
        }

    }

    private static class GuideViewParams {

        private List<ItemDecoration> mItemDecorations;
        private boolean isShowAll = true;
        private List<View> mViews;
        private int padding;
        @HighLightShape
        private int highLightShape;
        private float blurRadius = -1.0f;

        private GuideViewParams() {
            mItemDecorations = new ArrayList<>();
            mViews = new ArrayList<>();
        }
    }

}
