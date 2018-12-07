package com.guoxd.workframe.fragments.system;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
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
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by guoxd on 2018/5/8.
 */

public class TextWidgeFragment extends BaseFragment {

    final String TAG="system.TestWidgeFragment";
    @Override
    public void onRefresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.system_fragment_widge, container, false);

        //edittext筛选
        EditText edit = root.findViewById(R.id.edit_2);
       edit.addTextChangedListener(new SearchWather(edit));

        //Android加密算法中需要随机数时要使用SecureRandom来获取随机数
//        SecureRandom sr = new SecureRandom();
//        byte[] output = new byte[16];
//        sr.nextBytes(output);
        return root;
    }
    class SearchWather implements TextWatcher {
        //监听改变的文本框
        private EditText editText;
        /** 构造函数 */
        public SearchWather(EditText editText){
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String editable = editText.getText().toString();
            if(! TextUtils.isEmpty(editable)) {
                String str = isFirst(editable.toString());
                LogUtil.d("SearchWather", "afterTextChanged:" + str);
                if (!str.equals(editable)) {//前后不一致更新
                    editText.setText(str);
                    if (!TextUtils.isEmpty(str)) {
                        editText.setSelection(str.length());
                    }
                    LogUtil.d("SearchWather", "update:" + str);
                }
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        }
        public String isFirst(String str){//筛选A-Z开头
            char[] array = str.toCharArray();
            if(array.length == 1){
                if(!(Integer.valueOf(array[0]) >=65 && Integer.valueOf(array[0]) <=90)){//
                    str="";
                }
            }else if(array.length>1){
                int i=0,size=array.length;
                for(;i<size;i++){//查找第一个A-Z开头的
                    if(Integer.valueOf(array[i]) >=65 && Integer.valueOf(array[i]) <=90){
                        break;
                    }
                }
                if(i<size){
                    str=str.substring(i,size);
                }else{
                    str="";//
                }
            }
            return str;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}