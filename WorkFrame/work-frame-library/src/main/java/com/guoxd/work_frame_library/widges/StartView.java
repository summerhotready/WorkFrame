package com.guoxd.work_frame_library.widges;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.View;

import com.guoxd.work_frame_library.R;
import com.guoxd.work_frame_library.listeners.StartOnClickListener;

/**
 * Created by guoxd on 2018/5/25.
 * checkStart's base
 */

public class StartView extends AppCompatCheckedTextView{

    public StartView(Context mContext,int mStartSize){
        super(mContext);
        initView(mStartSize);
    }

    private void initView(int mStartSize) {
        setWidth(mStartSize);
        setHeight(mStartSize);
    }

    public void setTag(String tag){
        setTag(tag);
    }

        /**
     *
     * @param b
     */
    public void startChecked(boolean b){
        setChecked(b);
        setBackgroundResource(b?R.drawable.start_chedked:R.drawable.start_uncheck);
    }

    public void setStartViewClickListener(final StartOnClickListener listener){
        setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               startChecked(isChecked());
               listener.onClick(isChecked(),getTag().toString());
           }
       });
    }
}
