package com.guoxd.workframe.my_page;

import android.view.View;

import com.guoxd.work_frame_library.views.widges.SlideBlock;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

/**
 * Created by guoxd on 2018/5/16.
 * 用于显示组件SlideBlock
 */

public class SlideBlockFragment extends BaseFragment {
    final String TAG="SlideBlockFragment";

    @Override
    protected int getCurrentLayoutID() {
        return R.layout.my_fragment_slideblock;
    }

    @Override
    protected void initView(View root) {
        getBaseActity().setPageTitle("滑动滑块，待完善");
        SlideBlock blocks = (SlideBlock)root.findViewById(R.id.blocks);
//        blocks.setDefaultTableMode(SlideBlock.COLUME);

    }
}
