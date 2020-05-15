package com.guoxd.work_frame_library.views.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.guoxd.work_frame_library.utils.ColorUtils;

/**
 * @author iron
 *         created at 2018/7/16
 */
public class DashboardView extends BaseDashboardView {

    private Paint mPaint;
    //外环画笔
    private Paint mPaintOuterArc;
    //内环画笔
    private Paint mPaintInnerArc;
    //进度点画笔
    private Paint mPaintProgressPoint;
    //指示器画笔
    private Paint mPaintIndicator;

    //外环区域
    private RectF mRectOuterArc;
    //外环画笔颜色
    private int mOuterArcColor;
    private int mProgressOuterArcColor;
    private float mOuterArcWidth;

    //内环区域
    private RectF mRectInnerArc;
    //内环画笔颜色
    private int mInnerArcColor;
    private int mProgressInnerArcColor;
    //内环宽度
    private float mInnerArcWidth ;

    //内外环之间的间距
    private float mArcSpacing ;

    //指示器和进度点颜色
    float mIndicatorWidth;
    private int mProgressPointColor;
    private float mProgressPointRadius ;

    //进度条的圆点属性
    private float[] mProgressPointPosition;



    private int mIndicatorColor;
    //指标器的Path
    private Path mIndicatorPath;
    //指示器的起始位置
    private float mIndicatorStart;

    private int  ringColor = Color.BLUE;


    float mValueTextSize;
    float mTopTextSize;
    float mBottomTextSize;


    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRingColor(@ColorInt int ringColor) {
        this.ringColor = ringColor;
        initColor();
        postInvalidate();
    }

    private void initColor(){
        mOuterArcColor =  ColorUtils.getColorWithAlpha(0.3f,ringColor);//DEFAULT_OUTER_ARC_COLOR;
        mProgressOuterArcColor = ColorUtils.getColorWithAlpha(0.9f,ringColor);
        //
        mInnerArcColor = ColorUtils.getColorWithAlpha(0.3f,ringColor);
        mProgressInnerArcColor = ColorUtils.getColorWithAlpha(0.9f,ringColor);
        //
        mProgressPointColor = ringColor;
        mIndicatorColor = ringColor;
        //yanse
        mValueTextColor = ringColor;
        mValueLevelColor =  ColorUtils.getColorWithAlpha(0.7f,ringColor);;
        mValueDateColor =  ColorUtils.getColorWithAlpha(0.7f,ringColor);;

    }
    /**
     * 初始化界面
     */
    @Override
    protected void initView() {
        //颜色
       initColor();

        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //外环画笔
        mPaintOuterArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOuterArc.setStrokeWidth(mOuterArcWidth);
        mPaintOuterArc.setStyle(Paint.Style.STROKE);
        mOuterArcWidth = dp2px(10f);

        mArcSpacing = dp2px(20f);

        //内环画笔
        mPaintInnerArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerArcWidth = dp2px(2f);

        //进度点画笔
        mPaintProgressPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPointRadius = dp2px(10f);

        //指示器画笔
        mPaintIndicator = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorWidth = dp2px(10);

        //进度点的图片
        mProgressPointPosition = new float[2];

        //文字
        mValueTextSize = dp2px(60f);
        mTopTextSize = dp2px(10f);
        mBottomTextSize = dp2px(30f);

    }

    /**
     * 初始化圆环区域
     */
    @Override
    protected void initArcRect(float left, float top, float right, float bottom) {
        //外环区域
        mRectOuterArc = new RectF(left, top, right, bottom);

        initInnerRect();
    }

    /**
     * 初始化内部的指示器区域，和指针路径
     */
    private void initInnerRect() {
        //内环位置 正方形
        mRectInnerArc = new RectF(mRectOuterArc.left + mArcSpacing,mRectOuterArc.top + mArcSpacing,
                mRectOuterArc.right - mArcSpacing , mRectOuterArc.bottom - mArcSpacing);
        //指标器的路径
        mIndicatorStart = mRectInnerArc.top + mArcSpacing / 2;
       /* mIndicatorPath = new Path();
        mIndicatorPath.moveTo(mRadius, mIndicatorStart);
        mIndicatorPath.rLineTo(-mIndicatorWidth, 2*mIndicatorWidth);
        mIndicatorPath.rLineTo(2*mIndicatorWidth, 0);
        mIndicatorPath.close();*/
        //指标器的路径
        int height = (int)((mRectInnerArc.bottom-mRectInnerArc.top)*0.5);
        int height_up = (int)(height*0.7);
        int height_down = (int)(height*0.15);
        mIndicatorPath = new Path();
        mIndicatorPath.moveTo(mRadius, mIndicatorStart);
        mIndicatorPath.rLineTo(-mIndicatorWidth, height_up);
        mIndicatorPath.rLineTo(mIndicatorWidth,height_down);
        mIndicatorPath.rLineTo(mIndicatorWidth,-height_down);
        mIndicatorPath.rLineTo(-mIndicatorWidth, -height_up);
        mIndicatorPath.close();
    }


