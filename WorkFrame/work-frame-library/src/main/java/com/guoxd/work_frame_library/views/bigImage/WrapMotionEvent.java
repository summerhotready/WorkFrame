package com.guoxd.work_frame_library.views.bigImage;

import android.view.MotionEvent;

/**
 * Created by guoxd on 2018/10/16.
 * 点击监听方法
 */

public class WrapMotionEvent {

    protected MotionEvent event;
    protected WrapMotionEvent(MotionEvent event) {
        this.event = event;
    }

    static public WrapMotionEvent wrap(MotionEvent event) {
        try {
            return new EclairMotionEvent(event);
        } catch (VerifyError e) {
            return new WrapMotionEvent(event);
        }
    }



    public int getAction() {
        return event.getAction();
    }

    public float getX() {
        return event.getX();
    }

    public float getX(int pointerIndex) {
        verifyPointerIndex(pointerIndex);
        return getX();
    }

    public float getY() {
        return event.getY();
    }

    public float getY(int pointerIndex) {
        verifyPointerIndex(pointerIndex);
        return getY();
    }

    public int getPointerCount() {
        return 1;
    }

    public int getPointerId(int pointerIndex) {
        verifyPointerIndex(pointerIndex);
        return 0;
    }

    private void verifyPointerIndex(int pointerIndex) {
        if (pointerIndex > 0) {
            throw new IllegalArgumentException(
                    "Invalid pointer index for Donut/Cupcake");
        }
    }
}
