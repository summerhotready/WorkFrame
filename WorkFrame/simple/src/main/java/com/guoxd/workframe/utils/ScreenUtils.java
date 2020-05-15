package com.guoxd.workframe.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemProperties;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 用于适配刘海屏
 */
@TargetApi(5)
public class ScreenUtils {
    static final String TAG="ScreenUtils";

    /**设置statusBar颜色为transparent
     */
    public static void setStatusBarTransparent(Activity activity){
        //>4.4设置
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {//6.0以下的
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
        //android6.0以后可以对状态栏文字颜色和图标进行修改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 沉浸式
     */
    @TargetApi(28)
    public static void openFullScreenModel(Activity activity,final ScreenChangeListener listener){
        LogUtil.i(TAG,"sceen_ openFullScreenModel");
        Window window = activity.getWindow();
        //android P
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
            //沉浸式状态栏适配
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            View contentView = window.getDecorView().findViewById(android.R.id.content).getRootView();
            contentView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    DisplayCutout cutout = windowInsets.getDisplayCutout();
                    if (cutout == null) {
                        LogUtil.e(TAG, "cutout==null, is not notch screen");//通过cutout是否为null判断是否刘海屏手机
                    } else {
                        List<Rect> rects = cutout.getBoundingRects();
                        if (rects == null || rects.size() == 0) {
                            LogUtil.e(TAG, "rects==null || rects.size()==0, is not notch screen");
                        } else {
                            LogUtil.e(TAG, "rect size:" + rects.size());//注意：刘海的数量可以是多个
                            for (Rect rect : rects) {
                                LogUtil.e(TAG, "cutout.getSafeInsetTop():" + cutout.getSafeInsetTop() //168
                                        + ", cutout.getSafeInsetBottom():" + cutout.getSafeInsetBottom() //0
                                        + ", cutout.getSafeInsetLeft():" + cutout.getSafeInsetLeft() //0
                                        + ", cutout.getSafeInsetRight():" + cutout.getSafeInsetRight() //0
                                        + ", cutout.rects:" + rect); //Rect(552, 0 - 888, 168)
                                listener.onStatusBarHas(cutout.getSafeInsetTop());
                            }
                        }
                    }
                    return windowInsets;
                }
            });
        }else{//android O
            //华为 O 刘海屏
            if(hasNotchInScreenHuaWei(activity)){
                //获取华为刘海的高宽
                int[] sceens = getNotchSizeHuaWei(activity);
                LogUtil.i(TAG,String.format("sceen_ huawei has (%d,%d)",sceens[0],sceens[1]));
                listener.onStatusBarHas(sceens[1]);
            }else if(hasNotchInScreenMiUI(activity)){
                int[] sceens = getNotchSizeMiUI(activity);
                LogUtil.i(TAG,String.format("sceen_ mi has (%d,%d)",sceens[0],sceens[1]));
                listener.onStatusBarHas(sceens[0]);
            }else if(hasNotchInScreenOppo(activity)){
                int bar = getStatusBarHeightByField(activity);
                LogUtil.i(TAG,String.format("sceen_ oppo has %d",bar));
                listener.onStatusBarHas(bar);
            }else if(hasNotchInScreenVoio(activity)){
                int bar = getStatusBarHeightByField(activity);
                LogUtil.i(TAG,String.format("sceen_ voio has %d",bar));
                listener.onStatusBarHas(bar);
            }else{
                int bar = getStatusBarHeightByField(activity);
                LogUtil.i(TAG,String.format("sceen_ voio has %d",bar));
                listener.onStatusBarHas(bar);
            }
        }
    }

    /**华为刘海屏判断（o版本）
     * @param context
     * @return
     */
    public static boolean hasNotchInScreenHuaWei(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            LogUtil.e(TAG, "huawei_ hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "huawei_ hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            LogUtil.e(TAG, "huawei_ hasNotchInScreen Exception");
        } finally {
            return ret;
        }
    }

    /**华为获取刘海尺寸
     * @param context
     * @return
     */
    public static int[] getNotchSizeHuaWei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            LogUtil.e(TAG, "huawei_ getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtil.e(TAG, "huawei_ getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            LogUtil.e(TAG, "huawei_ getNotchSize Exception");
        } finally {
            return ret;
        }
    }

    /** MIUI Android O
     * @param context
     * @return
     */
    public static boolean hasNotchInScreenMiUI(Context context) {
        return SystemProperties.getInt("ro.miui.notch", 0) == 1;
    }

    /**MIUI Android O
     * @param context
     * @return
     */
    public static int[] getNotchSizeMiUI(Context context) {
        int result[] =new int[]{0,0};
        //高度获取
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            result[0] = context.getResources().getDimensionPixelSize(resourceId);
        }
        //宽度获取
         resourceId = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (resourceId > 0) {
            result[1] = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //oppo
    public static boolean hasNotchInScreenOppo(Context context){
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
    //voio
    public static final int NOTCH_IN_SCREEN_VOIO=0x00000020;//是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VOIO=0x00000008;//是否有圆角
    public static boolean hasNotchInScreenVoio(Context context){
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport",int.class);
            ret = (boolean) get.invoke(FtFeature,NOTCH_IN_SCREEN_VOIO);

        } catch (ClassNotFoundException e)
        { LogUtil.e("test", "hasNotchInScreen ClassNotFoundException"); }
        catch (NoSuchMethodException e)
        { LogUtil.e("test", "hasNotchInScreen NoSuchMethodException"); }
        catch (Exception e)
        { LogUtil.e("test", "hasNotchInScreen Exception"); }
        finally
        { return ret; }
    }

    //    Notch 设备的状态栏高度与正常机器不一样，因此在需要使用状态栏高度时，不建议写死一个值，而应该改为读取系统的值。
//    以下是获取当前设备状态栏高度的方法：
    /**获取状态栏高度
     *读取静态文件
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**通过反射的方式获取状态栏高度
     * @return
     */
    public static int getStatusBarHeightByField(Activity activity) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public interface ScreenChangeListener{
        void onStatusBarHas(int height);
    }
}
