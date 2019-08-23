package com.guoxd.workframe.others;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.LogUtil;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**第三方图标库 （反射调用）
 * Created by guoxd on 2018/5/8.
 */

public class MPCharFragment extends BaseFragment {

    final String TAG="other.MPCharFragment";
    @Override
    public void onRefresh() {

    }

    LineChart lineChart;
    PieChart pieChart;
    Timer mTimer;
    TimerTask mTimerTask;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.other_fragment_mpchar, container, false);

        lineChart = (LineChart)root.findViewById(R.id.line_chart);
        initChart(lineChart);
        pieChart = (PieChart)root.findViewById(R.id.pie_chart);
        initPie(pieChart);


        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG,String.format("******** new Line **********"));
                mHandler.sendEmptyMessage(1);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask,2,3*1000);
        setDate();

        return root;
    }
    void initChart(LineChart mLineChart){
        //设置没有数据时显示的文本
        mLineChart.setNoDataText("没有数据喔~~");
        //没有数据时显示文字的颜色
        mLineChart.setNoDataTextColor(Color.BLUE);
        //chart 绘图区后面的背景矩形将绘制
        mLineChart.setDrawGridBackground(false);
        //设置是否绘制chart边框的线
        mLineChart.setDrawBorders(true);
        //设置chart边框线颜色
        mLineChart.setBorderColor(Color.GRAY);
        //设置chart边框线宽度(单位 dp)
        mLineChart.setBorderWidth(1f);

        //设置chart动画
        mLineChart.animateXY(1000, 1000);
        //打印日志
        lineChart.setLogEnabled(true);
        //======== 交互 =========
        mLineChart.setTouchEnabled(true);// 设置是否可以触摸
//        lineChart.setDragEnabled(true);// 是否可以拖拽
        //设置是否可以缩放 x和y，
        mLineChart.setScaleEnabled(true);// 是否可以缩放 x和y轴, 默认是true
        lineChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(true); //是否可以缩放 仅y轴
        lineChart.setPinchZoom(true);  //设置x轴和y轴能否同时缩放。默认是否
        lineChart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true
        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。

        //setting X-轴参数
        XAxis xAxis = lineChart.getXAxis();
        //是否启用X轴
        xAxis.setEnabled(true);
        //设置x轴标签数（注意数据会水平分割，所以此数据与一定要精确）
        xAxis.setLabelCount(10, true);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        //是否绘制X轴线
        xAxis.setDrawAxisLine(true);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);
        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);
        //设置X轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置竖线为虚线样式
        // xAxis.enableGridDashedLine(10f, 10f, 0f);
        //当设置为true，确保图表第一个和最后一个label数据不超出左边和右边的Y轴(注意源码有bug，x轴点位为基数个时最后一位失效，需要继承使用)
        xAxis.setAvoidFirstLastClipping(true);

        //======== setting Y-轴 =========
        //设置左边的Y轴
        YAxis axisLeft = lineChart.getAxisLeft();
        //是否启用左边Y轴
        axisLeft.setEnabled(true);
        //设置横向的线为虚线
        axisLeft.enableGridDashedLine(10f, 10f, 0f);
        //axisLeft.setDrawLimitLinesBehindData(true);
        //是否绘制0所在的网格线
        axisLeft.setDrawZeroLine(false);

        //设置右边的Y轴
        YAxis axisRight = lineChart.getAxisRight();
        //是否启用右边Y轴
        axisRight.setEnabled(false);
        //设置横向的线为虚线
