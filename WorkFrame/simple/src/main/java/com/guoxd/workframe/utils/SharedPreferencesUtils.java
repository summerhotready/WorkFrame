package com.guoxd.workframe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**短信息存储文件
 * Created by: guoxd
 * Data: 2017/2/12
 */

public class SharedPreferencesUtils {
	static SharedPreferencesUtils utils;
	private static SharedPreferences sp;

	private SharedPreferencesUtils(){}
	public static SharedPreferencesUtils getIntent(){
		if(utils == null){
			utils = new SharedPreferencesUtils();
		}
		return utils;
	}

	public void putString(Context context,String key, String value) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}

	public String getString(Context context,String key, String defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}

	public void putBoolean(Context context,String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	public boolean getBoolean(Context context,String key, boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}

	public void putInt(Context context,String key, int value) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}

	public int getInt(Context context,String key, int defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defValue);
	}
	//浮点型
	public void putFloat(Context context,String key, float value) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putFloat(key, value).commit();
	}

	public float getFloat(Context context,String key, float defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getFloat(key, defValue);
	}

}
