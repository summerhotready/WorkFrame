package com.guoxd.workframe.system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guoxd.work_frame_library.views.paint.PaintSurfaceview;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.PointMenuViewUtils;

/**
 * Created by guoxd on 2019/6/27.
 * 练习 property animtor
 */

public class AnimtorFragment extends BaseFragment {

    @Override
    protected int getCurrentLayoutID() {
        return R.layout.my_fragemnt_paintview;
    }

    @Override
    protected void initView(View root) {
        final PaintSurfaceview paintView = root.findViewById(R.id.paintView);
        root.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });
    }



}
