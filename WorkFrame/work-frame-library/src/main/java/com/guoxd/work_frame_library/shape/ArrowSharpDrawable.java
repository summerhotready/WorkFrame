package com.guoxd.work_frame_library.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;


/**
 * copy自sharpview，对箭头部分进行优化修改
 * 原本图形只能绘制正三角形，
 */
public class ArrowSharpDrawable extends GradientDrawable {
    public ArrowSharpDrawable(ArrowDirection arrowDirection) {
        super(Orientation.LEFT_RIGHT, null);//GradientDrawable方向
        setArrowDirection(arrowDirection);
        init();
    }
    public ArrowSharpDrawable(Orientation orientation, @ColorInt int[] colors) {
        super(orientation, colors);//GradientDrawable方向
        init();
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
        super.setColor(bgColor);
    }

    public void setCornerRadius(float cornerRadius) {
        mCornerRadius = cornerRadius;
        super.setCornerRadius(cornerRadius);
    }

    public void setArrowDirection(ArrowDirection arrowDirection) {
        mArrowDirection = arrowDirection;
    }

    public void setRelativePosition(float relativePosition) {
        mRelativePosition = Math.min(Math.max(relativePosition, 0), 1);
    }

    public void setArrowPath(Path mArrowPath) {
        this.mArrowPath = mArrowPath;
    }

    public void setBorder(float border) {
        mBorder = border;
        super.setStroke((int) mBorder, mBorderColor);
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        super.setStroke((int) mBorder, mBorderColor);
    }

    public void setSharpSize(float w,float sharpSize) {
        mArrowWidth = w;
        mArrowHeight = sharpSize;
    }
//    三角形部分
    //箭头方向
    private ArrowDirection mArrowDirection = ArrowDirection.BOTTOM;
    /**三角位置 from 0 to 1     */
    private float mRelativePosition;
//三角尺寸（高度）
    private float mArrowWidth,mArrowHeight;
    //坐标系用于存储三角形的三个点的坐标
    private PointF arrowPointF;//三角形顶点坐标
    private PointF[] mPointFs;//0为三角的顶点坐标
//    path方式引入三角形
    private Path mArrowPath;//直接使用path的方式设置
//    private Shape mArrowShape;//


    //设置背景色
    private int mBgColor;
//圆角
    private float mCornerRadius;

//设置边和颜色
    private float mBorder;
    private int mBorderColor;
    //矩形部分的面积
    private RectF mRect;

    void setPaint(Paint paint) {
        mPaint = paint;
    }

    private Paint mPaint;


    private Path mPath;



    ArrowSharpDrawable() {
        super();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mRect = new RectF();
        mPointFs = new PointF[3];
        mPath = new Path();
        arrowPointF = new PointF();
        mPointFs[0] = new PointF();
        mPointFs[1] = new PointF();
        mPointFs[2] = new PointF();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mArrowHeight == 0) {
            super.draw(canvas);
        } else {
            Rect bounds = getBounds();//获取图形边距
            int left = bounds.left;
            int top = bounds.top;
            int right = bounds.right;
            int bottom = bounds.bottom;

            float pointX;
            if(mArrowDirection == ArrowDirection.LEFT || mArrowDirection == ArrowDirection.RIGHT) {
                if(mArrowPath==null) {//default
                    mArrowWidth = bottom / 10.0f;
                }
                pointX = Math.max(bottom*mRelativePosition,mArrowWidth*0.5f);
                pointX = Math.min(pointX,bottom-mArrowWidth*0.5f);
            }else{
                if(mArrowPath==null) {//default
                    mArrowWidth = right / 10.0f;
                }
                pointX = Math.max(right*mRelativePosition,mArrowWidth*0.5f+mCornerRadius);
                pointX = Math.min(pointX,right-(mArrowWidth*0.5f+mCornerRadius));
            }


            float length = pointX-mArrowWidth*0.5f;//三角的第一笔位置

            switch (mArrowDirection) {
                case LEFT:
                    left += mArrowHeight;//向右错位，流出角的位置
                    arrowPointF.set(bounds.left,length);
                    mRect.set(left, top, right, bottom);

                    break;
                case TOP:
                    top += mArrowHeight;
                    arrowPointF.set(length, bounds.top);
                    mRect.set(left, top, right, bottom);//重新定义矩形部分的尺寸，用于绘制背景和圆角

                    break;
                case RIGHT:
                    right -= mArrowHeight;
                    arrowPointF.set(right, length);
                    mRect.set(left, top, right, bottom);
                    break;
                case BOTTOM:
                    bottom -= mArrowHeight;
                    arrowPointF.set(length, bottom);//.set(bounds.left + length - mArrowWidth/2.0f, bounds.bottom);
                    mRect.set(left, top, right, bottom);
                    break;
            }
            if(mArrowPath==null) {
                setArrowDefault();
            }
//装填
            mPath.reset();
//          主体矩形
            mPath.addRoundRect(mRect, mCornerRadius, mCornerRadius, Path.Direction.CW);
            if(mArrowPath!=null && !mArrowPath.isEmpty()) {
                mPath.addPath(mArrowPath,arrowPointF.x, arrowPointF.y);
            }
          /*  else {
//          三角形
                mPath.moveTo(mPointFs[0].x, mPointFs[0].y);
                mPath.lineTo(mPointFs[1].x, mPointFs[1].y);
                mPath.lineTo(mPointFs[2].x, mPointFs[2].y);
                mPath.lineTo(mPointFs[0].x, mPointFs[0].y);
            }*/
//            画笔绘制
            mPaint.setColor(mBgColor);
            canvas.drawPath(mPath, mPaint);
        }
    }
    private void setArrowDefault(){
        Path arrowPath = new Path();
        float half = mArrowWidth/2.0f;
        switch (mArrowDirection) {
            case LEFT:
                arrowPath.moveTo(0,half);
                arrowPath.lineTo(mArrowHeight,0);
                arrowPath.lineTo(mArrowHeight,mArrowWidth);
                arrowPath.lineTo(0,half);
                break;
            case TOP:
                arrowPath.moveTo(half,0);
                arrowPath.lineTo(0,mArrowHeight);
                arrowPath.lineTo(mArrowWidth,mArrowHeight);
                arrowPath.lineTo(half,0);
//                top += mArrowHeight;
//                mPointFs[0].set(bounds.left + length, bounds.top);
//                mPointFs[1].set(mPointFs[0].x - mArrowHeight, top);
//                mPointFs[2].set(mPointFs[0].x + mArrowHeight, top);
//                mRect.set(left, top, right, bottom);//重新定义矩形部分的尺寸，用于绘制背景和圆角
                break;
            case RIGHT:
                arrowPath.moveTo(mArrowHeight,half);
                arrowPath.lineTo(0,0);
                arrowPath.lineTo(0,mArrowWidth);
                arrowPath.lineTo(mArrowHeight,half);

//                mPointFs[0].set(bounds.right, length + bounds.top);
//                mPointFs[1].set(right, mPointFs[0].y - mArrowHeight);
//                mPointFs[2].set(right, mPointFs[0].y + mArrowHeight);
//                mRect.set(left, top, right, bottom);
                break;
            case BOTTOM:
                arrowPath.moveTo(half,mArrowHeight);//顶点
                arrowPath.lineTo(0,0);
                arrowPath.lineTo(mArrowWidth,0);
                arrowPath.lineTo(half,mArrowHeight);

//                mPointFs[0].set(bounds.left + length, bounds.bottom);
//                mPointFs[1].set(mPointFs[0].x - mArrowHeight, bottom);
//                mPointFs[2].set(mPointFs[0].x + mArrowHeight, bottom);

                break;
        }
        arrowPath.close();
        mArrowPath = arrowPath;
    }

