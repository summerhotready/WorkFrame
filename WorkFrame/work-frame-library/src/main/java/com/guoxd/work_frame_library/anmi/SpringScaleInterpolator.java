package com.guoxd.work_frame_library.anmi;


import android.view.animation.Interpolator;

/**
 * Created by guoxd on 2018/6/26.
 * 不适合button的点击效果，等动画结束后会有停顿
 */

public class SpringScaleInterpolator implements Interpolator {
    //弹性因数
    private float factor;

    public SpringScaleInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {
//        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        return (float)(Math.pow(2.5, -4*input)*Math.sin((input - factor / 5) * (2 * Math.PI) / factor)+1);
    }
}



