package com.yndongyong.guideview;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import androidx.annotation.IntDef;

/**
 *  created by yndongyong at 2018.08.01
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({HighLightShape.TYPE_RECTANGLE,HighLightShape.TYPE_ROUNDRECT,HighLightShape.TYPE_CIRCULAR,HighLightShape.TYPE_OVAL})
public @interface HighLightShape {

     int TYPE_RECTANGLE  = 0x1;
     int TYPE_ROUNDRECT  = 0x2;
     int TYPE_CIRCULAR = 0x3 ;
     int TYPE_OVAL = 0x4 ;

}
