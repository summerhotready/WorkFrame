package com.guoxd.work_frame_library.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by guoxd on 2018/5/23.
 */

public class PaintViewUtils {
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

    public static Drawable getDrawable(Context context,int id){
        return context.getResources().getDrawable(id,context.getTheme());
    }

    public static int getSizePX(Context context,int size){
        return  Math.round(size* context.getResources().getDisplayMetrics().density);
    }

    public static int getColor(Context context,int id){
        return context.getResources().getColor(id);
    }
}
