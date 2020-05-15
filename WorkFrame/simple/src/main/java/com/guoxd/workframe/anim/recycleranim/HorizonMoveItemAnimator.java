package com.guoxd.workframe.anim.recycleranim;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizonMoveItemAnimator extends DefaultItemAnimator {
    //
    ArrayList<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<>();
    private ArrayList<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<>();
    //添加item时调用，通常返回true
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        resetAnimation(holder);//重置动画，这个方法最终指向endAnimation，取消之前执行的动画，同时恢复Item的状态
//        ViewCompat.setAlpha(holder.itemView, 0);
        holder.itemView.setAlpha(0);
//这里我们增加了一个平移动画
        ViewCompat.setTranslationX(holder.itemView,-holder.itemView.getWidth());
        mPendingAdditions.add(holder);
        return true;
    }

    void animateAddImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mAddAnimations.add(holder);
//这里我们增加了一个平移动画
        animation.alpha(1).translationX(0).setDuration(getAddDuration()).setListener(new ViewPropertyAnimatorListener() {
            @Override // 通知动画开始
            public void onAnimationStart(View view) {
                dispatchAddStarting(holder);
            }

            @Override
            public void onAnimationEnd(View view) {
                animation.setListener(null);//取消监听
                dispatchAddFinished(holder);//通知动画结束
                mAddAnimations.remove(holder);// 从动画队列中移除
                dispatchFinishedWhenDone();
            }

            @Override
            public void onAnimationCancel(View view) {
                ViewCompat.setAlpha(view, 1);
            }
        }).start();

    }


    private ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<>();
    ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();
//
@Override
public boolean animateRemove(final RecyclerView.ViewHolder holder) {
    resetAnimation(holder);
    mPendingRemovals.add(holder);
    return true;
}

    private void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mRemoveAnimations.add(holder);
//这里执行了一个淡出动画
        animation.setDuration(getRemoveDuration())
                .alpha(0).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                dispatchRemoveStarting(holder);
            }
            /**
             * 动画结束时...
             *  因为复用布局，所以你将控件删除的时候需要将他还原，要不会出现重复问题ViewCompat.setAlpha(view, 1);
             */
            @Override
            public void onAnimationEnd(View view) {
                animation.setListener(null);
                ViewCompat.setAlpha(view, 1);
                dispatchRemoveFinished(holder);
                mRemoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }


    //从父类中获取的私有变量和方法
    private static TimeInterpolator sDefaultInterpolator;
    private void resetAnimation(RecyclerView.ViewHolder holder) {
        if (sDefaultInterpolator == null) {
            sDefaultInterpolator = new ValueAnimator().getInterpolator();
        }
        holder.itemView.animate().setInterpolator(sDefaultInterpolator);
        endAnimation(holder);
    }
    /**
     * Check the state of currently pending and running animations. If there are none
     * pending/running, call {@link #dispatchAnimationsFinished()} to notify any
     * listeners.
     */
    void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }
}
