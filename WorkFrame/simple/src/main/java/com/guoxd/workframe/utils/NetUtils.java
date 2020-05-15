package com.guoxd.workframe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**提供联网判断服务
 * Created by guoxd on 2018/8/14.
 */

public class NetUtils {
    //没有连接网络
    public static final int NETWORK_NONE = -1;
    //移动网络
    public static final int NETWORK_MOBILE = 0;
    //无线网络
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取接口
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断是否连接
        if(networkInfo!=null && networkInfo.isConnected()){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return NETWORK_WIFI;
            }
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                return NETWORK_MOBILE;
            }
        }
        //未连接
        return NETWORK_NONE;
    }


    //判断有无网络
    //true 有网, false 没有网络.
    public static boolean isNetConnect(Context context) {
        int flag = getNetWorkState(context);
        if (flag > NetUtils.NETWORK_NONE) {
            return true;
        }
        return false;
    }
}
