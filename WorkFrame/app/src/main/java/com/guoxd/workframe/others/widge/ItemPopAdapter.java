package com.guoxd.workframe.others.widge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guoxd.workframe.R;
import com.guoxd.workframe.modles.BasePopItem;

import java.util.ArrayList;

/**com.github.razerdp:BasePopup widge
 * Created by guoxd on 2019/1/2.
 */

public class ItemPopAdapter extends RecyclerView.Adapter {
    ArrayList<BasePopItem> mDatas;
    Context mContext;

    public ItemPopAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDatas == null? 0:mDatas.size();
    }
    public void setData(ArrayList<BasePopItem> datas){
        mDatas = datas;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BasePopItem device = mDatas.get(position);
        ((ItemHolder) holder).getTitle().setText(device.getValue());

        ((ItemHolder) holder).getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener !=null){
                    mListener.onItemClick(device.getKey(),device.getValue());
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.main_pop_adapter_item, parent, false));
    }



    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }

        public TextView getTitle() {
            return title;
        }
    }
    OnItemClickListener mListener;
    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
    public interface OnItemClickListener {
        void onItemClick(String key, String value);
    }
}
