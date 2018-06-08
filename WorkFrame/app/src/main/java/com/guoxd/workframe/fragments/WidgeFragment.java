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
import com.guoxd.work_frame_library.widges.ProgressShowView;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.views.MyItemDecoration;

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
        View root = inflater.inflate(R.layout.fragment_widge, container, false);
        CheckStart checkStart = root.findViewById(R.id.checkStart);
        checkStart.setMax(10);
        checkStart.setProgress(2);

        ProgressShowView view = (ProgressShowView) root.findViewById(R.id.bgview);
        view.setMax(1440);
        //初始化颜色
        HashMap<Integer, Integer> colorMap = new HashMap<>();
        colorMap.put(0, android.R.color.transparent);
        colorMap.put(1, android.R.color.holo_blue_dark);
        colorMap.put(2, android.R.color.holo_green_light);
        //设置预设颜色
        view.setColor(colorMap);
        //设置data的颜色
        int[] array = new int[1440];
        for (int i = 0; i < 1440; i++) {
            array[i] = i%39 == 0? 3:((i > 229 && i<569) ? 2 : 1);
        }
        //设置data
        view.setData(array);
        return root;
    }
}