    /**
     * 绘制圆环
     */
    @Override
    protected void drawArc(Canvas canvas, float arcStartAngle, float arcSweepAngle) {
        //绘制圆环
        mPaintOuterArc.setColor(mOuterArcColor);
        mPaintOuterArc.setStrokeWidth(mOuterArcWidth);
        canvas.drawArc(mRectOuterArc, arcStartAngle, arcSweepAngle, false, mPaintOuterArc);

        //绘制内环
        mPaintInnerArc.setStrokeWidth(mInnerArcWidth);
        mPaintInnerArc.setStyle(Paint.Style.STROKE);
        mPaintInnerArc.setStrokeCap(Paint.Cap.ROUND);
        PathEffect mPathEffect = new DashPathEffect(new float[] { 10, 10 }, 0);
        mPaintInnerArc.setPathEffect(mPathEffect);
        mPaintInnerArc.setColor(mInnerArcColor);
        canvas.drawArc(mRectInnerArc, arcStartAngle, arcSweepAngle, false, mPaintInnerArc);
    }

    /**
     * 绘制进度圆环
     */
    @Override
    protected void drawProgressArc(Canvas canvas, float arcStartAngle, float progressSweepAngle) {
        //绘制进度点
        if(progressSweepAngle == 0) {
            return;
        }
        Path path = new Path();
        //添加进度圆环的区域
        path.addArc(mRectOuterArc, arcStartAngle, progressSweepAngle);
        //计算切线值和为重
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength(), mProgressPointPosition, null);
        //绘制圆环
        mPaintOuterArc.setColor(mProgressOuterArcColor);
        canvas.drawPath(path, mPaintOuterArc);
        //绘制进度点
        mPaintProgressPoint.setStyle(Paint.Style.FILL);
        mPaintProgressPoint.setColor(mProgressPointColor);
        if(mProgressPointPosition[0] != 0 && mProgressPointPosition[1] != 0) {
            canvas.drawCircle(mProgressPointPosition[0], mProgressPointPosition[1], mProgressPointRadius, mPaintProgressPoint);
        }

        //绘制内环
        mPaintInnerArc.setColor(mProgressInnerArcColor);
        canvas.drawArc(mRectInnerArc, arcStartAngle, progressSweepAngle, false, mPaintInnerArc);

        //绘制指示器
        canvas.save();
        canvas.rotate(arcStartAngle + progressSweepAngle - 270, mRadius, mRadius);
        drawIndicator(canvas);
        canvas.restore();
    }
    private void drawIndicator(Canvas canvas){
        //绘制指针 路径三角形+圆圈
        mPaintIndicator.setStrokeCap(Paint.Cap.SQUARE);
        mPaintIndicator.setStrokeWidth(mIndicatorWidth);
        mPaintIndicator.setColor(mIndicatorColor);
        mPaintIndicator.setStyle(Paint.Style.FILL);
//        canvas.drawPath(mIndicatorPath, mPaintIndicator);
//        mPaintIndicator.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(mRadius, (mIndicatorStart + 3*mIndicatorWidth+ 1), mIndicatorWidth, mPaintIndicator);

        //绘制指示器指针
        canvas.drawPath(mIndicatorPath, mPaintIndicator);
    }

    /**
     * 绘制文字
     */
    @Override
    protected void drawText(Canvas canvas, int value, String valueLevel, String currentTime) {
        mPaint.setTextAlign(Paint.Align.CENTER);
        float marginTop = mRadius + mTextSpacing;
        //绘制数值
//        mPaint.setTextSize(mValueTextSize);
//        mPaint.setColor(mValueTextColor);
//        canvas.drawText(String.valueOf(value), mRadius, marginTop, mPaint);

        //绘制数值文字信息
        if(!TextUtils.isEmpty(valueLevel)){
            mPaint.setTextSize(mTopTextSize);
            mPaint.setColor(mValueLevelColor);
            float margin = mRadius - mTextSpacing - getPaintHeight(mPaint, "9");
            canvas.drawText(valueLevel, mRadius, margin, mPaint);
        }

        //绘制日期
        if(!TextUtils.isEmpty(currentTime)) {
//            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(mBottomTextSize);
            mPaint.setColor(mValueDateColor);
            marginTop = marginTop + getPaintHeight(mPaint, currentTime) + mTextSpacing;
            canvas.drawText(String.valueOf(value), mRadius, marginTop, mPaint);
        }
    }

    /**
     * 设置圆环的距离
     */
    public void setArcSpacing(float dpSize){
        mArcSpacing = dp2px(dpSize);

        initInnerRect();

        postInvalidate();
    }

    /**
     * 设置外环颜色
     */
    public void setOuterArcPaint(float dpSize, @ColorInt int color){
        mPaintOuterArc.setStrokeWidth(dp2px(dpSize));
        mOuterArcColor = color;

        postInvalidate();
    }

    /**
     * 设置进度条的颜色
     */
    public void setProgressOuterArcColor(@ColorInt int color){
        mProgressOuterArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环的属性
     */
    public void setInnerArcPaint(float dpSize, @ColorInt int color){
        mPaintInnerArc.setStrokeWidth(dp2px(dpSize));
        mInnerArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环的属性
     */
    public void setProgressInnerArcPaint(@ColorInt int color){
        mProgressInnerArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环实线和虚线状态
     */
    public void setInnerArcPathEffect(float[] intervals){
        PathEffect mPathEffect = new DashPathEffect(intervals, 0);
        mPaintInnerArc.setPathEffect(mPathEffect);

        postInvalidate();
    }

    /**
     * 设置进度圆点的属性
     */
    public void setProgressPointPaint(float dpRadiusSize,@ColorInt int color){
        mProgressPointRadius = dp2px(dpRadiusSize);
        mPaintProgressPoint.setColor(color);

        postInvalidate();
    }

    /**
     * 设置指示器属性
     */
    public void setIndicatorPaint(@ColorInt int color){
        mPaintIndicator.setColor(color);

        postInvalidate();
    }
}
