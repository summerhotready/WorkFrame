package com.guoxd.work_frame_library.views.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.guoxd.work_frame_library.R;

import java.util.HashMap;

/**
 * Created by guoxd on 2018/6/4.
 * base on ProgressBGView
 */

public class ProgressShowView extends LinearLayout {
    public final String TAG = "ProgressShowView";
    //只支持矩形的绘制
    public final int Rectangle=1;
    public Context mContext;

    //progress max value
    public int mMax=10;
    //组件宽高
    int viewWidth=0;
    int viewHeight=0;
    //default background color
    public int defaultColor;
    //draw view shape
    public int type=0;

    //use to draw
    public Paint paintView ;

    //color map use to draw background for data
    public HashMap<Integer,Integer> colorMap;
    //which key is object's type,and if not you want can return null;
    public int[] data;

    //when value is true limit need change by data,false not do
    public boolean isCorrectionLimit = true;

    public ProgressShowView(Context context) {
        this(context,null);
    }
    public ProgressShowView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ProgressShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        this.mContext = context;
        paintView = new Paint();
        paintView.setStyle(Paint.Style.FILL);
        paintView.setAntiAlias(true);
        type = Rectangle;
        TypedArray type = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressShowView,0,0);
        try{
            defaultColor = type.getResourceId(R.styleable.ProgressShowView_ProgressBackground,0);
        }catch (Exception e){
        }finally {
            type.recycle();
        }
        if(defaultColor == 0){
            defaultColor = android.R.color.white;
        }
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int mMax) {
        this.mMax = mMax;
    }


    public void setMode(int type) {
        this.type = Rectangle;
    }

    public void setData(int[] data){
        this.data = data;
        invalidate();
    }

    public void setColor(HashMap<Integer,Integer> colorMap){
        this.colorMap = colorMap;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        TextShowUtils.ShowLog(TAG,"onMeasure");
        if((getMeasuredWidth()>0 && getMeasuredHeight() >0)){
            viewWidth = getMeasuredWidth();
            viewHeight = getMeasuredHeight();
        }
//        TextShowUtils.ShowLog(TAG,"width:"+viewWidth+" height:"+viewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        TextShowUtils.ShowLog(TAG,"onSizeChanged"+" width:"+w+" height:"+h);
        viewWidth = w-(getPaddingLeft()+getPaddingRight());
        viewHeight = h-(getPaddingTop()+getPaddingBottom());
        //这个不设置会不走OnDraw
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //此处getChildCount()=0
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        TextShowUtils.ShowLog(TAG,"onDraw");
        try {
            if (data != null && data.length>0) {

                mMax = isCorrectionLimit ? data.length : mMax;
                float shapeWidth = 0, shapeHeight = 0;
                float marginWidth = 0, marginHeight = 0;
                Path path;
                if (type == Rectangle) {
                    shapeHeight = viewHeight;
                    double d2 = viewWidth / (double) mMax;//every rect width
                    shapeWidth = (float) d2;
                    marginWidth = (viewWidth - shapeWidth * mMax) / 2;
                    marginHeight = 0;
                }
                float startX = (int) marginWidth+getPaddingLeft();
                float startY = (int) marginHeight+getPaddingTop();

                //判断是否使用默认color
                boolean getColor = true;
                if (colorMap == null || colorMap.size() == 0) {
                    getColor = false;
                    paintView.setColor(mContext.getResources().getColor(defaultColor));
                }
                //循环绘制
                for (int i = 0; i < data.length; i++) {
                    if(getColor){
                        int color = getColor(data[i]);
                        paintView.setColor(color);
                    }
                    path = new Path();
                    path.moveTo(startX, startY);//base point
                    path.lineTo(startX + shapeWidth, startY);
                    path.lineTo(startX + shapeWidth, startY+shapeHeight);
                    path.lineTo(startX, startY+shapeHeight);
                    path.close();
                    canvas.drawPath(path, paintView);

                    startX += shapeWidth;
                }
            } else {
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getColor(int color) {
        int colorId= colorMap.get(color) == null? defaultColor:colorMap.get(color);
        return mContext.getResources().getColor(colorId);
    }
}
