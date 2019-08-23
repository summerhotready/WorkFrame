package com.guoxd.workframe.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by guoxd on 2018/10/25.
 */
@TargetApi(11)
public class PointMenuViewUtils {
    final String TAG="PointMenuViewUtils";
    ArrayList<ImageView> imageList;
    int[] sizes;
    int viewSize=0;

    int orientation = LinearLayout.VERTICAL;

    View parentView;//showview的控制

    public boolean isMoveing=false;
    public boolean isOpen=false;

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     *
     * @param size
     * @param parentView
     * @param imageViews
     */
    public void setImageViews(int size, View parentView, ImageView... imageViews){
        this.parentView = parentView;
        if(imageViews.length>0){
            viewSize = imageViews.length;
            imageList = new ArrayList<>();
            sizes = new int[viewSize];
            sizes[0] = 0;
            int count = 0;
            ImageView iv;
            for(int i=0;i<viewSize;i++){
                iv = imageViews[i];
                iv.setVisibility(View.GONE);
                if(orientation == LinearLayout.VERTICAL) {
                    count -= size;
                }else{
                    count+=size;
                }
                sizes[i] = count;

                imageList.add(iv);

            }
        }
    }
    int current=0;
    public void showMenu(){
        Log.d(TAG,"showMenu");
        isMoveing = true;
        isOpen = true;
        current = 0;
        parentView.setVisibility(View.VISIBLE);
        ShowView(imageList.get(current),0,sizes[current]);
    }

    public void closeMenu(){
        Log.d(TAG,"closeMenu");
        isMoveing = true;
        isOpen = false;
        current = viewSize-1;
        CloseView(imageList.get(current),sizes[current],sizes[current-1]);
    }

    private void ShowView(ImageView v,int start_size,int end_size){
        Log.d(TAG,String.format("tag:%d from:%d to:%d",current,start_size,end_size));
        v.setVisibility(View.VISIBLE);
        ObjectAnimator mObjectAnimator= new ObjectAnimator();
        if(orientation == LinearLayout.VERTICAL){//竖直方向
            mObjectAnimator= ObjectAnimator.ofFloat(v, "translationY", start_size, end_size);
        }else{
            mObjectAnimator= ObjectAnimator.ofFloat(v, "translationX", start_size,  end_size);
        }
        mObjectAnimator.setDuration(150);
        mObjectAnimator.start();
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                current ++;
                if(current<viewSize) {
                    ShowView(imageList.get(current), sizes[current-1], sizes[current]);
                }else{
                    isMoveing = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void CloseView(final ImageView v,int start_size,int end_size){
        Log.d(TAG,String.format("tag:%d from:%d to:%d",current,start_size,end_size));
        ObjectAnimator mObjectAnimator= new ObjectAnimator();
        if(orientation == LinearLayout.VERTICAL){//竖直方向
            mObjectAnimator= ObjectAnimator.ofFloat(v, "translationY", start_size, end_size);
        }else{
            mObjectAnimator= ObjectAnimator.ofFloat(v, "translationX", start_size,  end_size);
        }
        mObjectAnimator.setDuration(150);
        mObjectAnimator.start();
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.GONE);
                current --;
                if(current>0) {
                    CloseView(imageList.get(current), sizes[current], sizes[current-1]);
                }else if(current ==0){
                    CloseView(imageList.get(current), sizes[current], 0);
                }else{
                    isMoveing = false;
                    parentView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}
