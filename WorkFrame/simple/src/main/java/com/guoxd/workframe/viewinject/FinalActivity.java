
package com.guoxd.workframe.viewinject;


import android.view.View;
import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
//基于afinal。baseActivity需要继承此activity
public abstract class FinalActivity extends AppCompatActivity {

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        AnnotationUtils.initInjectedView(this, FinalActivity.class);
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        AnnotationUtils.initInjectedView(this, FinalActivity.class);
    }

    public void setContentView(View view) {
        super.setContentView(view);
        AnnotationUtils.initInjectedView(this, FinalActivity.class);
    }

}
