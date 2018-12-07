package com.elco.tieta.tieta.login

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.guoxd.workframe.R
import com.guoxd.workframe.utils.HttpUtils
import com.guoxd.workframe.utils.LogUtil
import com.guoxd.workframe.utils.SystemUtils
import com.guoxd.workframe.utils.http_info.FileDownloadListener
import com.guoxd.workframe.utils.http_info.HttpCallListener
import java.io.File

/**版本检查工具
 * Created by guoxd on 2018/9/12.
 * 需要在调用页面进行权限判断：checkUpdatePermission
 * 并手动确认更新
 * 需要在res中增加layout：dialog_download_progress
 */

class UpdateVersionUtils(var mContext:AppCompatActivity){

    interface LoginControllerListener{
        fun onUpdateFailure(msg:String)
    }
    var mListener:LoginControllerListener ?=null
    //更新包下载地址
    var updateUrl:String?=null
    //检查版本更新地址
    var versionCheckUrl:String ?=null
    //该flag在执行close后会置为false，主要用于异步网络后，是否还有弹出后续页面的需求
    var isRun = true;

    init{
        //入口
        isRun = true;
        checkVersion()
    }

    /**检查版本更新
     * 应用id api_Token
     */
    fun checkVersion() {
        HttpUtils.getIntent(mContext).getRequest(versionCheckUrl,object: HttpCallListener {
            override fun Success(code:Int,data: String?) {
                try {
                    if (data != null) {
                        LogUtil.d("Login_checkVersion", "response:" + data);
                        var firVersionEntity: FirVersionEntity = Gson().fromJson(data, FirVersionEntity::class.java)
                        var fir_version = firVersionEntity.fir_version
                        var versionCode = SystemUtils.getIntent().getVersionCode(mContext);
                        LogUtil.e("Login_checkVersion", "versionCode:" + versionCode + "\nfir_server:" + fir_version);
                        if (fir_version > versionCode) {//
                            updateUrl = firVersionEntity.updateUrl
                            if (isRun) {
                                showVersionDialog()
                            }
                        } else {
                            LogUtil.d("Login", "same version")
                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            override fun Failure(code:Int,message: String?) {
                LogUtil.e("Login_checkVersion", "Failure:" );
            }
        })
    }

    class FirVersionEntity{
        var fir_version:Int = 0
        var updateUrl:String ?=null
    }



    /**下载APP
     * 使用OnDownloadListener回传
     * updateUrl 为新版本下载地址
     */
    fun downApk(fileback: FileDownloadListener) {
        HttpUtils.getIntent(mContext).fileDownload(updateUrl,"tieta.apk",fileback)
    }

    /**Dialog
     * 是否进行版本更新的请求
     */
    private fun showVersionDialog(){
        var builder: AlertDialog.Builder  = AlertDialog.Builder(mContext);
        builder.setMessage("这里有一个新的版本,建议你更新!");
        builder.setPositiveButton("更新",  DialogInterface.OnClickListener(){
            dialog, which ->
            dialog.dismiss()
            if(isRun) {
                showUpdateDialog()
            }
        })
        builder.setNegativeButton("取消",  DialogInterface.OnClickListener(){
            dialog,which->
            dialog.dismiss();
        });
        builder.show();
    }

    /**Dialog
     * 版本更新进度
     */
    private fun showUpdateDialog(){
        val builder = AlertDialog.Builder(mContext)
        val view = mContext.layoutInflater.inflate(R.layout.dialog_download_progress, null)
        var mProgress: ProgressBar = view.findViewById(R.id.progress)
        var mPresemt: TextView = view.findViewById(R.id.present)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
        var alertDialog: AlertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false);
        downApk(object:FileDownloadListener{
            override fun onDownloadSuccess(file: File) {
                mContext.runOnUiThread(Runnable {
                    alertDialog.dismiss()
                    if(isRun) {
                        installApk(file)
                    }
                })
            }
            override fun onDownloadFailed() {
                alertDialog.dismiss()
                mListener?.onUpdateFailure("更新失败，请检查后重试")
            }
            override fun onDownloading(progress: Int) {
                mContext.runOnUiThread(Runnable {
                    mProgress.progress = progress
                    mPresemt.text=progress.toString() +"%" //java.util.UnknownFormatConversionException: Conversion:
                })
            }
        })
        alertDialog.show()
    }

    /**
     * 安装app
     */
    protected fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val apkUri = FileProvider.getUriForFile(mContext, "com.elco.tieta.tieta.fileprovider", file)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            }catch (e:Exception){
                LogUtil.d("update",e.message)
                mListener?.onUpdateFailure("安装失败，请检查开启安装未知应用")
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if(isRun) {
            mContext.startActivity(intent)
        }
    }

    fun close(){
        isRun = false
    }
}
