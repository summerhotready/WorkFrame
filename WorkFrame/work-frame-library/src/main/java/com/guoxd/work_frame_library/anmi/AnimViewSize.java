package com.guoxd.work_frame_library.anmi;

import android.view.View;

/**
 * 用于在属性动画中改变组件宽高的
 */
public class AnimViewSize {
    View rView;
    public AnimViewSize(View v){
        rView = v;
    }
    public  int getWidth(){
        return rView.getLayoutParams().width;
    }
    public void setWidth(int width){
        rView.getLayoutParams().width = width;
        rView.requestLayout();
    }

    public int getHeight(){
        return rView.getLayoutParams().height;
    }
    public void setHeight(int height){
        rView.getLayoutParams().height = height;
        rView.requestLayout();
    }
    public int getBoth(){
        return getWidth();
    }
    public void setBoth(int both){
        rView.getLayoutParams().width = both;
        rView.getLayoutParams().height = both;
        rView.requestLayout();
    }
}
