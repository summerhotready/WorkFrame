package com.guoxd.workframe.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.guoxd.workframe.LanguageActivity
import com.guoxd.workframe.R
import com.guoxd.workframe.ShowActivity
import com.guoxd.workframe.base.BaseActivity
import com.guoxd.workframe.base.BaseFragment
import com.guoxd.workframe.base.MainApplication
import com.guoxd.workframe.base.ShowTextUrl
import com.guoxd.workframe.my_page.data_binding.DataBindingTestActivity
import com.guoxd.workframe.utils.LogUtil
import com.guoxd.workframe.utils.PermissionUtils
import com.guoxd.workframe.utils.ToastUtils
import pub.devrel.easypermissions.EasyPermissions

/**recyclerView侧滑
 * item使用SwipeListLayout实现
 * AUTHOR: The_Android
 * DATE: 2018/4/24
 */
class MainMyWidgeFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    var listView: ListView ?=null
    lateinit var bg:View

    override fun getCurrentLayoutID(): Int {
        return R.layout.my_fragment_main
    }

    override fun initView(root: View) {
        bg = root.findViewById(R.id.ll_bg)
        listView = root.findViewById(R.id.listView) as ListView;

        initAdapter()
    }

    var strs:Array<String>?=null
    fun initAdapter(){
        var tag = arguments?.getString("tag")
        when(tag){
            "my"->{//自定义组件
                strs = arrayOf(
                        ShowTextUrl.Widge,
                        ShowTextUrl.StaggeredList,
                        ShowTextUrl.PaintView,ShowTextUrl.MenuWidge,ShowTextUrl.SlideBlock//ShowTextUrl.BitmapImage,
                        );//
                bg.setBackgroundColor(Color.parseColor("#3fFBC72B"))

            }
            "other"->{//第三方
                strs = arrayOf(ShowTextUrl.OtherWidge,ShowTextUrl.OtherAnim,ShowTextUrl.MpChar,ShowTextUrl.OtherDataBinding,ShowTextUrl.SwiptList,ShowTextUrl.GaodeMap);
                bg.setBackgroundColor(Color.parseColor("#3f99cc00"))
            }
            "system"->{//系统组件和功能
                strs = arrayOf(ShowTextUrl.TextWidge,ShowTextUrl.RecyclerView,ShowTextUrl.BLEView,ShowTextUrl.LANGUAGE,ShowTextUrl.INTENT_SEND);
                bg.setBackgroundColor(Color.parseColor("#3faa66cc"))
            }
        }

        var listAdapter: ArrayAdapter<String> = ArrayAdapter<String>(activity as BaseActivity,android.R.layout.simple_expandable_list_item_1, strs as Array<String>);

        listView?.adapter = listAdapter;

        listView?.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showActivity(strs?.get(position)?:"");
            }
        })
    }
    fun showActivity(str:String){
        if(TextUtils.isEmpty(str))
            return
        when(str){
            ShowTextUrl.OtherDataBinding->{
                if(EasyPermissions.hasPermissions(activity?.baseContext!!, *PermissionUtils.getCameraPermiss())){
                    startActivity(Intent(activity, DataBindingTestActivity::class.java))
                }else{
                    EasyPermissions.requestPermissions(this@MainMyWidgeFragment,"",REQUEST_CODE_PERMS_CAMERA,*PermissionUtils.getCameraPermiss())
                }
            }

            ShowTextUrl.INTENT_SEND->{
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            ShowTextUrl.LANGUAGE->{
                var languageIntent = Intent(activity,LanguageActivity::class.java)
                startActivity(languageIntent)
                activity?.finish()
            }

            else->{
                try {
                    var intent: Intent = Intent(activity, ShowActivity().javaClass);
                    intent.putExtra("value", str)
                    startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    val REQUEST_CODE_PERMS_EXTERNAL_STORAGE = 101
    val REQUEST_CODE_PERMS_CAMERA = 102
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CODE_PERMS_CAMERA) {
            if(activity !=null) {
                getAppDetailSettingIntent(requireActivity())
            }
        }
        ToastUtils.showMsgToast(activity,"应用缺少权限，请到系统设置页面进行设置")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when(requestCode){
            REQUEST_CODE_PERMS_CAMERA->
                startActivity(Intent(activity, DataBindingTestActivity::class.java))
            else->{}
        }
    }

    private fun getAppDetailSettingIntent(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("应用需要使用存储权限用来保存一些数据,禁止可能会导致某些异常")
        builder.setPositiveButton("去设置") { _, _ ->
            val localIntent = Intent()
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", activity?.getPackageName(), null)
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.action = Intent.ACTION_VIEW
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
                localIntent.putExtra("com.android.settings.ApplicationPkgName", activity?.getPackageName())
            }
            startActivity(localIntent)
        }
        builder.show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        LogUtil.d("Permission", "onRequestPermissionsResult:$requestCode")
    }
}
