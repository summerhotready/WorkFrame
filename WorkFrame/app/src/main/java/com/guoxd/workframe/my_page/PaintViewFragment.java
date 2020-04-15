package com.guoxd.workframe.my_page;

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

/**画板
 * Created by guoxd on 2018/10/25.
 */

public class PaintViewFragment extends BaseFragment {
    @Override
    public void onRefresh() {

    }

    PointMenuViewUtils utils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.my_fragemnt_paintview,container,false);
        final PaintSurfaceview paintView = root.findViewById(R.id.paintView);
        root.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });
        return root;
    }



}
