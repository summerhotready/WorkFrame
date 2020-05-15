package com.guoxd.workframe.system;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.system.dialog.ShowModleProgressDialog;

import java.util.List;

/**
 * Created by guoxd on 2018/5/8.
 */

public class RecyclerViewFragment extends BaseFragment {

    final String TAG="RecyclerViewFragment";
    ShowModleProgressDialog progressDialog;


    final int delayTime = 2*1000;

    @Override
    protected int getCurrentLayoutID() {
        return R.layout.system_fragment_recyclerview;
    }

    @Override
    protected void initView(View root) {
        progressDialog = new ShowModleProgressDialog();
        //
        root.findViewById(R.id.btn_shouquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(getActivity().getSupportFragmentManager(),"recyclerViewVertical");
                mHandler.sendEmptyMessageDelayed(101,delayTime);
            }
        });
        root.findViewById(R.id.btn_zhiling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(getActivity().getSupportFragmentManager(),"recyclerViewVertical");
                mHandler.sendEmptyMessageDelayed(101,delayTime);
            }
        });
        RecyclerView recyclerViewVertical = root.findViewById(R.id.recyclerView1);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(getActivity()));//default is LinearLayoutManager.VERTICAL
        recyclerViewVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       //横向RecyclerView
        RecyclerView recyclerViewHorizontal = root.findViewById(R.id.recyclerView2);
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 101:{
                    progressDialog.addProgress("101的头衔");
                    mHandler.sendEmptyMessageDelayed(102,delayTime);
                    break;
                }
                case 102:{
                    progressDialog.addProgress("102的头衔");
                    mHandler.sendEmptyMessageDelayed(103,delayTime);
                    break;
                }
                case 103:{
                    progressDialog.addProgress("103的头衔");
                    mHandler.sendEmptyMessageDelayed(104,delayTime);
                    break;
                }
                case 104:{
                    progressDialog.addProgress("104的头衔");
                    break;
                }
            }
        }
    };

//    recycler1
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
