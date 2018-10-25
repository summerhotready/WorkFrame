package com.guoxd.work_frame_library.views.bigImage;

import android.view.MotionEvent;

/**
 * Created by guoxd on 2018/10/16.
 * 点击监听方法
 */

public class EclairMotionEvent  extends WrapMotionEvent {

    protected EclairMotionEvent(MotionEvent event) {
        super(event);
    }

    public float getX(int pointerIndex) {
        return event.getX(pointerIndex);
    }

    public float getY(int pointerIndex) {
        return event.getY(pointerIndex);
    }

    public int getPointerCount() {
        return event.getPointerCount();
    }

    public int getPointerId(int pointerIndex) {
        return event.getPointerId(pointerIndex);
    }

}