//    arrow path
public void setArrowPath(float width,float height){
        setArrowPath(mArrowDirection,width,height);
}
    public void setArrowPath(ArrowDirection direction,float width,float height){
        Path arrowPath = new Path();
        mArrowDirection = direction;
        mArrowWidth = width;
        mArrowHeight = height;
        float half = width/2.0f;
        switch (direction){
            case TOP:
                arrowPath.moveTo(half,0);
                arrowPath.lineTo(0,height);
                arrowPath.lineTo(width,height);
                arrowPath.lineTo(half,0);
                break;
            case LEFT:
                arrowPath.moveTo(0,half);
                arrowPath.lineTo(height,0);
                arrowPath.lineTo(height,width);
                arrowPath.lineTo(0,half);
                break;
            case RIGHT:
                arrowPath.moveTo(height,half);
                arrowPath.lineTo(0,0);
                arrowPath.lineTo(0,width);
                arrowPath.lineTo(height,half);
                break;
            case BOTTOM:
                arrowPath.moveTo(half,height);//顶点
                arrowPath.lineTo(0,0);
                arrowPath.lineTo(width,0);
                arrowPath.lineTo(half,height);
                break;
            default:
                break;
        }
        arrowPath.close();
        mArrowPath = arrowPath;
    }
    public void setArrowCirclePath(float pointX,float pointY,float width,float height) {
        setArrowCirclePath(mArrowDirection,pointX,pointY,width,height);
    }

    /**
     *
     * @param direction
     * @param pointX 转折点距离中点的宽度
     * @param pointY 转折点距离起点轴的高度
     * @param width
     * @param height
     */
    public void setArrowCirclePath(ArrowDirection direction,float pointX,float pointY,float width,float height) {
        Path arrowPath = new Path();
        mArrowDirection = direction;
        mArrowWidth = width;
        mArrowHeight = height;
        float half = width/2.0f;
        if(pointX>=half){pointX = width/3.0f;}
        if(pointY>height){pointY = height/2.0f;}
        switch (direction){
            case TOP:
                arrowPath.moveTo(half,0);
                arrowPath.quadTo(half-pointX,pointY,0,height);
                arrowPath.lineTo(width,height);
                arrowPath.quadTo(half-pointX,pointY,half,0);
                break;
            case LEFT:
                arrowPath.moveTo(0,half);//三角顶点
                arrowPath.quadTo(pointY,half-pointX,height,0);
                arrowPath.lineTo(height,width);
                arrowPath.quadTo(pointY,half+pointX,0,half);

                break;
            case RIGHT:
                arrowPath.moveTo(height,half);//最低点
                arrowPath.quadTo(pointY,half-pointX,0,0);
                arrowPath.lineTo(0,width);
                arrowPath.quadTo(pointY,half+pointX,height,half);
                break;
            case BOTTOM:
                arrowPath.moveTo(half,height); //最低点
                arrowPath.quadTo(half-pointX,pointY,0,0);
                arrowPath.lineTo(width,0);
                arrowPath.quadTo(half+pointX,pointY,half,height);

                break;
            default:
                break;
        }
        arrowPath.close();
        mArrowPath = arrowPath;
    }
   public enum ArrowDirection {
        LEFT, TOP, RIGHT, BOTTOM
    }
}

