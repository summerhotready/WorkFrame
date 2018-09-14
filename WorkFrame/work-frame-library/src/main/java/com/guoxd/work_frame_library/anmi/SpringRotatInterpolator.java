package com.guoxd.work_frame_library.anmi;

import android.view.animation.Interpolator;

/**
 * Created by guoxd on 2018/6/26.
 */

public class SpringRotatInterpolator implements Interpolator {

    float factor;
    public SpringRotatInterpolator(float value){
        factor = value;
    }
    @Override
    public float getInterpolation(float input) {
       return (float)(Math.pow(2.5, -4 * input)*Math.sin((input - factor / 4) * (2 * Math.PI) / factor)+1);
    }
}
