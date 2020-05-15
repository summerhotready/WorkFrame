package com.guoxd.work_frame_library.utils;

import androidx.annotation.ColorInt;

public class ColorUtils {

    //给颜色添加透明度
    public static int getColorWithAlpha(float alpha,@ColorInt int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
