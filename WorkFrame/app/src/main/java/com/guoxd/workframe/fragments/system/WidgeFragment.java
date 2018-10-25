package com.guoxd.workframe.fragments.system;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoxd.work_frame_library.widges.CheckStart;
import com.guoxd.work_frame_library.widges.ProgressShowView;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

import java.util.HashMap;

/**
 * Created by guoxd on 2018/5/8.
 */

public class WidgeFragment extends BaseFragment {

    @Override
    public void onRefresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_widge_system, container, false);


        return root;
    }
}
