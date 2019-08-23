package com.guoxd.work_frame_library.views.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class PaintSurfaceview extends SurfaceView
        implements SurfaceHolder.Callback, View.OnTouchListener {
    //paint
    private Paint paint = new Paint();
    private Path path = new Path();

    public PaintSurfaceview(Context context) {
        this(context, null);
    }

    public PaintSurfaceview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSurfaceView(context);
    }

    public PaintSurfaceview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSurfaceView(context);
    }

    //初始化
    void initSurfaceView(Context context) {
        getHolder().addCallback(this);
        //初始化画笔
        paint.setColor(Color.BLUE);
        //画笔大小
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.STROKE);
        //设置监听
        setOnTouchListener(this);
    }

    //绘制方法
    public void draw() {
        //锁定画布
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(path, paint);
//        解锁画布
        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //在surfaceview初始化时调用
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            //按下事件，开始绘制
            case MotionEvent.ACTION_DOWN:
                //获取按下点的x,y坐标
                path.moveTo(event.getX(), event.getY());
                //绘制
                draw();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动使用lineto()
                path.lineTo(event.getX(), event.getY());
                draw();
                break;
        }
        //返回true，否则只能响应down事件
        return true;
    }

    //清理画布
    public void clear() {
        //路径重置
        path.reset();
        //重新锁定，否则不能再次绘画
        draw();
    }
}
