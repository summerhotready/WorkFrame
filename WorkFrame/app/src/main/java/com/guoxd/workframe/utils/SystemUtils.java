package com.guoxd.workframe.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**提供系统服务
 * Created by guoxd on 2018/10/23.
 */

public class SystemUtils {
    static SystemUtils utils;
    private SystemUtils(){}
    public static SystemUtils getIntent(){
        if(utils ==null){
            utils = new SystemUtils();
        }
        return utils;
    }
    /**
     * 获取当前版本信息
     */
    public int getVersionCode(Context mContext){
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(
                        mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return 0;
    }

    //日期格式化
    public Date string2date(String str){
        return string2date(str,"yyyy-MM-dd");
    }
    public Date string2date(String str,String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            Date date = sf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**比较日期大小
     * @param start
     * @param end
     * @return flag，等于0日期相等,大于0表示在end在start之后,小于0表示在end在start之前
     */
    public int compairDate(String start, String end) {
        Date strDate = string2date(start);
        Date endDate = string2date(end);
        int flag = endDate.compareTo(strDate);
        return flag;
    }
}
