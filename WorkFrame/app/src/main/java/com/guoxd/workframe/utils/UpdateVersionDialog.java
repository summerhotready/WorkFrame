package com.guoxd.workframe.utils;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guoxd.workframe.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.DecimalFormat;

import okhttp3.Call;

/**版本检查工具
 * Created by guoxd on 2018/9/12.
 * 申请权限
 *  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
 增加fileProvider
 * 需要在调用页面先初始化对象，再进行权限判断：
 * void showUpdate(){
     if (EasyPermissions.hasPermissions(this, updateVersionDialog. PERMS_EXTERNAL_STORAGE)) {
        updateVersionDialog.checkVersion();
     } else {
         EasyPermissions.requestPermissions(this, "应用需要使用存储权限用来保存一些数据,禁止可能会导致某些异常",
         updateVersionDialog. REQUEST_CODE_PERMS_EXTERNAL_STORAGE,updateVersionDialog. PERMS_EXTERNAL_STORAGE);
     }
 }
 @Override
 public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
 }

 @Override
 public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
     if (requestCode == updateVersionDialog. REQUEST_CODE_PERMS_EXTERNAL_STORAGE) {
     updateVersionDialog.checkVersion();
     }
 }

 @Override
 public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
     if (requestCode == updateVersionDialog. REQUEST_CODE_PERMS_EXTERNAL_STORAGE) {
     getAppDetailSettingIntent(LoginActivity.this);
     }
 }

 private void getAppDetailSettingIntent(Context context) {
 AlertDialog.Builder builder = new AlertDialog.Builder(context);
 builder.setMessage("应用需要使用存储权限用来保存一些数据,禁止可能会导致某些异常");
 builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
 @Override
 public void onClick(DialogInterface dialog, int which) {
 Intent localIntent = new Intent();
 localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 if (Build.VERSION.SDK_INT >= 9) {
 localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
 localIntent.setData(Uri.fromParts("package", getPackageName(), null));
 } else if (Build.VERSION.SDK_INT <= 8) {
 localIntent.setAction(Intent.ACTION_VIEW);
 localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
 localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
 }
 startActivity(localIntent);
 }
 });
 builder.show();
 }
 * 并手动确认更新
 * 需要在res中增加layout：dialog_download_progress
 */

public class UpdateVersionDialog extends DialogFragment {
    final String TAG="UpdateVersionDialog";
    public int REQUEST_CODE_PERMS_EXTERNAL_STORAGE = 100;
    public String[] PERMS_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    interface UpdateVersionDialogListener{
        void onUpdateFailure(String msg);
        void showUpdateDialog();
    }
    UpdateVersionDialogListener mListener;

    public void setListener(UpdateVersionDialogListener listener) {
        this.mListener = listener;
    }

    //该flag在执行close后会置为false，主要用于异步网络后，是否还有弹出后续页面的需求
//    boolean isRun = true;
    //检查版本更新地址
    String id = "5bbd6a3cca87a8758e371b12";
    String api_token = "dcff7f1027fe9475f3d44ca7fb38ac81";

    String versionCheckUrl=String.format("http://api.fir.im/apps/latest/%s?api_token=%s",id, api_token);
    //更新包名称
    String apkName = "apkName.apk";

    String updateUrl="";

    Dialog mDialog;
    /**检查版本更新入口
     * 应用id api_Token
     */
    public void checkVersion() {
        OkHttpUtils
                .get()
                .url(versionCheckUrl)
                .build()
                .execute(new StringCallback(){
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.d(TAG, "onError");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)){
                            //error
                        }else{
                            LogUtil.d(TAG, "checkVersion response:" + response);
                            FirVersionEntity firVersionEntity = new Gson().fromJson(response, FirVersionEntity.class);
                            int fir_version = firVersionEntity.version;//当前最新版本
                            int versionCode = SystemUtils.getIntent().getVersionCode(getActivity());
                            if (fir_version > versionCode) {//need update
                                updateUrl = firVersionEntity.installUrl;
                                mListener.showUpdateDialog();
                            }else{
                                LogUtil.d(TAG, "checkVersion same version");
                            }
                        }
                    }
                } );

    }
    class FirVersionEntity{
        private String name;
        private int version;//当前最新版本
        private Object changelog;
        private int updated_at;
        private String versionShort;
        private String build;
        private String installUrl;//下载地址
        private String install_url;
        private String direct_install_url;
        private String update_url;
    }

    boolean isRun(){
        return getActivity() !=null;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       //展示请求
            AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            builder.setMessage("这里有一个新的版本,建议你更新!");
            builder.setPositiveButton("更新", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(isRun()) {
                        getDialog().dismiss();
                        showDownLoad();
                    }
                }
            });
            builder.setNegativeButton("取消",new  DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        mDialog = builder.show();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
                if (keyCode == KeyEvent.KEYCODE_BACK ) {
                    dismissAllowingStateLoss();
                    return true; // pretend we've processed it
                }else
                    return false; // pass on to be processed as normal
            }
        });
        return mDialog;
    }

    void showDownLoad(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_download_progress, null);
        final ProgressBar mProgress= view.findViewById(R.id.progress);
        mProgress.setMax(100);
        final TextView mPresemt = view.findViewById(R.id.present);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
                if (keyCode == KeyEvent.KEYCODE_BACK ) {
                    mDialog.dismiss();
                    dismissAllowingStateLoss();
                    return true; // pretend we've processed it
                }else
                    return false; // pass on to be processed as normal
            }
        });
        /*final AlertDialog alertDialog= builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();*/
        final DecimalFormat decimalFormat = new DecimalFormat("#.0 %");
        //下载
        OkHttpUtils
                .get()
                .url(updateUrl)
                .addParams("username", "hyman")
                .addParams("password", "123")
                .build()
                .execute(new FileCallBack(FileUtils.getIntent().getDiskCacheDir(getActivity()),"apkName.apk") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDialog.dismiss();
                        if (isRun()) {
                            mListener.onUpdateFailure("文件下载错误");
                        }
                        dismissAllowingStateLoss();
                    }
                    @Override
                    public void onResponse(File response, int id) {
                        mDialog.dismiss();
                        if(response!=null && isRun()) {
                            installApk(response);
                        }else{
                            dismissAllowingStateLoss();
                        }
                    }
                    @SuppressLint("DefaultLocale")
                    @Override //进度
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        if(mDialog.isShowing() && isRun()) {
                            mProgress.setProgress((int) (progress * 100));
                            mPresemt.setText(decimalFormat.format(progress));//java.util.UnknownFormatConversionException: Conversion:
                        }
                    }
                });
    }

    /**
     * 当窗口关闭（执行了dismiss或者点击了外部区域，或者按了返回键），此方法执行。
     * 但是不要调用父类的onDismiss方法，否则当前fragment就会被移除。我们想要的是fragment还在，只是关闭掉内部弹框，也是隐藏视图。
     * @param dialog
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        //丢弃super.onDismiss(dislog);
        LogUtil.d(TAG,"fragment onDismiss");
    }

    /**
     * 安装app
     */
    protected void installApk( File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    Uri apkUri = FileProvider.getUriForFile(getActivity(), "com.elco.anfang.men.fileprovider", file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } catch (Exception e) {
                    LogUtil.d("update", e.getMessage());
                    mListener.onUpdateFailure("安装失败，请检查开启安装未知应用");
                }
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isRun()) {
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        dismissAllowingStateLoss();
    }
}
