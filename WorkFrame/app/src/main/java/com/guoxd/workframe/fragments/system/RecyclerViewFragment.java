package com.guoxd.workframe.fragments.system;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guoxd on 2018/5/8.
 */

public class RecyclerViewFragment extends BaseFragment {

    final String TAG="RecyclerViewFragment";
    @Override
    public void onRefresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.system_fragment_recyclerview, container, false);

        //
        RecyclerView recyclerViewVertical = root.findViewById(R.id.recyclerView1);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(getActivity()));//default is LinearLayoutManager.VERTICAL
       //横向RecyclerView
        RecyclerView recyclerViewHorizontal = root.findViewById(R.id.recyclerView2);
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        return root;
    }
    class Code{
        int type=0;
        String title;
        String subTitle;
        String dataTime;
        String checkType;//
    }

    class CodeHolder extends RecyclerView.ViewHolder{
        public CodeHolder(View itemView){
            super(itemView);
        }
    }
    class HorizontalAdapter extends RecyclerView.Adapter<CodeHolder>{
        List<Code> mData;
        public HorizontalAdapter(List<Code> datas){

        }
        @Override
        public int getItemCount() {
            return mData==null? 0:mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public CodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull CodeHolder holder, int position) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
