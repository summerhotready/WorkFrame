package com.guoxd.workframe.my_page;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.guoxd.work_frame_library.views.widges.ChangeRingImageView;
import com.guoxd.work_frame_library.views.progress.CustomProgress;
import com.guoxd.work_frame_library.views.widges.CheckStart;
import com.guoxd.work_frame_library.views.progress.ProgressShowView;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**展示小尺寸自定义组件
 * Created by guoxd on 2018/5/8.
 */
@TargetApi(9)
public class ShowWidgeFragment extends BaseFragment {


    @Override
    public void onRefresh() {
        Camera camera = Camera.open(0);

    }

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
        ChangeRingImageView iv =  (ChangeRingImageView)root.findViewById(R.id.iv_changeRing);

//Location.distanceBetween();

        return root;
    }


    //1、dingwei获取当前坐标 117.044388,39.384119
    //2、转换为GPS的WGS84坐标系
    //3、获取范围内的20个点
    //



    /**
     * 根据经纬度和半径计算经纬度范围
     *
     * @param raidus 单位米
     * @return minLat, minLng, maxLat, maxLng
     */
    public static double[] getAround(double lat, double lon, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;//半径距离

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[]{minLat, minLng, maxLat, maxLng};
    }
}
