package com.guoxd.workframe


import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.guoxd.workframe.main.MainMyWidgeFragment
//程序入口
class MainActivity : AppCompatActivity() ,View.OnClickListener{
    var fragment_my: MainMyWidgeFragment?=null //自定义组件展示页面
    var fragment_system: MainMyWidgeFragment?=null//系统级别的组件使用展示/测试页面
    var fragment_other: MainMyWidgeFragment?=null//其他优秀的三方开源使用页面

    var fragmentManager: FragmentManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager = supportFragmentManager
        var transaction = fragmentManager?.beginTransaction()
        fragment_my = MainMyWidgeFragment()
        var bundle:Bundle = Bundle()
        bundle.putString("tag","my")
        fragment_my?.arguments = bundle
        transaction?.add(R.id.fragment,fragment_my as Fragment)
        transaction?.commit()

        findViewById<TextView>(R.id.tv_my).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_system).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_other).setOnClickListener(this)
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
