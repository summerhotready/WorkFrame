package com.guoxd.work_frame_library.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by guoxd on 2018/6/1.
 */

public class TextShowUtils {
    public static void ShowToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void ShowLog(String tag,String msg){
        Log.d(tag,msg);
    }
    public static void ShowLogError(String tag,String msg){
        Log.e(tag,msg);
    }
}
