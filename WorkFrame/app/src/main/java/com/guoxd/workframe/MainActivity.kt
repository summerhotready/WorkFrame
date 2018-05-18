package com.guoxd.workframe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.guoxd.workframe.fragments.SlideBlockFragment
import com.guoxd.workframe.fragments.StaggeredListFragment
import com.guoxd.workframe.fragments.SwiptListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var title:TextView = findViewById(R.id.tv_title)


//        var fragment: SwiptListFragment = SwiptListFragment();
//        var fragment:StaggeredListFragment = StaggeredListFragment();
        var fragment:SlideBlockFragment = SlideBlockFragment()
        title.setText("SlideBlocks")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment, fragment).commit();
    }
}
