package com.guoxd.workframe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.guoxd.workframe.base.ShowTextUrl

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var strs= arrayOf(ShowTextUrl.Widge,ShowTextUrl.SlideBlock,ShowTextUrl.SwiptList,ShowTextUrl.StaggeredList);
        var listAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, strs);
        var listView:ListView = findViewById(R.id.listView);
        listView.adapter = listAdapter;

        listView.setOnItemClickListener(object:AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showActivity(strs[position]);
            }
        })
    }

    fun showActivity(str:String){
        var intent:Intent = Intent(this@MainActivity,ShowActivity::class.java);
        intent.putExtra("value",str)
        startActivity(intent)
    }
}
