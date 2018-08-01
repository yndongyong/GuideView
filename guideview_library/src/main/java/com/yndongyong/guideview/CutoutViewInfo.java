package com.yndongyong.guideview;

/**
 *  要被挖空的view的信息
 *  created by yndongyong at 2018.08.01
 */
public class CutoutViewInfo {

    public int offsetTop;
    public int offsetLeft;
    public int width;
    public int height;

    public CutoutViewInfo() {
    }

    public CutoutViewInfo(int offsetTop, int offsetLeft, int width, int height) {
        this.offsetTop = offsetTop;
        this.offsetLeft = offsetLeft;
        this.width = width;
        this.height = height;
    }

}
