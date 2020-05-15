package com.guoxd.workframe.anim.recycleranim;

import android.annotation.TargetApi;
import android.util.Log;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

@TargetApi(11)
public class HorizonInsertItemAnimator extends BaseItemAnimator {
    String TAG = "MyAnimationDemo";

    @Override
    protected void animateRemoveImpl(RecyclerView.ViewHolder holder) {

        Log.i(TAG,String.format("%d",holder.itemView.getRootView().getWidth()));
        ViewCompat.animate(holder.itemView)
                //完成态是移除
                .translationX((holder.itemView.getRootView().getWidth()))
                .setDuration(getRemoveDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultRemoveVpaListener(holder))
                .setStartDelay(getRemoveDelay(holder))
                .start();
    }
    //先执行此动画在执行animateAddImpl
    /**动画方案：从右侧进入preAnimateAddImpl设置setTranslationX(-holder.itemView.getRootView().getWidth());animateAddImpl设置 .translationX(0)
     *
     * @param holder
     */
    @Override
    protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        //从右侧进入
//        holder.itemView.setTranslationX(-holder.itemView.getRootView().getWidth());
        //从左侧进入
        holder.itemView.setTranslationX(holder.itemView.getRootView().getWidth());
        holder.itemView.setAlpha(0);
    }

    //anim结束状态
    @Override
    protected void animateAddImpl(RecyclerView.ViewHolder holder) {
        Log.i(TAG,String.format("%d,%d",holder.itemView.getRootView().getWidth(),holder.itemView.getWidth()));
        ViewCompat.animate(holder.itemView)
                .translationX(0)
                .alpha(1)
                .setDuration(getAddDuration())
                .setStartDelay(getRemoveDelay(holder))
                .setListener(new DefaultAddVpaListener(holder))
                .start();
    }

    @Override
    protected long getRemoveDelay(RecyclerView.ViewHolder holder) {
        return super.getRemoveDelay(holder);
    }
}