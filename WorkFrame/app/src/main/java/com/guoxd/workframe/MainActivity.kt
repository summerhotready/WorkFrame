package com.guoxd.workframe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.guoxd.workframe.fragments.StaggeredListFragment
import com.guoxd.workframe.fragments.SwiptListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        var fragment: SwiptListFragment = SwiptListFragment();
        var fragment:StaggeredListFragment = StaggeredListFragment();
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment, fragment).commit();
    }
}
