package com.yndongyong.guideview.style;

import com.yndongyong.guideview.ItemDecoration;
import com.yndongyong.guideview.CutoutViewInfo;

/**
 *  created by yndongyong at 2018.08.01
 */
public class BottomCenterItemDecoration extends ItemDecoration {

    public BottomCenterItemDecoration(int layoutRes, int offsetX, int offsetY) {
        super(layoutRes, offsetX, offsetY);
    }

    public BottomCenterItemDecoration(int layoutResId) {
        super(layoutResId);
    }

    @Override
    public int[] getOffsetLeftAndTop(CutoutViewInfo cutoutViewInfo, int offsetX, int offsetY) {
        int[] out = new int[2];
        out[0] = cutoutViewInfo.offsetLeft + (cutoutViewInfo.width - decoView.getWidth()) / 2 + offsetX;
        out[1] = cutoutViewInfo.offsetTop + cutoutViewInfo.height + offsetY;
        return out;
    }

}
