package com.guoxd.workframe

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.guoxd.workframe.base.ShowTextUrl
import com.guoxd.workframe.fragments.my.SlideBlockFragment
import com.guoxd.workframe.fragments.my.StaggeredListFragment
import com.guoxd.workframe.fragments.my.SwiptListFragment
import android.support.v7.app.AppCompatDelegate
import android.widget.Toast
import com.guoxd.workframe.fragments.my.BitmapImageFragment
import com.guoxd.workframe.utils.LogUtil
import java.lang.reflect.Field


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
        LogUtil.d("ShowActivity","tag:"+str)
        setTitle(str)
        showFragment(str)
    }

    fun showFragment(str:String){
        var fragment: Fragment ?=null;
        when(str){
            ShowTextUrl.SlideBlock->{
                fragment = SlideBlockFragment()
            }
            ShowTextUrl.Widge->{
                fragment = com.guoxd.workframe.fragments.my.WidgeFragment()
            }
            ShowTextUrl.SwiptList->{
                fragment = SwiptListFragment()
            }
            ShowTextUrl.StaggeredList->{
                fragment = StaggeredListFragment()
            }
            ShowTextUrl.BitmapImage->{
                fragment = BitmapImageFragment()
            }
            ShowTextUrl.SystemWidge->{
                fragment = com.guoxd.workframe.fragments.system.WidgeFragment()
            }

            ShowTextUrl.OtherAnimWidge->{
                try {
                    var mToast: Toast = Toast(this)
                    var filed: Field = mToast.javaClass.getDeclaredField("mTN")
                    filed.isAccessible = true;
                    var obj = filed.get(mToast)
                    var showMethod = obj.javaClass.getDeclaredMethod("show", null);
                    var hideMethod = obj.javaClass.getDeclaredMethod("hide", null);
                    LogUtil.d("","")
                }catch (e:Exception){
                    LogUtil.d("","")
                }
//                fragment =
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment, fragment).commit();
    }
}