package com.guoxd.workframe.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxd.work_frame_library.widges.SlideBlock;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

/**
 * Created by guoxd on 2018/5/16.
 * 用于显示组件SlideBlock
 */

public class SlideBlockFragment extends BaseFragment {
    final String TAG="SlideBlockFragment";
    @Override
    public void onRefresh() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideblock, container, false);
        Log.i(TAG,"ScanWidth:"+getActivity().getResources().getDisplayMetrics().widthPixels);
        Log.i(TAG,"ScanHeight:"+getActivity().getResources().getDisplayMetrics().heightPixels);
        SlideBlock blocks = (SlideBlock)root.findViewById(R.id.blocks);
//        blocks.setDefaultTableMode(SlideBlock.COLUME);
        return root;
    }
}
