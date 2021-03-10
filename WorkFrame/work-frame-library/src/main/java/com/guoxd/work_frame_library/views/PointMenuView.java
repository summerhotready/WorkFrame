package com.guoxd.work_frame_library.views;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;

/**顺序横向或者纵向弹出
 * Created by emma on 2018/10/25.
 * 使用
 */

public class PointMenuView extends LinearLayout {

    String TAG="PointMenuView";
    //设置的menuicon，按照顺序生成menuItems
    int[] images;
    Animator enterAnimtor;
    Animator exitAnimtor;

    //menu item
    ArrayList<AppCompatImageView> menuItems;
    //
    int width;
    //
    Context mContext;


    public PointMenuView(Context context, AttributeSet attrs){
        super(context,attrs);
        mContext = context;
        menuItems = new ArrayList<>();
        setBackgroundResource(android.R.color.transparent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
//        height = MeasureSpec.getSize(heightMeasureSpec);
//        Log.d(TAG,String.format("width size:%d",width));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG,String.format("width:%d,height:%d,o_width:%d,o_height:%d",w, h, oldw, oldh));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG,String.format("left:%d,right:%d,top:%d,buttom:%d",l, t, r, b));
        if(changed){
            Log.d(TAG,"changed");
        }
        setView();
    }

    private void setView(){
        Log.d(TAG,"setView()");
        removeAllViews();
        this.setLayoutParams(new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

        if(images !=null && images.length>0){
            Log.d(TAG,"images");
        }else{//only one
            Log.d(TAG,"menu");
            AppCompatImageView imageView = new AppCompatImageView(mContext);
            imageView.setLayoutParams(new LayoutParams(width,width));
            imageView.setImageResource(android.R.drawable.ic_menu_add);
            imageView.setBackgroundResource(android.R.color.holo_blue_dark);
            addView(imageView,new LayoutParams(width,width));
        }
    }
    public void setImages(int[] images) {
        this.images = images;
        if(images.length>0 && menuItems.size() ==1){
           setView();
        }
    }
}
