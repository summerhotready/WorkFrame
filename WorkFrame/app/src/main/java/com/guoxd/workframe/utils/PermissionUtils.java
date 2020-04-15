package com.guoxd.workframe.utils;

import android.Manifest;

public class PermissionUtils {
    private static String[] CAMERA_PERMISS = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    public static String[] getCameraPermiss() {
        return CAMERA_PERMISS;
    }
}
