package com.guoxd.workframe.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import com.guoxd.workframe.R
import com.guoxd.workframe.ShowActivity
import com.guoxd.workframe.base.BaseFragment
import com.guoxd.workframe.base.ShowTextUrl

/**recyclerView侧滑
 * item使用SwipeListLayout实现
 * AUTHOR: The_Android
 * DATE: 2018/4/24
 */
class MainMyWidgeFragment : BaseFragment() {


    override fun onRefresh() {

    }


    var listView: ListView ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.my_fragment_main, container, false)
        listView = root.findViewById(R.id.listView);

        initAdapter()
        return root
    }
    var strs:Array<String>?=null
    fun initAdapter(){

        var tag = arguments?.getString("tag")
        when(tag){
            "my"->{//自定义组件
                strs = arrayOf(ShowTextUrl.Widge,ShowTextUrl.PaintView,ShowTextUrl.MenuWidge, ShowTextUrl.SlideBlock, ShowTextUrl.SwiptList, ShowTextUrl.StaggeredList, ShowTextUrl.BitmapImage,ShowTextUrl.CameraView);
            }
            "other"->{//第三方
                strs = arrayOf(ShowTextUrl.OtherAnimWidge,ShowTextUrl.MpChar);
            }
            "system"->{//系统组件和功能测试
                strs = arrayOf(ShowTextUrl.RecyclerView,ShowTextUrl.TextWidge);
            }
        }

        var listAdapter: ArrayAdapter<String> = ArrayAdapter<String>(activity,android.R.layout.simple_expandable_list_item_1, strs);

        listView?.adapter = listAdapter;

        listView?.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showActivity(strs?.get(position)?:"");
            }
        })
    }
    fun showActivity(str:String){
        if(str.equals(""))
            return
        var intent: Intent = Intent(activity, ShowActivity::class.java);
        intent.putExtra("value",str)
        startActivity(intent)
    }

}
