package com.guoxd.work_frame_library.anmi;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by guoxd on 2018/6/26.
 * 普通属性动画效果
 */

public class AnimatorUtils {

    private static int animtorTime = 170;

    public static void setAnimtorTime(int animtorTime) {
        AnimatorUtils.animtorTime = animtorTime;
    }

    public static void getSpringScaleAnimtor(View view, final AnimtorCallBackListener listener){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view,"scaleX",0.5f,1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,"scaleY",0.5f,1.0f);
        AnimatorSet set =new AnimatorSet();
        set.setDuration(animtorTime);
        set.setInterpolator(new SpringScaleInterpolator(0.7f));
        set.playTogether(animatorX,animatorY);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                listener.finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
//                LogUtil.d("Animtor","onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
//                LogUtil.d("Animtor","onAnimationCancel");
            }
        });
    }
    public static void getSpringRotatAnimtor(View view, final AnimtorCallBackListener listener){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view,"rotation",60.0f,180.0f);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,"rotation",0.0f,360.0f);//结果会歪
        AnimatorSet set =new AnimatorSet();
        set.setDuration(animtorTime);
        set.setInterpolator(new SpringRotatInterpolator(0.4f));
        set.playTogether(animatorX);//animatorY
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
//                LogUtil.d("Animtor","onAnimationCancel");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                LogUtil.d("Animtor","onAnimationCancel");
                listener.finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
//                LogUtil.d("Animtor","onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
//                LogUtil.d("Animtor","onAnimationCancel");
            }
        });
    }
//用于dialog开启
    public static void getSmoothstepMoveAnimtor(View view, final AnimtorCallBackListener listener){

    }

    public interface AnimtorCallBackListener {
        void start();
        void finish();
    }
}
