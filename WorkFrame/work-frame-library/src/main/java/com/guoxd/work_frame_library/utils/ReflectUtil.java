package com.guoxd.work_frame_library.utils;

import android.content.Context;

/**
 * <p>资源反射类</p>
 * <li>getViewId - 获取控件id
 * <li>getLayoutId - 获取布局id
 * <li>getStringId - 获取字符串id
 * <li>getDrawableId - 获取图片资源id
 * <li>getStyleId - 获取样式id
 * <li>getDimenId - 获取尺寸id
 * <li>getArrayId - 获取数组资源id
 * <li>getColorId - 获取颜色id
 * <li>getAnimId - 获取动画资源id
 * <li>isClassFounded - 判断类是否存在
 * <li>getObjectByClassName - 根据类名获取对象
 * 从：
 *TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgress, defStyleAttr, 0);
 * 到：
 * TypedArray a = context.obtainStyledAttributes(attrs, new int[]{ReflectUtil.getStyleId("CircularProgress")[0]}, defStyleAttr, 0);
 * */
public class ReflectUtil  {

    private static int getResourceId(Context context, String name, String type) {
        int id = 0;
        id = context.getResources().getIdentifier(name, type, context.getPackageName());
        return id;
    }

    public static int getViewId(Context context, String name) {
        return getResourceId(context, name, "id");
    }

    public static int getLayoutId(Context context, String name) {
        return getResourceId(context, name, "layout");
    }

    public static int getStringId(Context context, String name) {
        return getResourceId(context, name, "string");
    }

    public static int getDrawableId(Context context, String name) {
        return getResourceId(context, name, "drawable");
    }

    public static int getStyleId(Context context, String name) {
        return getResourceId(context, name, "style");
    }

    public static int getDimenId(Context context, String name) {
        return getResourceId(context, name, "dimen");
    }

    public static int getArrayId(Context context, String name) {
        return getResourceId(context, name, "array");
    }

    public static int getColorId(Context context, String name) {
        return getResourceId(context, name, "color");
    }

    public static int getAnimId(Context context, String name) {
        return getResourceId(context, name, "anim");
    }

    public static boolean isClassFounded(String className)
    {
        try {
            @SuppressWarnings("unused")
            Class<?> aClass = Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object getObjectByClassName(String className)
    {
        try {
            Class<?> aClass = Class.forName(className);
            return aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
