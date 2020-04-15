package com.guoxd.work_frame_library.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guoxd.work_frame_library.R;
import com.guoxd.work_frame_library.utils.ViewControlUtils;


/**
 * 仿华为loadingView
 * 12个点，7个颜色规格相同，5个颜色规格递增
 * 地址：https://www.jianshu.com/p/4732d8ae61fe
 */
public class CircleLoadingHWView extends View {
    //小圆总数
    private final int CIRCLE_COUNT = 12;
    //小圆圆心之间间隔角度差
    private final int DEGREE_PER_CIRCLE = 360 / CIRCLE_COUNT;
    //记录所有小圆半径
    private float[] mWholeCircleRadius = new float[CIRCLE_COUNT];
    //记录所有小圆透明度
    private int[] mWholeCircleColors = new int[CIRCLE_COUNT];
    //小圆最大半径
    private float mMaxCircleRadius;


    private int mSize;     //控件大小
    private int mColor;    //小圆颜色
    private ValueAnimator mAnimator;
    private int mAnimateValue = 0;
    //动画时长
    private long mDuration;
    private int defaultColor = Color.parseColor("#878787");

    private Paint mPaint; //画笔

    public CircleLoadingHWView(Context context){
        this(context,null);
    }
    public CircleLoadingHWView(Context context, @Nullable AttributeSet attrs){
        this(context,attrs,0);
    }
    public CircleLoadingHWView(Context context, @Nullable AttributeSet attrs,int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.CircleLoadingHWView);

        mSize = (int)typedArray.getDimension(R.styleable.CircleLoadingHWView_view_size, ViewControlUtils.dp2px(context,100));
        mColor = typedArray.getColor(R.styleable.CircleLoadingHWView_view_color, defaultColor);
        mDuration = typedArray.getIndex(R.styleable.CircleLoadingHWView_circle_duration);
        typedArray.recycle();
        init();
        initValue();
    }
    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);//set Color
    }
//计算原点位置和颜色
    private void initValue(){
        float minCircleRadius = mSize / 24;
        for (int i = 0; i < CIRCLE_COUNT; i++) {
            switch (i) {
                case 7:
                    mWholeCircleRadius[i] = minCircleRadius * 1.25f;
                    mWholeCircleColors[i] = (int) (255 * 0.7f);
                    break;
                case 8:
                    mWholeCircleRadius[i] = minCircleRadius * 1.5f;
                    mWholeCircleColors[i] = (int) (255 * 0.8f);
                    break;
                case 9://10&12 is same
                case 11:
                    mWholeCircleRadius[i] = minCircleRadius * 1.75f;
                    mWholeCircleColors[i] = (int) (255 * 0.9f);
                    break;
                case 10://the biggest
                    mWholeCircleRadius[i] = minCircleRadius * 2f;
                    mWholeCircleColors[i] = 255;
                    break;
                default:
                    mWholeCircleRadius[i] = minCircleRadius;
                    mWholeCircleColors[i] = (int) (255 * 0.5f);
                    break;
            }
        }
        mMaxCircleRadius = minCircleRadius * 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //去除 wrap_content match_Parent对控件的影响
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("CircleLoading","onDraw mAnimateValue:"+mAnimateValue);
        if(mSize>0){//允许绘制
            //围绕控件中心点旋转DEGREE_PER_CIRCLE * mAnimateValue度，在此处开始绘制所有小圆
            canvas.rotate(DEGREE_PER_CIRCLE * mAnimateValue, mSize / 2, mSize / 2);
            for (int i = 0; i < CIRCLE_COUNT; i++) {
                //设置小圆透明度
                mPaint.setAlpha(mWholeCircleColors[i]);
                //每隔DEGREE_PER_CIRCLE角度，绘制一个小圆
                canvas.drawCircle(mSize / 2, mMaxCircleRadius, mWholeCircleRadius[i], mPaint);
                //围绕控件中心旋转DEGREE_PER_CIRCLE度，是下一个待绘制小圆的位置
                canvas.rotate(DEGREE_PER_CIRCLE, mSize / 2, mSize / 2);
            }
        }
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
        invalidate();
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        invalidate();
    }

    /**
     * 动画监听
     */
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mAnimateValue = (int) animation.getAnimatedValue();
            Log.i("CircleLoading","AnimatorUpdateListener mAnimateValue:"+mAnimateValue);
            invalidate();
        }
    };

    /**
     * 开始动画
     */
    public void start() {
        Log.i("CircleLoading","start Animator");
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, CIRCLE_COUNT - 1);
            mAnimator.addUpdateListener(mUpdateListener);
            mAnimator.setDuration(mDuration);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        } else if (!mAnimator.isStarted()) {
            mAnimator.start();
        }
    }

    /**
     * 停止动画
     */
    public void stop() {
        Log.i("CircleLoading","stop Animator");
        if (mAnimator != null) {
            mAnimator.removeUpdateListener(mUpdateListener);
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    /**
     * View依附Window时开始动画
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    /**
     * View脱离Window时停止动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 根据View可见性变化开始/停止动画
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

}
