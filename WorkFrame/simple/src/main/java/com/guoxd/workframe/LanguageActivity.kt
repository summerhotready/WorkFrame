package com.guoxd.workframe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.guoxd.workframe.utils.LanguageUtils
import com.guoxd.workframe.utils.LogUtil
import kotlinx.android.synthetic.main.setting_activity_language.*


/**加载页面的Activity
 * Created by guoxd on 2020/12/3
 */
class LanguageActivity : AppCompatActivity(){
    var TAG = "LanguageActivity"
    var isCn = true;
    var defaultLangeage = true;
    lateinit var mActivity:AppCompatActivity;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        isCn = LanguageUtils.appLanguageIsChinese()
        defaultLangeage = isCn
        LogUtil.i(TAG,"init isCn?:"+isCn)
        LanguageUtils.applyAppLanguage(mActivity)

        setContentView(R.layout.setting_activity_language)

        changeView()
        lang_en.setOnClickListener {
            if(isCn) {
                isCn = false;
                changeView();
                LanguageUtils.changeLanguage(mActivity,isCn)
                reload()
            }
        }
        lang_cn.setOnClickListener {
            if(!isCn){
                isCn = true;
                changeView()
                LanguageUtils.changeLanguage(mActivity,isCn)
                reload()
            }
        }
        btn_sure.setOnClickListener {
            finish()
//            var intent = Intent(mActivity,MainActivity::class.java)
//            startActivity(intent)
        }
//系统 短信息
        btn_sendMsg.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
//            携带的内容，跳转到选择联系人，如果不选文字也不会保留
        }
        btn_showConn.setOnClickListener{
            val showConnInten = Intent();
            showConnInten.action = Intent.ACTION_PICK
            showConnInten.setType(ContactsContract.Contacts.CONTENT_TYPE);
            startActivity(showConnInten)
        }

        btn_sendEmail.setOnClickListener {
            var intent =  Intent(Intent.ACTION_SEND);

            intent.putExtra(Intent.EXTRA_EMAIL, "admin@android-study.com");
            intent.putExtra(Intent.EXTRA_CC, "webmaster@android-study.com");
            intent.putExtra(Intent.EXTRA_TEXT, "I come from http://www.android-study.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "http://www.android-study.com");intent.setType("message/rfc882");
            startActivity(Intent.createChooser(intent, "选择邮箱"));
        }
    }

    fun resetLanageag(){
        if(defaultLangeage != isCn){
            LanguageUtils.changeLanguage(mActivity,defaultLangeage)
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            resetLanageag();
        }
        return super.onKeyDown(keyCode, event)
    }

    fun reload(){
        title_dialog.setText(getString(R.string.langeage_title))
        btn_sure.setText(getString(R.string.language_button_test))
        lang_en.setText(getString(R.string.language_en))
        lang_cn.setText(getString(R.string.language_cn))
    }
    fun changeView(){
        if(isCn){
//            lang_cn.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white))
            lang_cn.setBackgroundResource(R.color.colorPrimary)
//            lang_en.setTextColor(ContextCompat.getColor(mActivity,R.color.text_color_detail))
            lang_en.setBackgroundResource(R.color.text_color_detail)
        }else{
//            lang_en.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white))
            lang_en.setBackgroundResource(R.color.colorPrimary)
//            lang_cn.setTextColor(ContextCompat.getColor(mActivity,R.color.text_color_detail))
            lang_cn.setBackgroundResource(R.color.text_color_detail)
        }
    }

}