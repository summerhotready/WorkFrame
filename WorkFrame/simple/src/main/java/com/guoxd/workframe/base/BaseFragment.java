package com.guoxd.workframe.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.guoxd.workframe.ShowActivity;


/**
 * Created by guoxd on 2018/4/27.
 */

public abstract class BaseFragment extends Fragment {

    public String TAG = getClass().getName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= initView(inflater,container);
        initView(rootView);
        initData();
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    //重定位rootView
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container){
        // View root = inflater.inflate(R.layout.other_fragment_mpchar, container, false);
        View rootView = inflater.inflate(getCurrentLayoutID(), container, false);
        return rootView;
    }

    protected   void  initView(View root){

    }

    protected  void initData(){};


    protected abstract int getCurrentLayoutID();

    @Override
    public void onResume() {
        super.onResume();
        afterViewShow();
    }

    @Override
    public void onPause() {
        super.onPause();
        afterViewHide();
    }

    public void afterViewShow(){}
    public void afterViewHide(){}

    public void setPageTitle( String str){
        if(getActivity() instanceof ShowActivity){
            ((ShowActivity)getActivity()).setPageTitle(str);
        }
    }
}
