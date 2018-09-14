package com.guoxd.workframe.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxd.work_frame_library.views.RecyclerRefreshLayout;
import com.guoxd.work_frame_library.views.SwipeListLayout;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.modles.DeviceModle;
import com.guoxd.workframe.views.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**recyclerView侧滑
 * item使用SwipeListLayout实现
 * AUTHOR: The_Android
 * DATE: 2018/4/24
 */
public class SwiptListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<DeviceModle> mData;
    DeviceAdapter adapter;

    @Override
    public void onRefresh() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mData = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_base_recycler, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //分割线
        mRecyclerView.addItemDecoration(new MyItemDecoration(getActivity(), MyItemDecoration.VERTICAL));

        adapter = new DeviceAdapter();
        mRecyclerView.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.refreshLayout);
        initListener();

        getData();
        return root;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 101:
                    refreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private void initListener() {

        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onClick(DeviceModle device) {

            }

            @Override
            public void onUpdate(DeviceModle device) {

            }

            @Override
            public void onDelete(final DeviceModle device) {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(101,2000);
            }
        });
    }


    private void getData() {
        for(int i=0;i<20;i++){
            mData.add(new DeviceModle("056","0151","1111","","","","","","","","","","","",0));
            mData.add(new DeviceModle("055","0152","1111","","","","","","","","","","","",0));
        }

        adapter.notifyDataSetChanged();
    }

    interface OnItemClickListener {
        void onClick(DeviceModle device);
        void onUpdate(DeviceModle device);
        void onDelete(DeviceModle device);
    }


    class DeviceHolder extends RecyclerView.ViewHolder {

        public DeviceHolder(View itemView) {
            super(itemView);
           item = itemView.findViewById(R.id.layout);
            layout = itemView.findViewById(R.id.swip_layout);
            title = (TextView) itemView.findViewById(R.id.title);
            value = (TextView) itemView.findViewById(R.id.value);
            edit = (TextView) itemView.findViewById(R.id.edit);
            delete = (TextView) itemView.findViewById(R.id.delete);
        }

        TextView title, value,edit,delete;
        View item;
        SwipeListLayout layout;
    }

    class DeviceAdapter extends RecyclerView.Adapter<DeviceHolder> {
        OnItemClickListener listener;
        int isOpen =-1;
        int witch=-1;


        public void setWitch(int witch) {
            this.witch = witch;
        }

        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(final DeviceHolder holder, final int position) {
            if(witch == position){
                holder.layout.setStatus(SwipeListLayout.Status.Close, true);
            }
            final DeviceModle device = mData.get(position);
            holder.title.setText(device.getBianHao());
            holder.value.setText(device.getIMEI());
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOpen !=-1){
                        closeItem(isOpen);
                        isOpen = -1;
                    }
                    listener.onClick(device);
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layout.setStatus(SwipeListLayout.Status.Close, true);
                    listener.onUpdate(device);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layout.setStatus(SwipeListLayout.Status.Close, true);
                    listener.onDelete(device);
                }
            });
            holder.layout.setOnSwipeStatusListener(new SwipeListLayout.OnSwipeStatusListener() {
                @Override
                public void onStatusChanged(SwipeListLayout.Status status) {
                    Log.i("Adapter","change:"+position+" status:"+status+" isopen:"+isOpen);
                    if(status == SwipeListLayout.Status.Open){
                        if(isOpen !=position){
                            //close
                            if(isOpen!=-1) {
                                closeItem(isOpen);
                            }
                            isOpen = position;
                        }
                    }
                    if(status == SwipeListLayout.Status.Close && isOpen == position){
                        isOpen = -1;
                        Log.e("Adapter","close:"+position);
                    }


                }

                @Override
                public void onStartCloseAnimation() {

                }

                @Override
                public void onStartOpenAnimation() {

                }
            });
        }
        void closeItem(int postion){
            setWitch(postion);
            notifyItemChanged(postion);
        }

        @Override
        public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_recyclerview_swiper_item, parent, false);
            DeviceHolder holder = new DeviceHolder(view);
            return holder;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
