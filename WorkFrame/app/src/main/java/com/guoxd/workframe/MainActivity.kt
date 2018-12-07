package com.guoxd.workframe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.*
import com.guoxd.workframe.fragments.main.MainMyWidgeFragment

class MainActivity : AppCompatActivity() ,View.OnClickListener{

    var fragment_my: MainMyWidgeFragment?=null
    var fragment_system: MainMyWidgeFragment?=null
    var fragment_other: MainMyWidgeFragment?=null

    var fragmentManager: FragmentManager ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager
        var transaction = fragmentManager?.beginTransaction()
        fragment_my = MainMyWidgeFragment()
        var bundle:Bundle = Bundle()
        bundle.putString("tag","my")
        fragment_my?.arguments = bundle
        transaction?.add(R.id.fragment,fragment_my)
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
                    transaction?.add(R.id.fragment,fragment_system)
                    transaction?.commit()
                }else{
                    fragment_system?.arguments =bundle
                    transaction?.show(fragment_system)
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
                    transaction?.add(R.id.fragment,fragment_other)
                    transaction?.commit()
                }else{
                    fragment_other?.arguments =bundle
                    transaction?.show(fragment_other)
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
                    transaction?.add(R.id.fragment,fragment_my)
                    transaction?.commit()
                }else{
                    fragment_my?.arguments =bundle
                    transaction?.show(fragment_my)
                    transaction?.commit()
                }
            }
        }
    }

    fun hindFragment(fragment:Fragment?){
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
