package com.guoxd.workframe.fragments.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import com.guoxd.work_frame_library.views.SwipeListLayout
import com.guoxd.workframe.R
import com.guoxd.workframe.ShowActivity
import com.guoxd.workframe.base.BaseFragment
import com.guoxd.workframe.base.ShowTextUrl
import com.guoxd.workframe.modles.DeviceModle
import com.guoxd.workframe.views.MyItemDecoration

import java.util.ArrayList

/**recyclerView侧滑
 * item使用SwipeListLayout实现
 * AUTHOR: The_Android
 * DATE: 2018/4/24
 */
class MainMyWidgeFragment : BaseFragment() {


    override fun onRefresh() {

    }

    var strs1= arrayOf(ShowTextUrl.Widge, ShowTextUrl.SlideBlock, ShowTextUrl.SwiptList, ShowTextUrl.StaggeredList, ShowTextUrl.BitmapImage);
    var strs2= arrayOf(ShowTextUrl.SystemWidge);

    var listView: ListView ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main_my, container, false)
        listView = root.findViewById(R.id.listView);

        initAdapter()
        return root
    }
    var strs:Array<String>?=null
    fun initAdapter(){

        if(arguments?.isEmpty?:true){
            strs = strs1
        }else{
            strs = strs2
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
