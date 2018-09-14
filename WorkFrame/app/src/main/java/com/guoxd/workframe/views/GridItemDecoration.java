package com.guoxd.workframe.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by guoxd on 2018/8/27.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mOrientation = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int[] attrs = new int[]{android.R.attr.listDivider};

    public GridItemDecoration(Context context, int orientation) {
        // 引用系统属性样式
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    /**
     * 设置参数枚举类型 水平 or 垂直
     * @param orientation
     */
    public void setOrientation(int orientation) {
        // 避免传入非法类型
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("非法参数枚举类型");
        }
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        // 偏移量 代表左上右下分别对应上一个的偏移值
        int right = mDivider.getIntrinsicWidth();
        int bottom = mDivider.getIntrinsicHeight();
        if (isLastRow(parent)) { // 代表当前是最后一行 不绘制底部
            bottom = 0;
        }
        if (isLastCol(itemPosition, parent)) { // 代表当前是最后一列 不绘制右侧
            right = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    /**
     * 是否是最后一行
     *
     * @return
     */
    private boolean isLastRow(RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int childCount = parent.getAdapter().getItemCount();
            int lastRowCount = childCount % spanCount;
            if (lastRowCount == 0 || lastRowCount < spanCount) { // 最后一行情况：1)被整除 2)小于列数
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是最后一列
     *
     * @return
     */
    private boolean isLastCol(int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 拿到当前有多少列
            int spanCount = getSpanCount(parent);
            if ((itemPosition + 1) % spanCount == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取列数
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            // 拿到当前有多少列
            int spanCount = gridLayoutManager.getSpanCount();
            return spanCount;
        }
        return 0;
    }

    /**
     * 2. RecycleView回调此函数 开发者需自己实现绘制分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) { // 垂直
            drawVertical(c, parent);
        } else { // 水平
            drawHorizontal(c, parent);
        }
        super.onDraw(c, parent, state);
    }

    /**
     * 绘制水平分割线
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        // 考虑父容器padding值
        int left = parent.getPaddingLeft();
        // 绘制右侧时需要减去右侧padding值
        int right = parent.getWidth() - parent.getPaddingRight();
        // 获取父容器下子控件个数 也就是item个数
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            // 高度等于子控件底部加margin加y轴值
            int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            // 底部等于高度加当前实际高度
            int bottom = top + mDivider.getIntrinsicHeight();
            // 设置绘制位置
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            int right = left + mDivider.getIntrinsicHeight();
            // 设置绘制位置
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}