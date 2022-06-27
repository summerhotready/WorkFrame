
package com.guoxd.work_frame_library.utils.viewinject;


import android.view.View;
import android.view.ViewGroup.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;

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
