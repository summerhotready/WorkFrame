package com.guoxd.work_frame_library.views.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wensefu on 17-3-21. update by emma on 2021-2-22
 * 这是一个画板，具有橡皮擦功能
 * 本质上是使用mDrawingList 缓存了一系列Paint对象，缺点是随着图形复杂度高创建的Paint变多
 */
public class PaletteView extends View {

    private Paint mPaint;
    private Path mPath;
    private float mLastX;
    private float mLastY;
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;

//    绘制和回退记录的步数
    private static final int MAX_CACHE_STEP = 20;
    private int cacheStep = MAX_CACHE_STEP;

    public int getCacheStep() {
        return cacheStep;
    }

    public void setCacheStep(int cacheStep) {
        this.cacheStep = cacheStep;
    }

    private List<DrawingInfo> mDrawingList;//绘画列表，每一笔画都成为一个独立path，方便回退
    private List<DrawingInfo> mRemovedList;//撤销列表

    private Xfermode mClearMode;
    private float mDrawSize;
    private float mEraserSize;

    private boolean mCanEraser;

    private Callback mCallback;

    public enum Mode {
        DRAW,
        ERASER
    }

    private Mode mMode = Mode.DRAW;

    public PaletteView(Context context) {
        this(context,null);
    }

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        init();
    }

    public interface Callback {
        void onUndoRedoStatusChanged();
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawSize = 20;
        mEraserSize = 40;
        mPaint.setStrokeWidth(mDrawSize);
        mPaint.setColor(0XFF000000);

        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    private void initBuffer(){
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    private abstract static class DrawingInfo {
        Paint paint;
        abstract void draw(Canvas canvas);
    }

    private static class PathDrawingInfo extends DrawingInfo{

        Path path;

        @Override
        void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {//设置模式
        if (mode != mMode) {
            mMode = mode;
            if (mMode == Mode.DRAW) {//绘画
                mPaint.setXfermode(null);
                mPaint.setStrokeWidth(mDrawSize);
            } else {//清除
                mPaint.setXfermode(mClearMode);
                mPaint.setStrokeWidth(mEraserSize);
            }
        }
    }

    public void setEraserSize(float size) {
        mEraserSize = size;
    }

    public void setPenRawSize(float size) {
        mDrawSize = size;
    }

    public void setPenColor(int color) {
        mPaint.setColor(color);
    }

    public void setPenAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    private void reDraw(){
        if (mDrawingList != null) {
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (DrawingInfo drawingInfo : mDrawingList) {
                drawingInfo.draw(mBufferCanvas);
            }
            invalidate();
        }
    }

    public boolean canRedo() {
        return mRemovedList != null && mRemovedList.size() > 0;
    }

    public boolean canUndo(){
        return mDrawingList != null && mDrawingList.size() > 0;
    }

    public void redo() {//重做
        int size = mRemovedList == null ? 0 : mRemovedList.size();
        if (size > 0) {
            DrawingInfo info = mRemovedList.remove(size - 1);
            mDrawingList.add(info);
            mCanEraser = true;
            reDraw();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
    }

    public void undo() {//撤销
        int size = mDrawingList == null ? 0 : mDrawingList.size();
        if (size > 0) {
            DrawingInfo info = mDrawingList.remove(size - 1);
            if (mRemovedList == null) {
                mRemovedList = new ArrayList<>(getCacheStep());
            }
            if (size == 1) {
                mCanEraser = false;
            }
            mRemovedList.add(info);
            reDraw();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }
    }

    public void clear() {//清空
        if (mBufferBitmap != null) {
            if (mDrawingList != null) {
                mDrawingList.clear();
            }
            if (mRemovedList != null) {
                mRemovedList.clear();
            }
            mCanEraser = false;
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            if (mCallback != null) {
                mCallback.onUndoRedoStatusChanged();
            }
        }else{
            Log.i("","bitmap is null");
        }
    }

    public Bitmap buildBitmap() {
        Bitmap bm = getDrawingCache();
        Bitmap result = Bitmap.createBitmap(bm);
        destroyDrawingCache();//释放
        return result;
    }

    /**
     * 绘制path结束，将缓存存入sheng'shu
     */
    private void saveDrawingPath(){//将当前缓存的mPath存入PathDrawingInfo，将这一条路经加入mDrawingList
        if (mDrawingList == null) {
            mDrawingList = new ArrayList<>(getCacheStep());
        } else if (mDrawingList.size() == getCacheStep()) {
            mDrawingList.remove(0);
        }
        Path cachePath = new Path(mPath);
        Paint cachePaint = new Paint(mPaint);
        PathDrawingInfo info = new PathDrawingInfo();
        info.path = cachePath;
        info.paint = cachePaint;
        mDrawingList.add(info);
        mCanEraser = true;
        if (mCallback != null) {
            mCallback.onUndoRedoStatusChanged();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        final float x = event.getX();
        final float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                //这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                if (mBufferBitmap == null) {
                    initBuffer();
                }
                if (mMode == Mode.ERASER && !mCanEraser) {
                    break;
                }
                mBufferCanvas.drawPath(mPath,mPaint);
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mMode == Mode.DRAW || mCanEraser) {
                    saveDrawingPath();
                }
                mPath.reset();
                break;
        }
        return true;
    }
}
