package com.guoxd.workframe.viewinject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


import com.guoxd.workframe.utils.LogUtil;

import java.lang.reflect.Method;
//基于afinal，对EventListener做了修改
public class EventListener implements OnClickListener, OnLongClickListener, OnItemClickListener, OnItemSelectedListener, OnItemLongClickListener {

    private static final String TAG = EventListener.class.getSimpleName();
    
	private Object handler;
	
	/**
	 * 寻找注入的方法需要循环到父类中找，该变量是停止循环的父类
	 */
	private Object stopInjectSuperClass;
	
	private String clickMethod;
	private String longClickMethod;
	private String itemClickMethod;
	private String itemSelectMethod;
	private String nothingSelectedMethod;
	private String itemLongClickMethod;
	
	public EventListener(Object handler, Object stopInjectSuperClass) {
		this.handler = handler;
		
		this.stopInjectSuperClass = stopInjectSuperClass;
	}
	
	public EventListener click(String method){
		this.clickMethod = method;
		return this;
	}
	
	public EventListener longClick(String method){
		this.longClickMethod = method;
		return this;
	}
	
	public EventListener itemLongClick(String method){
		this.itemLongClickMethod = method;
		return this;
	}
	
	public EventListener itemClick(String method){
		this.itemClickMethod = method;
		return this;
	}
	
	public EventListener select(String method){
		this.itemSelectMethod = method;
		return this;
	}
	
	public EventListener noSelect(String method){
		this.nothingSelectedMethod = method;
		return this;
	}
	
	public boolean onLongClick(View v) {
		return invokeLongClickMethod(handler,stopInjectSuperClass,longClickMethod,v);
	}
	
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		return invokeItemLongClickMethod(handler,stopInjectSuperClass,itemLongClickMethod,arg0,arg1,arg2,arg3);
	}
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		invokeItemSelectMethod(handler,stopInjectSuperClass,itemSelectMethod,arg0,arg1,arg2,arg3);
	}
	
	public void onNothingSelected(AdapterView<?> arg0) {
		invokeNoSelectMethod(handler,stopInjectSuperClass,nothingSelectedMethod,arg0);
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		invokeItemClickMethod(handler,stopInjectSuperClass,itemClickMethod,arg0,arg1,arg2,arg3);
	}
	
	public void onClick(View v) {
		
		invokeClickMethod(handler, stopInjectSuperClass, clickMethod, v);
	}
	
	
	private static Object invokeClickMethod(Object handler, Object stopInjectSuperClass, String methodName, Object... params){
		
		try{
			Method method = getMethod(handler, stopInjectSuperClass, methodName, View.class);
			if(method!=null)
				return method.invoke(handler, params);
		}catch(Exception e){
			LogUtil.e(TAG , e.getMessage());
		}
		
		return null;
		
	}
	
	
	private static boolean invokeLongClickMethod(Object handler, Object stopInjectSuperClass, String methodName, Object... params){
		try{   
			Method method = getMethod(handler, stopInjectSuperClass, methodName, View.class);
			if(method!=null){
				Object obj = method.invoke(handler, params);
				return obj==null?false: Boolean.valueOf(obj.toString());
			}
		}catch(Exception e){
		}
		
		return false;
		
	}
	
	
	
	private static Object invokeItemClickMethod(Object handler, Object stopInjectSuperClass, String methodName, Object... params){
		try{   
			Method method = getMethod(handler, stopInjectSuperClass, methodName, AdapterView.class, View.class,int.class,long.class);
			if(method!=null)
				return method.invoke(handler, params);	
		}catch(Exception e){
		}
		
		return null;
	}
	
	
	private static boolean invokeItemLongClickMethod(Object handler, Object stopInjectSuperClass, String methodName, Object... params){
		try{   
			Method method = getMethod(handler, stopInjectSuperClass, methodName, AdapterView.class, View.class,int.class,long.class);
			if(method!=null){
				Object obj = method.invoke(handler, params);
				return Boolean.valueOf(obj==null?false: Boolean.valueOf(obj.toString()));
			}
		}catch(Exception e){
		}
		
		return false;
	}
	
	
	private static Object invokeItemSelectMethod(Object handler, Object stopInjectSuperClass, String methodName, Object... params){
		try{   
			Method method = getMethod(handler, stopInjectSuperClass, methodName, AdapterView.class, View.class,int.class,long.class);
			if(method!=null)
				return method.invoke(handler, params);	
		}catch(Exception e){
		}
		
		return null;
	}
	
	private static Object invokeNoSelectMethod(Object handler, Object stopInjectSuperClass, String methodName, Object... params){
		try{   
			Method method = getMethod(handler, stopInjectSuperClass, methodName, AdapterView.class);
			if(method!=null)
				return method.invoke(handler, params);	
		}catch(Exception e){
		}
		
		return null;
	}

	private static Method getMethod(Object handler, Object stopInjectSuperClass, String methodName, Class<?>... parameterTypes) throws Exception {
		if(handler == null || stopInjectSuperClass == null) return null;
		
		Class<? extends Object> clazz = handler.getClass();

		Method method = null;
		
        while (!(clazz.equals(stopInjectSuperClass))) {
        	
        	try{
        		method = clazz.getDeclaredMethod(methodName, parameterTypes);
        	}catch(Exception e){
        	}
        	if(method != null) break;
        	
        	clazz = clazz.getSuperclass();
        }
		
			if(method == null)
				throw new ViewException("no such method:"+methodName);
		
		return method;
	}
	
}
