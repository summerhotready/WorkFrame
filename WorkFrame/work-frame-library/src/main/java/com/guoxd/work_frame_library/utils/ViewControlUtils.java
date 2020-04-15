package com.guoxd.work_frame_library.utils;

import android.content.Context;
import android.view.View;

/**
 * Created by guoxd on 2018/7/27.
 */

public class ViewControlUtils {
    public static boolean isViewShow(View view){
        if(view !=null){
            if(view.getVisibility() ==View.VISIBLE){
                return true;
            }
        }
        return false;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }
}
