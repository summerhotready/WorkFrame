package com.guoxd.workframe.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.guoxd.workframe.R;

import java.time.format.TextStyle;

/**视图工具类
 * Created by guoxd on 2018/6/19.
 */

public class ViewHelpUtils {
    /**给TextView设置drawableTop
     * @param tv 组件
     * @param drawable  图标
     *@param imageSize 图标尺寸
     */
    public static void setDrawableTop(TextView tv, Drawable drawable, int imageSize){
        drawable.setBounds(0,0,imageSize,imageSize);
        tv.setCompoundDrawables(null,drawable
                ,null,null);
    }
    /**给TextView设置drawableRight
     * @param tv 组件
     * @param drawable  图标
     *@param imageSize 图标尺寸
     */
    public static void setDrawableRight(TextView tv, Drawable drawable, int imageSize){
        drawable.setBounds(0,0,imageSize,imageSize);
        tv.setCompoundDrawables(null,null
                ,drawable,null);
    }
    /**Drawable着色
     * @param drawable
     * @param colors
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**转换View成bitmap
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**  dp转px  */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**  px转dp  */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //为了计算使popupwindow与目标view中间对其
    public static int dipTopx(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int getTextViewTextHeight(Context context, AppCompatTextView textView) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textView.getTextSize());

        return (int) textPaint.getFontMetricsInt().bottom-textPaint.getFontMetricsInt().top;// 与文本推荐高度一致
    }
    public static int getTextViewTextWidth(Context context, AppCompatTextView textView){
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textView.getTextSize());
        getTextViewTextHeight(context, textView);
        return (int)Layout.getDesiredWidth(textView.getText().toString(),textPaint);//273
    }
}
