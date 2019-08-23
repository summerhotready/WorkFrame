package com.guoxd.work_frame_library.views.widges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 圆形Imageview
 */
public class ChangeRingImageView extends AppCompatImageView implements View.OnTouchListener {

    public ChangeRingImageView(Context context, AttributeSet attr,int defStyle){
        super(context,attr,defStyle);
        initPaint();
    }
    public ChangeRingImageView(Context context, AttributeSet attr){
        this(context,attr,0);
    }
    public ChangeRingImageView(Context context){
        this(context,null,0);
    }

    final String TAG="ChangeRingImageView";
    //组件宽高
    private int width = 0;
    private int height = 0;
    //环的中心点
    private float cx=150;//x轴坐标
    private float cy=150;//Y轴坐标
    //环的半径
    private float radius=100;//半径
    //环的宽度
    private float borderWidth=2f;//宽
    private Paint paint;//画笔

    void initPaint(){
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG,String.format("onMeasure height:%d,width:%d,heightSpace:%d,widthSpace:%d",getMeasuredHeight(),getMeasuredWidth(),heightMeasureSpec,widthMeasureSpec));

        if(getMeasuredHeight()>0 && getMeasuredWidth()>0){
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            cx = width/2.0f;
            cy = height/2.0f;
            radius = Math.min(cx,cy);
            Log.i(TAG,String.format("onMeasure point size:%f,(%f,%f)",radius,cx,cy));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG,"onDraw");

            canvas.drawCircle(cx, cy, radius - borderWidth, paint);
            //刷新
//
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.i(TAG,String.format("onTouch (%f,%f)",motionEvent.getX(),motionEvent.getY()));
        return false;
    }
}
