package com.guoxd.work_frame_library.views.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 多个SurfaceView公用WeakHandler
 */
public class HandlerMuitSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, Handler.Callback {

    final String TAG = "HandlerMuitSurfaceView";
    static final int MESSAGE_DRAW = 0;

    private boolean isQuitHandlerThreadWhenDestroy = true;
    private HandlerThread handlerThread;
    private WeakHandler handler;

    private volatile Path path = new Path();

    public HandlerMuitSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    public HandlerMuitSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandlerMuitSurfaceView(Context context) {
        this(context, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                refresh();
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                refresh();
                break;
            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }

    private void refresh() {
        Message message = Message.obtain();
        message.what = MESSAGE_DRAW;
        handler.removeMessages(MESSAGE_DRAW);
        handler.sendMessage(message);
    }

    public WeakHandler setHandlerThread(HandlerThread thread) {
        return setHandlerThread(thread, null);
    }

    public WeakHandler setHandlerThread(HandlerThread thread, Handler.Callback callback) {
        if (thread == null) {
            Log.w(TAG, "the HandlerThread set is null");
            return null;
        }
        return initHandler(thread, callback,null);
    }

    public WeakHandler setHandlerThread(HandlerThread thread, Handler.Callback callback, WeakHandler handler) {
        if (thread == null) {
            Log.w(TAG, "the HandlerThread set is null");
            return null;
        }
        return initHandler(thread, callback, handler);
    }

    private WeakHandler initHandler(HandlerThread thread, Handler.Callback callback, WeakHandler h) {
        this.handlerThread = thread;
        if (handlerThread.getLooper() == null) {
            handlerThread.start();
        }
        if (callback == null) {
            callback = this;
        }
        if (h == null) {
            handler = new WeakHandler(thread.getLooper(), callback);
        } else {
            handler = h;
        }
        return handler;
    }


    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MESSAGE_DRAW:
                Canvas canvas = getHolder().lockCanvas();
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(10);
                canvas.drawColor(Color.WHITE);
                canvas.drawPath(path, paint);
                getHolder().unlockCanvasAndPost(canvas);
                return true;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (handlerThread == null) {
            handlerThread = new HandlerThread(TAG);
            initHandler(handlerThread, null,null);
            isQuitHandlerThreadWhenDestroy = true;
        }
        refresh();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (isQuitHandlerThreadWhenDestroy) {
            handlerThread.quit();
            handlerThread = null;
        }
    }
}
