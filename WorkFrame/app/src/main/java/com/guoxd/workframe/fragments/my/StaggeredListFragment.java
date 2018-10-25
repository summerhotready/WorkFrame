package com.guoxd.workframe.fragments.my;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoxd.work_frame_library.views.SwipeListLayout;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.modles.DeviceModle;
import com.guoxd.workframe.modles.ImageModle;
import com.guoxd.workframe.views.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**recyclerView瀑布流
 * item是普通item，通过对imageview的高度改写来解决问题
 * AUTHOR: guoxd
 * DATE: 2018/4/24
 */
public class StaggeredListFragment extends BaseFragment {

    SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private List<ImageModle> mData;
    DeviceAdapter adapter;

    @Override
    public void onRefresh() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mData = new ArrayList<>();
    }
    StaggeredGridLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_base_recycler, container, false);
        refreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        //设置瀑布流
        manager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //GAP_HANDLING_NONE 什么都不做，但实际上也会调整位置
        //GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS  通过item之间互换位置重新调整布局
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE  );

        mRecyclerView.setLayoutManager(manager);

        //分割线
        mRecyclerView.addItemDecoration(new MyItemDecoration(getActivity(), MyItemDecoration.VERTICAL));

        adapter = new DeviceAdapter();
        mRecyclerView.setAdapter(adapter);
        initListener();

        getData();
        return root;
    }
int lastVisibleItem;

    private void initListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    manager.invalidateSpanAssignments();
//                }
                Log.i("scroll","lastVisibleItem:"+lastVisibleItem);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==adapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                        }
                    },1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] into = new int[1];
//                lastVisibleItem =manager.findLastVisibleItemPositions(into).length;
                Log.i("scroll","lastVisibleItem:"+lastVisibleItem);
            }
        });
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
                Log.i("text","onRefresh");
                getData();
            }
        });

    }

int current=0;
    private void getData() {
        int size = current+5;
        for(int i=current;i<size;i++){
            mData.add(new ImageModle(10,100,"0"+i+"1"));
            mData.add(new ImageModle(10,200,"0"+i+"2"));
            mData.add(new ImageModle(10,300,"0"+i+"3"));
            Log.i("text","size:"+mData.size());
            adapter.notifyItemInserted(mData.size()-3);
        }
        current = size;
        refreshLayout.setRefreshing(false);
//        refreshLayout.
    }

    interface OnItemClickListener {
        void onClick(DeviceModle device);
        void onUpdate(DeviceModle device);
        void onDelete(DeviceModle device);
    }


    class DeviceHolder extends RecyclerView.ViewHolder {

        public DeviceHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.img);
        }

        ImageView image;
        TextView title;
        View layout;
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

            final ImageModle device = mData.get(position);
            holder.title.setText(device.getIds());
                ViewGroup.LayoutParams lp = holder.image.getLayoutParams();
                lp.height = device.getHeight();
                holder.image.setLayoutParams(lp);
                Log.i("do",position+" "+device.getHeight());

        }


        @Override
        public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_stagger, parent, false);
            DeviceHolder holder = new DeviceHolder(view);
            return holder;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