//        axisRight.enableGridDashedLine(10f, 10f, 0f);

        //设置图例
        Legend l = lineChart.getLegend();//图例
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);//设置图例的位置
        l.setTextSize(10f);//设置文字大小
        l.setForm(Legend.LegendForm.CIRCLE);//正方形，圆形或线
        l.setFormSize(10f); // 设置Form的大小
        l.setWordWrapEnabled(true);//是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        l.setFormLineWidth(10f);//设置Form的宽度
        l.setXEntrySpace(0f);//横向间距
        l.setYEntrySpace(8f);//纵向行距

        //设置基准线
        LimitLine ll = new LimitLine(3f, "Limit");
        //線條顏色、寬度
        ll.setLineColor(getResources().getColor(R.color.colorAccent));
        ll.setLineWidth(2f);
        //文字位置
        ll.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        //文字顏色、大小
        ll.setTextColor(getResources().getColor(R.color.colorAccent));
        ll.setTextSize(12f);//
        //加入到 mXAxis 或 mYAxis
        xAxis.addLimitLine(ll);//放在位置3（第四个）上
        axisLeft.addLimitLine(ll); //放在值=3上，YAxis可以只選一邊設定就好

    }
    private void  setDate(){
        /**  Entry 坐标点对象  构造函数 第一个参数为x点坐标 第二个为y点         */
        ArrayList<Entry> pointValues = new ArrayList<>();

        int count = (int)(Math.random()*10)+1;
        LogUtil.i(TAG,String.format("count id %d",count));

        int max=0,min=0;
        String[] values = new String[count];

        for(int i=0;i<count;i++){
            int point = (int)(Math.random()*10)+1;
            pointValues.add(new Entry(i,point));
            if(max<point){
                max = point;
            }
            values[i] = "data-"+i;
            LogUtil.i(TAG,String.format("point is %d",point));
        }

        LogUtil.i(TAG,String.format("count is %d,max is %d,min is %d,values size %d",count,max,min,values.length));
        setLineChar(pointValues,values,count,max,0);
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==1){
                setDate();
            }
        }
    };

    private void setLineChar(ArrayList<Entry> pointValues, final String[] values,final int count, int min, int max) {
        //点构成的某条线
        LineDataSet lineDataSet;
        if(lineChart.getData() == null || lineChart.getData().getDataSetByIndex(0) == null){
            //设置数据1  参数1：数据源 参数2：图例名称
            lineDataSet = new LineDataSet(pointValues, "巡更数据");
            //设置该线的颜色
            lineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
            //设置该线的宽度
            lineDataSet.setLineWidth(1.5f);
            // 设置线模式
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);//设置折线模式
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置平滑曲线模式
            //设置范围背景填充（线一面部分是否填充颜色）
            lineDataSet.setDrawFilled(true);
            //设置填充的颜色 getSDKInt() >= 18使用setFillDrawable(drawable);
            lineDataSet.setFillColor(getResources().getColor(R.color.colorPrimary_light));

            //设置每个点的颜色
            lineDataSet.setCircleColor(getResources().getColor(R.color.colorPrimary));
            //设置空心每个坐标点中心颜色
            lineDataSet.setCircleColorHole(getResources().getColor(R.color.colorPrimary_light));
            //设置每个坐标点的圆大小
            lineDataSet.setCircleRadius(3f);
            //设置是否画圆
            lineDataSet.setDrawCircles(true);

            lineDataSet.setHighlightEnabled(true);//是否禁用点击高亮线
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            lineDataSet.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
            lineDataSet.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色

            //设置是否显示点的坐标值
            lineDataSet.setDrawValues(true);
            lineDataSet.setValueTextSize(9f);//设置显示值的文字大小
            //格式化显示数据
            final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            lineDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });

            //线的集合
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(lineDataSet);
            //把要画的所有线(线的集合)添加到LineData里
            LineData lineData = new LineData(dataSets);
            //把最终的数据添加到图表中（可单条或多条线）
            lineChart.setData(lineData);




        }else{
            Log.i(TAG,"update line");
            lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(pointValues);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        }

        //setting X-轴参数
        XAxis xAxis = lineChart.getXAxis();
        //设置x轴标签数（注意数据会水平分割，所以此数据与一定要精确）
        xAxis.setLabelCount(count, true);
        //自定义x轴标签数据
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int key = (int)value;
                LogUtil.d(TAG,String.format("x:%d,value:%f",key,value));
                if(key<0){
//                    return values[0];
                    return "";
                }else if(key>=values.length){
//                    return values[count-1];
                    return "";//返回null会报java.lang.NullPointerException
                }else{
                    return values[key];
                }
            }
        });

        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.setMaxWidth(max);
        axisLeft.setMinWidth(min);




        //绘制图表(notifyDataSetChanged时默认不会刷新)
        lineChart.invalidate();
    }

    void initPie(PieChart pieChart){

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimerTask.cancel();
        mTimerTask = null;
        mTimer.cancel();
        mTimer = null;

        //Android加密算法中需要随机数时要使用SecureRandom来获取随机数
        SecureRandom sr = new SecureRandom();
        


        byte[] output = new byte[16];
        sr.nextBytes(output);
    }
}
