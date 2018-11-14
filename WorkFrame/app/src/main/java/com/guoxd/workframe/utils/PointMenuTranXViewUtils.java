package com.guoxd.workframe.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by guoxd on 2018/10/25.
 */

public class PointMenuTranXViewUtils {
    final String TAG="PointMenuTranXViewUtils";
    ArrayList<ImageView> imageList;
    int[] sizes;
    int viewSize=0;

    public boolean isMoveing=false;
    public boolean isOpen=false;

    public void setImageViews(int size, ImageView... imageViews){

        if(imageViews.length>0){
            viewSize = imageViews.length;
            imageList = new ArrayList<>();
            sizes = new int[viewSize];
            sizes[0] = 0;
            int count = 0;
            ImageView iv;
            for(int i=0;i<viewSize;i++){
                iv = imageViews[i];
                if(i>0){
                    iv.setVisibility(View.GONE);
                    sizes[i] =count;
                }
                imageList.add(iv);

                count-=size;
            }
//            sizes[viewSize] =count;
//            Log.d(TAG,"size:"+sizes[viewSize]);
        }
    }
    int current=0;
    public void showMenu(){
        Log.d(TAG,"showMenu");
        isMoveing = true;
        isOpen = true;
        current = 1;
        ShowView(imageList.get(current),sizes[current-1],sizes[current]);
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
        ObjectAnimator mObjectAnimator= ObjectAnimator.ofFloat(v, "translationY", start_size, end_size);
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
        ObjectAnimator mObjectAnimator= ObjectAnimator.ofFloat(v, "translationY", start_size,  end_size);
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
}
