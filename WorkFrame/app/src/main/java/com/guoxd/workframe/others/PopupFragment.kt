package com.guoxd.workframe.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guoxd.workframe.R
import com.guoxd.workframe.base.BaseFragment2
import com.guoxd.workframe.others.widge.ListPopWindow

/**
 * use to test xpop
 */
public class PopupFragment :BaseFragment2(){
    lateinit var listPopup:ListPopWindow

    override fun getCurrentLayoutID(): Int {
        return R.layout.other_fragment_anim;
    }

    override fun initView(root: View?) {
        super.initView(root)
    }

    override fun initData() {
        super.initData()
        //
        listPopup = ListPopWindow(activity)
    }

}
