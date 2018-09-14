package com.guoxd.work_frame_library.utils;

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
}
