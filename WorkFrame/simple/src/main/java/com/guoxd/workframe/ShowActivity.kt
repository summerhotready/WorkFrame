package com.guoxd.workframe

import android.content.Intent
import android.os.Bundle
import com.guoxd.workframe.base.ShowTextUrl
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.guoxd.workframe.my_page.*
import com.guoxd.workframe.system.AnimtorFragment
import com.guoxd.workframe.system.BLETestFragment
import com.guoxd.workframe.system.RecyclerViewFragment
import com.guoxd.workframe.system.TextWidgeFragment
import com.guoxd.workframe.utils.LogUtil
import kotlinx.android.synthetic.main.activity_show.*


/**加载页面的Activity
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
        showFragment(str)
    }

    fun setPageTitle(str: String){
        tv_title.setText(str)
    }
    fun showFragment(str:String){
        var fragment: Fragment?=null;
        when(str){
            //my
            ShowTextUrl.Widge->{//自定义基本组件
                fragment = ShowWidgeFragment()
            }
            ShowTextUrl.StaggeredList->{//瀑布流
                fragment = StaggeredListFragment()
            }
            ShowTextUrl.SwiptList->{//侧滑
                fragment = SwiptListFragment()
            }



//            ShowTextUrl.T

            ShowTextUrl.BitmapImage->{
                fragment = BitmapImageFragment()
            }
            ShowTextUrl.MenuWidge->{
                fragment = MenuWidgeFragment()
            }
            ShowTextUrl.PaintView->{
                fragment = PaintViewFragment();
            }
            ShowTextUrl.SlideBlock->{
                try {
                    val clazz = Class.forName(String.format("%s%s",ShowTextUrl.MY_PAGE,ShowTextUrl.SlideBlock)) //SlideBlockFragment()
                    fragment = clazz.newInstance() as Fragment;
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                }
            }

            ShowTextUrl.INTENT_SEND->{
                fragment =null
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
                finish()
            }

            //system
            ShowTextUrl.TextWidge->{
                fragment = TextWidgeFragment()
            }
            ShowTextUrl.RecyclerView->{
                fragment = RecyclerViewFragment()
            }
            ShowTextUrl.AnimtorView->{
                fragment = AnimtorFragment()
            }
            ShowTextUrl.BLEView->{
                fragment = BLETestFragment()
            }

            //other
            ShowTextUrl.OtherAnim->{
                try {
                    var animFragemnt = Class.forName("com.guoxd.workframe.others.${ShowTextUrl.OtherAnim}")
                    fragment = animFragemnt.newInstance() as Fragment;
                    LogUtil.d("","")
                }catch (e:Exception){
                    LogUtil.d("","")
                }
            }
            ShowTextUrl.OtherWidge->{
                try {
                    var animFragemnt = Class.forName("com.guoxd.workframe.others.${ShowTextUrl.OtherWidge}")
                    fragment = animFragemnt.newInstance() as Fragment;
                }catch (e:Exception){
                    LogUtil.d("","")
                }
            }
            ShowTextUrl.MpChar->{
                var clazz = Class.forName("com.guoxd.workframe.others."+ShowTextUrl.MpChar)
                fragment = clazz.newInstance() as Fragment;
            }
           /* ShowTextUrl.MpCharList->{
                var clazz = Class.forName("com.guoxd.workframe.others."+ShowTextUrl.MpCharList)
                fragment = clazz.newInstance() as Fragment;
            }*/

        }

        if(fragment !=null && fragment is Fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment, fragment).commit();
        }else{
            LogUtil.e("Show Fragmen","error:")
        }
    }

}