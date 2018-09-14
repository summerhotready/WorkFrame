package com.guoxd.work_frame_library.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by guoxd on 2018/8/24.
 */

public class MobileInfoUtil {
    /**
     * 获取手机IMEI
     *IMEI(International Mobile Equipment Identity)是国际移动设备身份码的缩写，国际移动装备辨识码，是由15位数字组成的”电子串号”，它与每台移动电话机一一对应，而且该码是全世界唯一的。每一只移动电话机在组装完成后都将被赋予一个全球唯一的一组号码，这个号码从生产到交付使用都将被制造生产的厂商所记录。
     PS：通俗来讲就是标识你当前设备(手机)全世界唯一，类似于个人身份证，这个肯定唯一啦~
     * @param context
     * @return
     */
    public static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取手机IMSI
     * 国际移动用户识别码（IMSI：International Mobile Subscriber Identification
     Number）是区别移动用户的标志，储存在SIM卡中，可用于区别移动用户的有效信息。其总长度不超过15位，同样使用0~9的数字。其中MCC是移动用户所属国家代号，占3位数字，中国的MCC规定为460；MNC是移动网号码，由两位或者三位数字组成，中国移动的移动网络编码（MNC）为00；用于识别移动用户所归属的移动通信网；MSIN是移动用户识别码，用以识别某一移动通信网中的移动用户
     PS：通俗来讲就是标识你当前SIM卡(手机卡)唯一，同样类似于个人身份证，肯定唯一啦~
     * @param context
     * @return
     */
    public static String getIMSI(Context context){
        try {
            TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            String imsi=telephonyManager.getSubscriberId();
            if(null==imsi){
                imsi="";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
