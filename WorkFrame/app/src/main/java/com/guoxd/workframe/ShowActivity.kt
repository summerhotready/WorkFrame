package com.guoxd.workframe

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.guoxd.workframe.base.ShowTextUrl
import android.support.v7.app.AppCompatDelegate
import com.guoxd.workframe.base.BaseFragment
import com.guoxd.workframe.fragments.my.*
import com.guoxd.workframe.fragments.system.RecyclerViewFragment
import com.guoxd.workframe.fragments.system.TextWidgeFragment
import com.guoxd.workframe.utils.LogUtil


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
            //my
            ShowTextUrl.BitmapImage->{
                fragment = BitmapImageFragment()
            }
            ShowTextUrl.CameraView->{
                fragment = CameraViewFragment()
            }
            ShowTextUrl.MenuWidge->{
                fragment = MenuWidgeFragment()
            }
            ShowTextUrl.PaintView->{
                fragment = PaintViewFragment();
            }
            ShowTextUrl.SlideBlock->{
                try {
                    val clazz = Class.forName(ShowTextUrl.SlideBlock) //SlideBlockFragment()
                    fragment = clazz.newInstance() as Fragment;
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: java.lang.IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: java.lang.InstantiationException) {
                    e.printStackTrace()
                }
            }
            ShowTextUrl.StaggeredList->{
                fragment = StaggeredListFragment()
            }
            ShowTextUrl.SwiptList->{
                fragment = SwiptListFragment()
            }
            ShowTextUrl.Widge->{
                fragment = ShowWidgeFragment()
            }

            //system
            ShowTextUrl.TextWidge->{
                fragment = TextWidgeFragment()
            }
            ShowTextUrl.RecyclerView->{
                fragment = RecyclerViewFragment()
            }

            //other
            ShowTextUrl.OtherAnimWidge->{
                try {
                    var animFragemnt = Class.forName("com.guoxd.workframe.fragments.others."+ShowTextUrl.OtherAnimWidge)
                    fragment = animFragemnt.newInstance() as Fragment;
                   /* var mToast: Toast = Toast(this)
                    var filed: Field = mToast.javaClass.getDeclaredField("mTN")
                    filed.isAccessible = true;
                    var obj = filed.get(mToast)
                    var showMethod = obj.javaClass.getDeclaredMethod("show", null);
                    var hideMethod = obj.javaClass.getDeclaredMethod("hide", null);*/
                    LogUtil.d("","")
                }catch (e:Exception){
                    LogUtil.d("","")
                }
            }
            ShowTextUrl.MpChar->{
                var clazz = Class.forName("com.guoxd.workframe.fragments.others."+ShowTextUrl.MpChar)
                fragment = clazz.newInstance() as Fragment;
            }
        }
        if(fragment !=null && fragment is BaseFragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment, fragment).commit();
        }else{
            LogUtil.d("Show Fragmen","error:")
        }
    }

}