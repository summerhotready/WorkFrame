package com.guoxd.workframe.base;

import android.app.Application;
import android.content.Context;

import com.guoxd.workframe.utils.HttpUtils;

/**
 * Created by guoxd on 2018/10/23.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //初始化一些单例类
        HttpUtils.getIntent(this);
    }
    static Context mContext;

    public static Context getContext() {
        return mContext;
    }
}
