package com.guoxd.workframe

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.guoxd.workframe.base.ShowTextUrl
import com.guoxd.workframe.fragments.SlideBlockFragment
import com.guoxd.workframe.fragments.StaggeredListFragment
import com.guoxd.workframe.fragments.SwiptListFragment
import com.guoxd.workframe.fragments.WidgeFragment
import android.support.v7.app.AppCompatDelegate



/**
 * Created by guoxd on 2018/5/23.
 */
class ShowActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        //svg 4.0~5.0
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        var str = intent.getStringExtra("value")
        var title:TextView = findViewById(R.id.title)
        title.setText(str);
        showFragment(str)
    }

    fun showFragment(str:String){
        var fragment: Fragment ?=null;
        when(str){
            ShowTextUrl.SlideBlock->{
                fragment = SlideBlockFragment()
            }
            ShowTextUrl.Widge->{
                fragment = WidgeFragment()
            }
            ShowTextUrl.SwiptList->{
                fragment = SwiptListFragment()
            }
            ShowTextUrl.StaggeredList->{
                fragment = StaggeredListFragment()
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment, fragment).commit();
    }
}