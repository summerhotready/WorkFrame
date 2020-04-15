package com.guoxd.work_frame_library.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**图表绘制：直方图
 *
 */
public class HistogramView extends View {
    public HistogramView(Context context){
        super(context);
    }
    public HistogramView(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }
    //size
    int mWidth;
    int mHeight;

    //配置
    Paint mPaint;//画笔
    Rect rectPaint;//绘制区域
    //table title
    String title = "直方图";
    int titleTextSize=16;
    int titleColor = Color.BLACK;
    //table line
    int paddingTable = 20;
    //
    int horizontalLineColor = Color.BLUE;
    //
    String verticalUnit = "单位：万元";
    int verticalLineColor = Color.CYAN;
    //
    int tableLineColor = Color.GREEN;
    //
    //绘制纵坐标
    String[] arrayY = {"2008年","2009年","2010年","2011年","2012年"};
    //绘制横坐标
    int[] array = {0,50,100,150,200,250,300,350,400,450};

    private void init() {
        mPaint = new Paint();
        rectPaint = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        invalidate();
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //标题
        mPaint.setColor(titleColor);
        mPaint.setTextSize(titleTextSize);
        canvas.drawText(title,paddingTable,paddingTable,mPaint);
        //绘制坐标系
        canvas.drawLine(50,100,50,500,mPaint);
        canvas.drawLine(50,500,400,500,mPaint);
        //坐标系单位
        mPaint.setTextSize(10);
        canvas.drawText(verticalUnit,paddingTable,90,mPaint);

        mPaint.setColor(horizontalLineColor);
        for(int i=0;i<array.length;i++){
            canvas.drawLine(50,500-array[i],54,500-array[i],mPaint);
            canvas.drawText(array[i]+"",paddingTable,500-array[i],mPaint);
        }

        mPaint.setColor(verticalLineColor);
        for(int i=0;i<arrayY.length;i++){
            canvas.drawLine(50,500-array[i],54,500-array[i],mPaint);
            canvas.drawText(arrayY[i]+"",array[i]+80,520,mPaint);
        }
        //绘制图
        mPaint.setColor(tableLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        rectPaint.set(90,500-56,110,500);
        canvas.drawRect(rectPaint,mPaint);
        mPaint.setColor(Color.CYAN);
        rectPaint.set(140,500-98,160,500);
        canvas.drawRect(rectPaint,mPaint);
        mPaint.setColor(Color.YELLOW);
        rectPaint.set(190,500-207,210,500);
        canvas.drawRect(rectPaint,mPaint);
        mPaint.setColor(Color.CYAN);
        rectPaint.set(240,500-318,260,500);
        canvas.drawRect(rectPaint,mPaint);
        //
        mPaint.setColor(Color.BLACK);
        canvas.drawText("56.32",88,500-58,mPaint);
        canvas.drawText("90.00",138,500-100,mPaint);
        canvas.drawText("207.67",188,500-209,mPaint);
        canvas.drawText("318.56",238,500-320,mPaint);
//        mPaint.setColor(Color.RED);
    }

}
