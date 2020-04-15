package com.guoxd.workframe.others.widge

import android.app.Activity
import android.view.View
import com.guoxd.workframe.R
import razerdp.basepopup.BasePopupWindow

class TestBasePop(var mContext: Activity) :BasePopupWindow(mContext) {
    override fun onCreateContentView(): View {
        return createPopupById(R.layout.pop_sharp_bg_popupwindow)
    }
}