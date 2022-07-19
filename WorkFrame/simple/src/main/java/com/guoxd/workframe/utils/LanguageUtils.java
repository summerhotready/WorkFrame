package com.guoxd.workframe.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import android.os.LocaleList;
import android.text.TextUtils;

import com.guoxd.workframe.base.MainApplication;

import java.lang.reflect.Field;

/**
 * 多语言工具类
 * Created by Fitem on 2020/03/20.
 * Update by emma on 2020/12/10.
 */

public class LanguageUtils {

    public static String getSaveLanguage() {
        return SharedPreferencesUtils.getIntent().getString(MainApplication.getContext(),Constant.TAG_LANG,"");
    }
    public static void setSaveLanguage(String value) {
        SharedPreferencesUtils.getIntent().putString(MainApplication.getContext(),Constant.TAG_LANG,value);
    }

    private static boolean isLocalChinese(java.util.Locale locale){
        return locale.getLanguage().indexOf("zh")>=0;
    }
    public static boolean appLanguageIsChinese(){
        return isLocalChinese(getCurrentAppLocale());
    }

    /**
     * 获取app缓存Locale
     *没存过返回系统当前
     * @return null则无
     */
    public static java.util.Locale getPrefAppLocale() {
        String appLocaleLanguage = getSaveLanguage();
        if (TextUtils.isEmpty(appLocaleLanguage)) {
            return getSystemLocale();
        } else {
            return java.util.Locale.forLanguageTag(appLocaleLanguage);
       }
    }

static String TAG="language";
    public static void changeLanguage(Context context,boolean isCN) {
        LogUtil.i(TAG,"changeLanguage");
        java.util.Locale locale=null;
//        Context context = BaseApplication.getContext();
        if(isCN){
            locale = getChinaese();
        }else{
            locale = getOtherLaguage();
        }

        if(locale!=null) {
            updateLanguage(context, locale);
//            updateLanguage( BaseApplication.getContext(),locale);
        }
    }

    /**
     * 更新该context的config语言配置，对于application进行反射更新
     * @param context
     * @param locale
     */
    private static void updateLanguage(final Context context, java.util.Locale locale) {
        LogUtil.i(TAG,"updateLanguage");
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        java.util.Locale contextLocale = config.locale;
        if (isSameLocale(contextLocale, locale)) {//same local not change
            setSaveLanguage(locale.toLanguageTag());
            return;
        }
        setSaveLanguage(locale.toLanguageTag());

        config.setLocale(locale);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//1) <24
            config.locale = locale;
        }else{//2)>=24
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.createConfigurationContext(config);
            java.util.Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        if (context instanceof Application) {
            Context newContext = context.createConfigurationContext(config);
            try {
                //noinspection JavaReflectionMemberAccess
                Field mBaseField = ContextWrapper.class.getDeclaredField("mBase");
                mBaseField.setAccessible(true);
                mBaseField.set(context, newContext);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 对Application上下文进行替换
     *
     * @param activity activity
     */
    public static void applyAppLanguage(@androidx.annotation.NonNull Activity activity) {
        java.util.Locale appLocale = getCurrentAppLocale();
        try {
            updateLanguage(MainApplication.getContext(), appLocale);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            updateLanguage(activity, appLocale);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static java.util.Locale getChinaese(){
        return java.util.Locale.SIMPLIFIED_CHINESE;
    }
    public static java.util.Locale getOtherLaguage(){
        return java.util.Locale.ENGLISH;
    }

    /**
     * 获取系统Local
     *
     * @return
     */
    public static java.util.Locale getSystemLocale() {
        return Resources.getSystem().getConfiguration().locale;
        /*Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
//        LogUtil.e(TAG, "系统获取  ：getLanguage : " +  locale.getLanguage());
        return locale;*/
    }






    /**
     * 获取当前需要使用的locale，用于activity上下文的生成
     *
     * @return
     */
    public static java.util.Locale getCurrentAppLocale() {
        java.util.Locale prefAppLocale = getPrefAppLocale();
        return prefAppLocale == null ? getSystemLocale() : prefAppLocale;
    }



    /**
     * 判断是否是APP语言
     *
     * @param context
     * @param locale
     * @return
     */
    public static boolean isSimpleLanguage(Context context, java.util.Locale locale) {
        java.util.Locale appLocale = context.getResources().getConfiguration().locale;
        return appLocale.equals(locale);
    }

    /**
     * 获取App当前语言
     *
     * @return
     */
    public static String getAppLanguage() {
        java.util.Locale locale = MainApplication.getContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(language)) { //语言
            stringBuilder.append(language);
        }
        if (!TextUtils.isEmpty(country)) { //国家
            stringBuilder.append("-").append(country);
        }

        return stringBuilder.toString();
    }

    /**
     * 是否是相同的locale
     * @param l0
     * @param l1
     * @return
     */
    private static boolean isSameLocale(java.util.Locale l0, java.util.Locale l1) {
        return equals(l1.getLanguage(), l0.getLanguage())
                && equals(l1.getCountry(), l0.getCountry());
    }

    /**
     * Return whether string1 is equals to string2.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

}
