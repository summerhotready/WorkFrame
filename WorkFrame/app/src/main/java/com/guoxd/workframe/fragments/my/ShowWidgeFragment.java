package com.guoxd.workframe.fragments.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoxd.work_frame_library.views.ChangeRingImageView;
import com.guoxd.work_frame_library.views.CustomProgress;
import com.guoxd.work_frame_library.widges.CheckStart;
import com.guoxd.work_frame_library.widges.ProgressShowView;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

import java.util.HashMap;

/**展示小尺寸自定义组件
 * Created by guoxd on 2018/5/8.
 */

public class ShowWidgeFragment extends BaseFragment {

    @Override
    public void onRefresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.my_fragment_show_widge, container, false);
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

        CustomProgress pb = (CustomProgress) root.findViewById(R.id.custom_pb);
        pb.setPbMax(100);
        pb.setProgress(30);
        pb.setTextLeft("已使用 30%");
        pb.setTextRight("未使用 70%");

        //
        ChangeRingImageView iv = root.findViewById(R.id.iv_changeRing);
//        iv.

        return root;
    }
}
