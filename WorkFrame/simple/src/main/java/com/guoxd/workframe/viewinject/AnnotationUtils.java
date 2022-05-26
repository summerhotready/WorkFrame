/*
 * 文 件 名:  InjectedUtils.java
 * 版    权:  ISOFTSTONE GROUP AND RELATED PARTIES. ALL RIGHTS RESERVED.
 * 描    述:  <文件主要内容描述>
 * 作    者:  ddliu
 * 创建时间:  2014-12-1 上午11:26:15
 */

package com.guoxd.workframe.viewinject;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;

import java.lang.reflect.Field;

/**
 * 注解注入类，关键函数为initInjectedView；处理对EventListener
 */
public class AnnotationUtils {

    private static final String TAG = AnnotationUtils.class.getSimpleName();

    public static void initInjectedView(Activity activity, Class<?> clzz) {
        initInjectedView(activity, activity.getWindow().getDecorView(), clzz);
    }

    /**
     * 注入View和事件
     * 
     * @param injectedSource
     * @param sourceView
     * @param stopInjectSuperClass 需要循环注入父类，该参数为停止注入的父类
     */
    public static void initInjectedView(Object injectedSource, View sourceView, Class<? extends Object> stopInjectSuperClass) {

        Class<? extends Object> clazz = injectedSource.getClass();

        while (null != clazz && !clazz.equals(stopInjectSuperClass)) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                       ViewInject viewInject = field.getAnnotation(ViewInject.class);
                        if (viewInject != null) {

                            int viewId = viewInject.id();
                            field.set(injectedSource, sourceView.findViewById(viewId));

                            setListener(injectedSource, stopInjectSuperClass, field, viewInject.click(), Method.Click);
                            setListener(injectedSource, stopInjectSuperClass, field, viewInject.longClick(), Method.LongClick);
                            setListener(injectedSource, stopInjectSuperClass, field, viewInject.itemClick(), Method.ItemClick);
                            setListener(injectedSource, stopInjectSuperClass, field, viewInject.itemLongClick(), Method.itemLongClick);

                            Select select = viewInject.select();
                            if (!TextUtils.isEmpty(select.selected())) {
                                setViewSelectListener(injectedSource, stopInjectSuperClass, field, select.selected(), select.noSelected());
                            }

                        }
                    } catch (Exception e) {
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    private static void setViewSelectListener(Object injectedSource, Class<? extends Object> stopInjectSuperClass, Field field, String select, String noSelect) throws Exception {
        Object obj = field.get(injectedSource);
        if (obj instanceof View) {
            ((AbsListView) obj).setOnItemSelectedListener(new EventListener(injectedSource, stopInjectSuperClass).select(select).noSelect(noSelect));
        }
    }

    private static void setListener(Object injectedSource, Class<? extends Object> stopInjectSuperClass, Field field, String methodName, Method method) throws Exception {
        if (methodName == null || methodName.trim().length() == 0)
            return;

        Object obj = field.get(injectedSource);

        switch (method) {
            case Click:
                if (obj instanceof View) {
                    ((View) obj).setOnClickListener(new EventListener(injectedSource, stopInjectSuperClass).click(methodName));
                }
                break;
            case ItemClick:
                if (obj instanceof AbsListView) {
                    ((AbsListView) obj).setOnItemClickListener(new EventListener(injectedSource, stopInjectSuperClass).itemClick(methodName));
                }
                break;
            case LongClick:
                if (obj instanceof View) {
                    ((View) obj).setOnLongClickListener(new EventListener(injectedSource, stopInjectSuperClass).longClick(methodName));
                }
                break;
            case itemLongClick:
                if (obj instanceof AbsListView) {
                    ((AbsListView) obj).setOnItemLongClickListener(new EventListener(injectedSource, stopInjectSuperClass).itemLongClick(methodName));
                }
                break;
            default:
                break;
        }
    }

    public enum Method {
        Click, LongClick, ItemClick, itemLongClick
    }
}
