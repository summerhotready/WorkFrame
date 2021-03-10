package com.guoxd.work_frame_library.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

/**
 * Created by guoxd on 2018/5/23.
 */

public class PaintViewUtils {
    //****************************尺寸相关*********************
    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }
    public static int getSizePX(Context context,int size){
        return  Math.round(size* context.getResources().getDisplayMetrics().density);
    }

    public static float getTextViewLength(TextView textView, String text){
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        return textLength;
    }

    public static float getTextViewHeight(TextView textView,String text){
        Paint pFont = new Paint();
        Rect rect = new Rect();
        pFont.getTextBounds(text, 0, 1, rect);
        return rect.height();
    }
    public static LinearLayout.LayoutParams getLayoutParams(int h,int w,int l,int t,int r,int b){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w,h);
            lp.setMargins(l,t,r,b);
        return lp;
    }
    //**************************** Drawable相关 *********************
    public static Drawable getDrawable(Context context, int id){
        return ContextCompat.getDrawable(context, id);
//        return context.getResources().getDrawable(id,context.getTheme());
    }

    //****************************Color相关*********************
    public static int getColor(Context context,int id){
//        return context.getResources().getColor(id);
        return ContextCompat.getColor(context,id);
    }
    //给颜色添加透明度
    public static int getColorWithAlpha(float alpha,@ColorInt int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    //**************************** 显示相关 *********************
    public static boolean isViewShow(View view){
        if(view !=null){
            if(view.getVisibility() ==View.VISIBLE){
                return true;
            }
        }
        return false;
    }

}
