package com.guoxd.work_frame_library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * Created by guoxd on 2018/10/12.
 */

public class TabLinearLayout extends LinearLayout {

    Context mContext;

    //构造器
    public TabLinearLayout(Context context){
        this(context,null);
    }

    public TabLinearLayout(Context context, AttributeSet attrs) {
        this((Context)null, (AttributeSet)null, 0, 0);
    }

    public TabLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this((Context)null, (AttributeSet)null, 0, 0);
    }

    public TabLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super((Context)null, (AttributeSet)null, 0, 0);
    }

    //default
    int textDefaultColor = android.R.color.secondary_text_dark;
    int textCheckColor = android.R.color.black;

    //padding


    public final static int SingleModle=0;
    public final static int MulitModle=1;

    class TabPoint{
        String textDefault;//默认显示底部文字
        String textCheck;//选中时底部文字
        //颜色默认使用default
        int textDefaultColor = 0;
        int textCheckColor = 0;
        //图片
        int gravityModle = SingleModle;//SingleModle,MulitModle
        //SingleModle
        int gravity = Gravity.LEFT;//
        int imageRef=0;//未设置不显示

        //MulitModle
        int[] gravitys=new int[4];//四个方向分别为left,top,right,bottom,值默认为0，为1时为有
        int[] imageRefs=new int[4];//四个方向分别为left,top,right,bottom,值默认为0,设置不填写则不显示

        //padding
        int[] paddings=new int[4];//四个方向分别为left,top,right,bottom,

    }
}
