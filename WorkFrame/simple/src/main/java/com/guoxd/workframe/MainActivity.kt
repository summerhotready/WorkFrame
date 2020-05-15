package com.guoxd.workframe


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.guoxd.workframe.base.BaseActivity
import com.guoxd.workframe.main.MainMyWidgeFragment
import java.util.ArrayList



//程序入口
class MainActivity : BaseActivity() ,View.OnClickListener{
    var fragment_my: MainMyWidgeFragment?=null //自定义组件展示页面
    var fragment_system: MainMyWidgeFragment?=null//系统级别的组件使用展示/测试页面
    var fragment_other: MainMyWidgeFragment?=null//其他优秀的三方开源使用页面

    lateinit var viewMy:AppCompatTextView
    lateinit var viewOther:AppCompatTextView
    lateinit var viewSystem:AppCompatTextView

    var fragmentManager: FragmentManager?=null
    override fun getLayoutId(): Int {
       return R.layout.activity_main
    }

    override fun initView() {
        super.initView()
//        setContentView()
        setStateBarColor(ContextCompat.getColor(this,R.color.colorAccent),true)
        fragmentManager = supportFragmentManager
        var transaction = fragmentManager?.beginTransaction()
        fragment_my = MainMyWidgeFragment()
        var bundle:Bundle = Bundle()
        bundle.putString("tag","my")
        fragment_my?.arguments = bundle
        transaction?.add(R.id.fragment,fragment_my as Fragment)
        transaction?.commit()

        viewMy = findViewById(R.id.tv_my)
        viewMy.setOnClickListener(this)
        viewSystem = findViewById(R.id.tv_system)
        viewSystem.setOnClickListener(this)
        viewOther = findViewById(R.id.tv_other)
        viewOther.setOnClickListener(this)
    }

    fun List<String>.getM():List<String>{
        var result = this.toMutableList();
        return result;
    }
    fun genTodayLuck(): Pair<List<Map<String, String>>, List<Map<String, String>>>{
        val goodList = ArrayList<Map<String, String>>()
        val badList = ArrayList<Map<String, String>>()

        return Pair(goodList, badList)
    }

    //给TextView着色
    fun changeTextView(textView:AppCompatTextView,drawable: Drawable,isCheck:Boolean){

        var wrappedDrawable:Drawable = DrawableCompat.wrap(drawable)

        var colors:ColorStateList
        if(isCheck) {
            colors = ColorStateList.valueOf(Color.BLUE)// ColorStateList.valueOf(Color.parseColor("#03A9F4"))
        }else{
            colors = ColorStateList.valueOf(Color.GRAY)
        }
        DrawableCompat.setTintList(wrappedDrawable, colors)//系统方法
        //自定义方法
//        wrappedDrawable = tintDrawable(originalDrawable, ColorStateList.valueOf(Color.RED));
        textView.compoundDrawables

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tv_system->{
                var bundle:Bundle = Bundle()
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
            R.id.tv_other->{
                var bundle:Bundle = Bundle()
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
            R.id.tv_my->{
                var bundle:Bundle = Bundle()
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

    fun showFragment(fragment:MainMyWidgeFragment){
       /* if(fragment == null){
            fragment = MainMyWidgeFragment()

            fragment_system?.arguments =bundle
            transaction?.add(R.id.fragment,fragment_system)
            transaction?.commit()
        }else{
            fragment_system?.arguments =bundle
            transaction?.show(fragment_system)
            transaction?.commit()
        }*/
    }



}
