package com.guoxd.workframe.my_page;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.view.View;


import androidx.appcompat.widget.AppCompatTextView;

import com.guoxd.work_frame_library.shape.ArrowSharpDrawable;
import com.guoxd.work_frame_library.views.progress.FreeProcessView;
import com.guoxd.work_frame_library.views.widges.ChangeRingImageView;
import com.guoxd.work_frame_library.views.progress.CustomProgress;
import com.guoxd.work_frame_library.views.widges.CheckStart;
import com.guoxd.work_frame_library.views.progress.ProgressShowView;
import com.guoxd.workframe.R;
import com.guoxd.workframe.ShowActivity;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.ViewHelpUtils;

import java.util.HashMap;

/**展示小尺寸自定义组件
 * Created by guoxd on 2018/5/8.
 */
@TargetApi(9)
public class ShowWidgeFragment extends BaseFragment {


    @Override
    protected int getCurrentLayoutID() {
        return R.layout.my_fragment_show_widge;
    }


    @Override
    protected void initView(View root) {
        setPageTitle("自定义基本组件");
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
//
        FreeProcessView freeProcessView = root.findViewById(R.id.freeProgress);
        freeProcessView.setTagBitmap(R.mipmap.icon_free_process);
        freeProcessView.setLimit(23,150);
        freeProcessView.setLimit(35);
        freeProcessView.setProgress(30);

        CustomProgress pb = (CustomProgress) root.findViewById(R.id.custom_pb);
        pb.setPbMax(100);
        pb.setProgress(30);
        pb.setTextLeft("已使用 30%");
        pb.setTextRight("未使用 70%");

        //
        ChangeRingImageView iv =  (ChangeRingImageView)root.findViewById(R.id.iv_changeRing);

//        shape
        float width =  ViewHelpUtils.dipTopx(getActivity(),10);
        float height = ViewHelpUtils.dipTopx(getActivity(),6);


        AppCompatTextView shapeLeft = root.findViewById(R.id.shapeLeft);
//        ArrowSharpDrawable arrowSharpDrawable = new ArrowSharpDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        ArrowSharpDrawable bgDrawable = new ArrowSharpDrawable(ArrowSharpDrawable.ArrowDirection.LEFT);
        bgDrawable.setCornerRadius(ViewHelpUtils.dipTopx(getActivity(),10));
        bgDrawable.setRelativePosition(0.3f);
        bgDrawable.setBgColor(Color.CYAN);
//        bgDrawable.setArrowDirection(ArrowSharpDrawable.ArrowDirection.BOTTOM);
//        bgDrawable.setArrowPath(  width,height);
        bgDrawable.setArrowCirclePath(ViewHelpUtils.dipTopx(getActivity(),5)
        ,ViewHelpUtils.dipTopx(getActivity(),30),width,height);


//        bgDrawable.setSharpSize(width,height);
//        创建弧度三角形
//        bgDrawable.setArrowPath(ArrowSharpDrawable.ArrowDirection.LEFT,width,height);
//        bgDrawable.setArrowCirclePath(ArrowSharpDrawable.ArrowDirection.LEFT,
//bgDrawable.setArrowCirclePath(ArrowSharpDrawable.ArrowDirection.LEFT,ViewHelpUtils.dipTopx(getActivity(),5)
//        ,ViewHelpUtils.dipTopx(getActivity(),30),width,height);

        shapeLeft.setBackground(bgDrawable);

        AppCompatTextView shapeTop = root.findViewById(R.id.shapeTop);
//        ArrowSharpDrawable arrowSharpDrawable = new ArrowSharpDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        ArrowSharpDrawable bgDrawableTop = new ArrowSharpDrawable(ArrowSharpDrawable.ArrowDirection.TOP);
        bgDrawableTop.setCornerRadius(ViewHelpUtils.dipTopx(getActivity(),10));
        bgDrawableTop.setRelativePosition(0.6f);
        bgDrawableTop.setBgColor(Color.CYAN);
//        bgDrawableTop.setArrowDirection(ArrowSharpDrawable.ArrowDirection.TOP);
//        bgDrawableTop.setSharpSize(width,height);
//        bgDrawableTop.setArrowPath(  width,height);
        bgDrawableTop.setArrowCirclePath(ViewHelpUtils.dipTopx(getActivity(),5)
                ,ViewHelpUtils.dipTopx(getActivity(),30),width,height);
        shapeTop.setBackground(bgDrawableTop);


        AppCompatTextView shapeRight = root.findViewById(R.id.shapeRight);
//        ArrowSharpDrawable arrowSharpDrawable = new ArrowSharpDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        ArrowSharpDrawable bgDrawableRight = new ArrowSharpDrawable(ArrowSharpDrawable.ArrowDirection.RIGHT);
        bgDrawableRight.setCornerRadius(ViewHelpUtils.dipTopx(getActivity(),10));
        bgDrawableRight.setRelativePosition(0.7f);
        bgDrawableRight.setBgColor(Color.CYAN);
//        bgDrawableRight.setArrowDirection(ArrowSharpDrawable.ArrowDirection.RIGHT);
//        bgDrawableRight.setSharpSize(width,height);
//        bgDrawableRight.setArrowPath(  width,height);
        bgDrawableRight.setArrowCirclePath(ViewHelpUtils.dipTopx(getActivity(),5)
                ,ViewHelpUtils.dipTopx(getActivity(),30),width,height);
        shapeRight.setBackground(bgDrawableRight);


        AppCompatTextView shapeBottom = root.findViewById(R.id.shapeBottom);
//        ArrowSharpDrawable arrowSharpDrawable = new ArrowSharpDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        ArrowSharpDrawable bgDrawableBottom = new ArrowSharpDrawable(ArrowSharpDrawable.ArrowDirection.BOTTOM);
        bgDrawableBottom.setCornerRadius(ViewHelpUtils.dipTopx(getActivity(),10));
        bgDrawableBottom.setRelativePosition(1f);
        bgDrawableBottom.setBgColor(Color.CYAN);
//        bgDrawableBottom.setArrowDirection(ArrowSharpDrawable.ArrowDirection.BOTTOM);
//        bgDrawableBottom.setSharpSize(width,height);
//        bgDrawableBottom.setArrowPath(  width,height);
        bgDrawableBottom.setArrowCirclePath(ViewHelpUtils.dipTopx(getActivity(),5)
                ,ViewHelpUtils.dipTopx(getActivity(),30),width,height);
        shapeBottom.setBackground(bgDrawableBottom);

//
        root.findViewById(R.id.change_pro_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        root.findViewById(R.id.change_pro_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(root.findViewById(R.id.change_pro_add).getVisibility()==View.GONE){
                    root.findViewById(R.id.change_pro_add).setVisibility(View.VISIBLE);
                }else {
                    root.findViewById(R.id.change_pro_add).setVisibility(View.GONE);
                }
            }
        });

        root.findViewById(R.id.change_limit_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        root.findViewById(R.id.change_limit_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(root.findViewById(R.id.change_limit_add).getVisibility()==View.GONE){
                    root.findViewById(R.id.change_limit_add).setVisibility(View.VISIBLE);
                }else {
                    root.findViewById(R.id.change_limit_add).setVisibility(View.GONE);
                }
            }
        });
//
//        AppCompatTextView textViewType = root.findViewById(R.id.noCopyEdit);
//        Typeface customFace = Typeface.createFromAsset(getActivity().getAssets(),"digital.ttf");
//        textViewType .setTypeface(customFace);
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
