package com.guoxd.work_frame_library.views.arcmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.guoxd.work_frame_library.R;

/**放射状，卫星菜单
 */
public class ArcMenu extends ViewGroup {
    private View mButton;

    private BStatus mBStatus = BStatus.STATUS_CLOSE;
    public enum BStatus {
        STATUS_OPEN, STATUS_CLOSE
    }

    private onSubItemClickListener onListener;
    //子菜单点击接口
    public interface onSubItemClickListener {
        void onItemClick(View view, int position);
    }

    public ArcMenu(Context context){
        super(context);
    }
    public ArcMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSubItemClickListener(onSubItemClickListener mListener) {
        this.onListener = mListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //添加布局，就是所要显示的控件View
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            //主菜单按钮
            onMainButton();
            //子菜单按钮
            onSubItemButton();
        }
    }

    //获取主菜单按钮
    private void onMainButton() {
        mButton = getChildAt(0);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //主菜单动画
                Animation rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.arc_menu_anim);
                mButton.startAnimation(rotateAnim);

                //子菜单动画
                subItemAnim();
            }
        });
        int l, t, r = 0, b = 0;
        int mWidth = mButton.getMeasuredWidth();
        int mHeight = mButton.getMeasuredHeight();

        l = getMeasuredWidth() - mWidth;
        t = getMeasuredHeight() - mHeight;

        mButton.layout(l, t, getMeasuredWidth(), getMeasuredHeight());
    }

    //获取子菜单按钮
    private void onSubItemButton() {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            View childView = getChildAt(i + 1);

            //开始时不呈现子菜单
            childView.setVisibility(View.GONE);

            int radius = 350;
            int cl, ct, cr, cb;

            cr = (int) (radius * Math.sin(Math.PI / 2 / (count - 2) * i));
            cb = (int) (radius * Math.cos(Math.PI / 2 / (count - 2) * i));

            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();

            cl = getMeasuredWidth() - cWidth - cr;
            ct = getMeasuredHeight() - cHeight - cb;

            //layout(l,t,r,b);前两参数决定位置，后两参数决定大小
            //参数(1,t)为View控件的左上角坐标
            // (r-l,b-t)为View控件大小,r-l为控件宽度,b-t为控件高度
            childView.layout(cl, ct, getMeasuredWidth() - cr, getMeasuredHeight() - cb);
        }
    }

    //子菜单散开回笼动画
    public void subItemAnim() {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View cView = getChildAt(i + 1);

            //点击主菜单后，子菜单就立刻呈现，否则后面的动画无法完成
            cView.setVisibility(VISIBLE);

            int radius = 350;
            int l, t, r, d;

            r = (int) (radius * Math.sin(Math.PI / 2 / (count - 2) * i));
            d = (int) (radius * Math.cos(Math.PI / 2 / (count - 2) * i));

//            int cWidth = cView.getMeasuredWidth();
//            int cHeight = cView.getMeasuredHeight();
//
//            l = getMeasuredWidth() - cWidth - r;
//            t = getMeasuredHeight() - cHeight - d;

            AnimationSet set = new AnimationSet(true);
            Animation tranAnim = null;
            if (mBStatus == BStatus.STATUS_CLOSE) {
                //散开动画
                tranAnim = new TranslateAnimation(r, 0, d, 0);
                cView.setClickable(true);
                cView.setFocusable(true);
            } else {
                //回笼动画
                tranAnim = new TranslateAnimation(0, r, 0, d);
                cView.setClickable(false);
                cView.setFocusable(false);
            }
            tranAnim.setDuration(300);
//            tranAnim.setFillAfter(true);  //让最后一帧的动画不消失
            tranAnim.setStartOffset(100 * i / count);
            tranAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mBStatus == BStatus.STATUS_CLOSE) {
                        cView.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            Animation rotateAnim = new RotateAnimation(
                    0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(300);
//            rotateAnim.setFillAfter(false);

            set.addAnimation(rotateAnim);
            set.addAnimation(tranAnim);
            cView.startAnimation(set);

            //散开后子菜单的点击监听事件
            final int pos = i + 1;
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onListener != null) {
                        onListener.onItemClick(cView, pos);
                    }
                    //散开后点击子菜单动画
                    subItemClickAnim(pos - 1);
                    changStatus();
                }
            });
        }
        changStatus();
    }

    //监听子菜单状态改变
    private void changStatus() {
        mBStatus = (mBStatus == BStatus.STATUS_CLOSE ? BStatus.STATUS_OPEN : BStatus.STATUS_CLOSE);
    }

    //散开后点击子菜单动画
    private void subItemClickAnim(int pos) {
        int count = getChildCount();
        for (int i = 0;i<count-1;i++) {
            View cView = getChildAt(i+1);
            if(i == pos) {
                //变大，变透明
                cView.startAnimation(toBig());
            }
            else {
                //变小，变透明
                cView.startAnimation(toSmall());
            }
            cView.setClickable(false);
            cView.setFocusable(false);
        }
    }

    //变大，变透明
    private Animation toBig(){
        Animation big = AnimationUtils.loadAnimation(getContext(), R.anim.arc_big_alpha_anim);
        return big;
    }

    //变小，变透明
    private Animation toSmall(){
        Animation small = AnimationUtils.loadAnimation(getContext(),R.anim.arc_small_alpha_anim);
        return small;
    }

    //给ListView调用
    public boolean isOpen() {
        return mBStatus == BStatus.STATUS_OPEN;
    }

}
