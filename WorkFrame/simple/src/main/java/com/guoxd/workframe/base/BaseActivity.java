package com.guoxd.workframe.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.guoxd.workframe.R;

public abstract class BaseActivity extends AppCompatActivity {
    public String TAG = getClass().getName();

    abstract protected int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
    }


    public void initView(){};

    /**
     * 判断键盘是否在显示
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isSoftShowing(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - getSoftButtonsBarHeight(context) != 0;
    }
    //view处理
    public View getView(int id){
        return findViewById(id);
    }
    //textView处理
    public TextView getTextView(int id){
        return findViewById(id);
    }
    public String getTextViewString(int id){return ((TextView)findViewById(id)).getText().toString();}
    //权限
    public void getAppSettingIntent(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);//getString(R.string.permiss_to_set)
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                }
                startActivity(localIntent);
            }
        });
        builder.show();
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void closeSoft(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm.isActive()) {//如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

/*
修改状态栏颜色
 */
    public void setStateBarColor(@ColorInt int color, boolean isLight){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = getWindow();
        if(window == null)
            return;
        //5.1.1无遮盖，测试5.0是否带半透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //底部虚拟键
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            try {
                window.setStatusBarColor(color);
                window.setNavigationBarColor(color);
            }catch (Exception e){
                e.printStackTrace();
            }
            //6.0以上系统支持修改状态栏字体颜色，6.0以下不支持的设为黑色透明背景，突显白色字体
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(isLight){//light
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }else{//dark
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        } else {//API19-20
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置是否沉浸式
     * @param isTranlate
     */
    public void setStateBarTranslate(boolean isTranlate,boolean isLight){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = getWindow();
        if(window == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            try {
                window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.transparent));
                window.setNavigationBarColor(ContextCompat.getColor(this,android.R.color.transparent));
            }catch (Exception e){
                e.printStackTrace();
            }
            //6.0以上系统支持修改状态栏字体颜色，6.0以下不支持的设为黑色透明背景，突显白色字体
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(isLight){//light
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }else{//dark
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        } else {//API19-20
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


}
