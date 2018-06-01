package com.guoxd.workframe.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoxd.work_frame_library.widges.CheckStart;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.views.MyItemDecoration;

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
        View root = inflater.inflate(R.layout.fragment_widge, container, false);

        CheckStart checkStart = root.findViewById(R.id.checkStart);
        checkStart.setMode(CheckStart.SELECT_START_BEGIN_CHECK);
        return root;
    }
}
