package com.guoxd.work_frame_library.anmi;

import android.view.animation.Interpolator;

/**
 * Created by guoxd on 2018/6/27.
 */

public class SmoothstepMoveInterpolator implements Interpolator{
    @Override
    public float getInterpolation(float x) {
        return x * x * (3 - 2 * x);
    }
}
