package com.guoxd.workframe.my_page;

import android.view.View;

import com.guoxd.work_frame_library.views.paint.PaintSurfaceview;
import com.guoxd.workframe.R;
import com.guoxd.workframe.ShowActivity;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.PointMenuViewUtils;

/**画板
 * Created by guoxd on 2018/10/25.
 */

public class PaintViewFragment extends BaseFragment {

    @Override
    protected int getCurrentLayoutID() {
        return R.layout.my_fragemnt_paintview;
    }

    @Override
    protected void initView(View root) {
        setPageTitle("画板");
        final PaintSurfaceview paintView = root.findViewById(R.id.paintView);
        root.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });
    }
}
