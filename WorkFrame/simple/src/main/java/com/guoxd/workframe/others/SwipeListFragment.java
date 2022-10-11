package com.guoxd.workframe.others;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.modles.DeviceModle;
import com.guoxd.workframe.views.LineItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**recyclerView侧滑
 * item修改为使用第三方组件实现
 * AUTHOR: The_Android
 * DATE: 2018/4/24
 */
public class SwipeListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<DeviceModle> mData;
    DeviceAdapter adapter;

    @Override
    protected int getCurrentLayoutID() {
        return R.layout.layout_base_recycler;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mData = new ArrayList<>();
    }

    @Override
    protected void initView(View root) {
        setPageTitle("RecyclerView侧滑");
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //分割线
        LineItemDecoration lineItemDecoration = new LineItemDecoration(getActivity());
        lineItemDecoration.setLine((int)(5*getResources().getDisplayMetrics().density),R.color.color_purple_dark);
        mRecyclerView.addItemDecoration(lineItemDecoration);

        adapter = new DeviceAdapter();
        mRecyclerView.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        initListener();

        getData();
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
            layout = itemView.findViewById(R.id.swipe);
            title = (TextView) itemView.findViewById(R.id.title);
            value = (TextView) itemView.findViewById(R.id.value);
            edit = (TextView) itemView.findViewById(R.id.edit);
            delete = (TextView) itemView.findViewById(R.id.delete);
        }

        TextView title, value,edit,delete;
        View item;
        SwipeLayout layout;
    }

    class DeviceAdapter extends RecyclerSwipeAdapter<DeviceHolder> {
        OnItemClickListener listener;
        //单选开关
        boolean isSingle = true;
        int isOpen =-1;
        int witch=-1;


        public void setWitch(int witch) {
            this.witch = witch;
        }

        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }
        @Override
        public void onBindViewHolder(DeviceHolder holder, int position) {
            if(witch == position){
//                holder.layout.setStatus(SwipeListLayout.Status.Close, true);
            }
            //控制同时只显示一个
            mItemManger.bindView(holder.layout,position);//注意绑定目标为itemView
            final DeviceModle device = mData.get(position);
            holder.layout.setClickToClose(true);
            //PullOut效果为整条移动效果更好
            holder.layout. setShowMode(SwipeLayout.ShowMode.PullOut);//呼出方向
            //设置后只能多不能单
//           mItemManger.setMode(Attributes.Mode.Single);
//            holder.layout. addDrag(SwipeLayout.DragEdge.Right, holder.layout. findViewById(R.id.swipe));
            holder.layout. close();

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
                    closeItem(position);
//                    holder.layout.setStatus(SwipeListLayout.Status.Close, true);
                    listener.onUpdate(device);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeItem(position);
//                    holder.layout.setStatus(SwipeListLayout.Status.Close, true);
                    listener.onDelete(device);
                    mData.remove(position);
                    notifyItemRemoved(position);
                }
            });
           /* holder.layout.setOnSwipeStatusListener(new SwipeListLayout.OnSwipeStatusListener() {
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
            });*/
        }

  /*      public void closeItem(int postion){
            setWitch(postion);
            notifyItemChanged(postion);
        }*/

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


        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }
    }
}
