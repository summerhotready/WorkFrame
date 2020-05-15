package com.guoxd.workframe.utils;

import android.util.Log;


/**提供Log包装功能
 * Created by The_Android on 2017/10/20.
 */

public class LogUtil {

	private static boolean isDebug = true;//当开发完毕需要置为false

	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag,  msg == null? "null":msg);
		}
	}

	public static void e(Object object, String msg) {
		if (isDebug) {
			Log.e(object.getClass().getSimpleName(),  msg == null? "null":msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg == null? "null":msg);
		}
	}

	public static void d(Object object, String msg) {
		if (isDebug) {
			Log.d(object.getClass().getSimpleName(),  msg == null? "null":msg);
		}
	}

	public static void i(String tag, String msg) {
		if (isDebug) {
		Log.i(tag,  msg == null? "null":msg);
		}
	}

	public static void i(Object object, String msg) {
		if (isDebug) {
		Log.i(object.getClass().getSimpleName(),  msg == null? "null":msg);
		}
	}
}
