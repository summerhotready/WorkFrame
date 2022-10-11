package com.guoxd.workframe


import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.guoxd.workframe.base.BaseActivity
import com.guoxd.workframe.main.MainMyWidgeFragment

//程序入口
class MainActivity : BaseActivity() ,View.OnClickListener{
    var fragment_my: MainMyWidgeFragment?=null //自定义组件展示页面
    var fragment_system: MainMyWidgeFragment?=null//系统级别的组件使用展示/测试页面
    var fragment_other: MainMyWidgeFragment?=null//其他优秀的三方开源使用页面

    lateinit var textMy:AppCompatTextView
    lateinit var textOther:AppCompatTextView
    lateinit var textSystem:AppCompatTextView
    lateinit var imageMy:AppCompatImageView
    lateinit var imageOther:AppCompatImageView
    lateinit var imageSystem:AppCompatImageView

    var fragmentManager: FragmentManager?=null
    var current = R.id.my_tv;
    lateinit var colorDafault:ColorStateList;
    lateinit var colorChange:ColorStateList;

    override fun getLayoutId(): Int {
       return R.layout.activity_main
    }

    override fun initView() {
        super.initView()
//        setStateBarColor(ContextCompat.getColor(this,R.color.colorPrimary),true)
        fragmentManager = supportFragmentManager
        var transaction = fragmentManager?.beginTransaction()
        fragment_my = MainMyWidgeFragment()
        var bundle:Bundle = Bundle()
        bundle.putString("tag","my")
        fragment_my?.arguments = bundle
        transaction?.add(R.id.fragment,fragment_my as Fragment)
        transaction?.commit()

        textMy = findViewById(R.id.my_tv)
        textMy.setOnClickListener(this)
        imageMy= findViewById(R.id.my_image)
        imageMy.setOnClickListener(this)
        textSystem = findViewById(R.id.system_tv)
        textSystem.setOnClickListener(this)
        imageSystem= findViewById(R.id.system_image)
        imageMy.setOnClickListener(this)
        textOther = findViewById(R.id.other_tv)
        textOther.setOnClickListener(this)
        imageOther= findViewById(R.id.other_image)
        imageOther.setOnClickListener(this)

            colorDafault = ContextCompat.getColorStateList(this, R.color.text_color_detail)!!
            colorChange = ContextCompat.getColorStateList(this, R.color.colorPrimary)!!

        toChanged(imageMy,textMy)
        toUnChanged(imageOther,textOther)
        toUnChanged(imageSystem,textSystem)
    }

    fun toUnChanged(imageView: AppCompatImageView,textView: AppCompatTextView){
        imageView.imageTintList = colorDafault
        textView.setTextColor(colorDafault)
    }
    fun toChanged(imageView: AppCompatImageView,textView: AppCompatTextView){
        imageView.imageTintList = colorChange
        textView.setTextColor(colorChange)
    }

    fun changeTag(changeTo:Int){
        when(current){
            R.id.system_tv,R.id.system_image->{
                toUnChanged(imageSystem,textSystem)
            }
            R.id.my_tv,R.id.my_image->{
                toUnChanged(imageMy,textMy)
            }
            R.id.other_tv,R.id.other_image->{
                toUnChanged(imageOther,textOther)
            }
        }
        when(changeTo){
            R.id.system_tv,R.id.system_image->{
                toChanged(imageSystem,textSystem)
            }
            R.id.my_tv,R.id.my_image->{
                toChanged(imageMy,textMy)
            }
            R.id.other_tv,R.id.other_image->{
                toChanged(imageOther,textOther)
            }
        }
        current = changeTo
    }

    override fun onClick(v: View) {
        if(current == v.id) {
            return
        }
        changeTag(v.id)
        when(v.id){
            R.id.system_tv,R.id.system_image->{
                var bundle = Bundle()
                bundle.putString("tag","system")
                hindFragment(fragment_my)
                hindFragment(fragment_other)
                var transaction = fragmentManager?.beginTransaction()
                if(fragment_system == null){
                    fragment_system = MainMyWidgeFragment()
                    fragment_system?.arguments =bundle
                    transaction?.add(R.id.fragment,fragment_system as Fragment)
                    transaction?.commit()
                }else{
                    fragment_system?.arguments =bundle
                    transaction?.show(fragment_system as Fragment)
                    transaction?.commit()
                }

            }
            R.id.other_tv,R.id.other_image->{
                var bundle = Bundle()
                bundle.putString("tag","other")
                hindFragment(fragment_my)
                hindFragment(fragment_system)
                var transaction = fragmentManager?.beginTransaction()
                if(fragment_other == null){
                    fragment_other = MainMyWidgeFragment()
                    fragment_other?.arguments =bundle
                    transaction?.add(R.id.fragment,fragment_other as Fragment)
                    transaction?.commit()
                }else{
                    fragment_other?.arguments =bundle
                    transaction?.show(fragment_other as Fragment)
                    transaction?.commit()
                }
            }
            R.id.my_tv,R.id.my_image->{
                var bundle = Bundle()
                bundle.putString("tag","my")
                hindFragment(fragment_other)
                hindFragment(fragment_system)
                var transaction = fragmentManager?.beginTransaction()
                if(fragment_my == null){
                    fragment_my = MainMyWidgeFragment()
                    fragment_my?.arguments =bundle
                    transaction?.add(R.id.fragment,fragment_my as Fragment)
                    transaction?.commit()
                }else{
                    fragment_my?.arguments =bundle
                    transaction?.show(fragment_my as Fragment)
                    transaction?.commit()
                }
            }
        }
    }

    fun hindFragment(fragment: Fragment?){
        if(fragment !=null){
            var transaction = fragmentManager?.beginTransaction()
            transaction?.hide(fragment)
            transaction?.commit()
        }
    }

}
