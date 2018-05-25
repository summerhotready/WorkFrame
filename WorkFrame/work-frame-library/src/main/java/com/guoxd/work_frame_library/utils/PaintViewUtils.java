package com.guoxd.work_frame_library.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
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
}
