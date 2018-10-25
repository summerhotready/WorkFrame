package com.guoxd.workframe.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**弹出提示专用工具
 * Created by guoxd on 2017/3/1.
 */

public class ToastUtils {
	private static Toast toast = null;

	public static void showMsgToast(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
	//具有layout：view_toast
	public static void showViewToast(Activity activity, String msg) {
		/*if (toast == null) {
			//LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
			//调用Activity的getLayoutInflater()
			View view = activity.getLayoutInflater().inflate(R.layout.view_toast, null); //加載layout下的布局
			ImageView iv = view.findViewById(R.id.tvImageToast);
			iv.setImageResource(R.mipmap.atm);//显示的图片
			TextView title = view.findViewById(R.id.tvTitleToast);
			title.setText(titles); //toast的标题
			TextView text = view.findViewById(R.id.tvTextToast);
			text.setText(messages); //toast内容
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
			toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
			toast.setView(view); //添加视图文件
		}
		toast.show();*/
	}
	public static void showTextSnack(View view, String msg) {
		Snackbar.make(view,msg, Snackbar.LENGTH_SHORT).show();
	}
	//layout:view_snack
	public static void showViewtSnack(Activity activity, String msg) {
		//加載layout下的布局
		/*View view = activity.getLayoutInflater().inflate(R.layout.view_snack, null);
		View viewPos = view.findViewById(R.id.myCoordinatorLayout);
		Snackbar.make(viewPos, R.string.snackbar_text, Snackbar.LENGTH_LONG)
				.setAction(R.string.snackbar_action_undo, showListener)
				.show();*/
	}
}
