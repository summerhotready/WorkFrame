package com.guoxd.workframe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.guoxd.workframe.base.MainApplication
import com.guoxd.workframe.utils.LanguageUtils
import com.guoxd.workframe.utils.LogUtil
import kotlinx.android.synthetic.main.setting_activity_language.*


/**加载页面的Activity
 * Created by guoxd on 2020/12/3
 */
class LanguageActivity : AppCompatActivity(){
    var TAG = "LanguageActivity"
    var isCn = true;
    lateinit var mActivity:AppCompatActivity;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        isCn = LanguageUtils.appLanguageIsChinese()
        LogUtil.i(TAG,"init isCn?:"+isCn)
        LanguageUtils.applyAppLanguage(mActivity)

        setContentView(R.layout.setting_activity_language)

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
            var intent = Intent(mActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun reload(){
        btn_sure.setText(getString(R.string.language_button_test))
    }
    fun changeView(){
        if(isCn){
            lang_cn.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white))
            lang_cn.setBackgroundResource(R.color.colorPrimary)
            lang_en.setTextColor(ContextCompat.getColor(mActivity,android.R.color.darker_gray))
            lang_en.setBackgroundResource(R.color.colorPrimaryDark)
        }else{
            lang_en.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white))
            lang_en.setBackgroundResource(R.color.colorPrimary)
            lang_cn.setTextColor(ContextCompat.getColor(mActivity,android.R.color.darker_gray))
            lang_cn.setBackgroundResource(R.color.colorPrimaryDark)
        }
    }

}