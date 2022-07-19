package com.guoxd.work_frame_library.views.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guoxd.work_frame_library.R;

/**
 * Created by guoxd on 2018/10/25.
 * 包装进度条
 */

public class CustomProgress extends LinearLayout {

    TextView tv_name,tv_left,tv_right;
    ProgressBar pb;

    public CustomProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        //注意inflate的第三个参数要使用this,相当于把加载的这个view对象传给父类
        View.inflate(context, R.layout.view_custom_progress, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomProgress);
        String name = ta.getString(R.styleable.CustomProgress_pgText);

        tv_name = (TextView) findViewById(R.id.view_psv_tv_title);
        tv_left = (TextView) findViewById(R.id.view_psv_tv_left);
        tv_right = (TextView) findViewById(R.id.view_psv_tv_right);
        pb = (ProgressBar) findViewById(R.id.view_psv_progress);

        tv_name.setText(name);
        ta.recycle();
    }
    // 设置进度条左边的信息
    public void setTextLeft(String text) {
        tv_left.setText(text);
    }
    // 设置进度条右边的信息
    public void setTextRight(String text) {
        tv_right.setText(text);
    }
    // 设置进度条最大值
    public void setPbMax(int max) {
        pb.setMax(max);
    }
    // 设置进度条进度值
    public void setProgress(int progress) {
        pb.setProgress(progress);
    }
}
