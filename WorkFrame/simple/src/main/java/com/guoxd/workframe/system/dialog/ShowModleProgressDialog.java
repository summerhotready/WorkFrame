package com.guoxd.workframe.system.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guoxd.workframe.R;
import com.guoxd.workframe.anim.recycleranim.HorizonInsertItemAnimator;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.views.LineItemDecoration;

import java.util.ArrayList;

public class ShowModleProgressDialog extends DialogFragment {
    String TAG = "ShowModleProgressDialog";
    ShowModleProgressDialogListener mListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG,"onCreate()");
        //设置全屏
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

    }

    public void setListener(ShowModleProgressDialogListener mListener) {
        this.mListener = mListener;
    }

    Dialog dialog;
    RecyclerView recyclerView;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.i(TAG,"onCreateDialog()");
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mData = new ArrayList<>();
        mAdater = new ShowAdapter();

        View view = inflater.inflate(R.layout.system_dialog_show_modle_progress, null);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(),LinearLayout.HORIZONTAL));
        recyclerView.setAdapter(mAdater);

        recyclerView.setItemAnimator(new HorizonInsertItemAnimator());
        recyclerView.getItemAnimator().setAddDuration(1000);
        //关闭
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener !=null){
                    mListener.onClose();
                }
                dialog.dismiss();
            }
        });
        //点击取消
        view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mData !=null && mData.size()>0){//已开始
                    String tag = mData.get(mData.size()-1).text;
                    if(mListener !=null){
                        mListener.onCancle(tag);
                    }
                }
            }
        });

        ViewGroup.LayoutParams lprc = recyclerView.getLayoutParams();
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        int height = getActivity().getResources().getDisplayMetrics().heightPixels;
        lprc.width = (int)(width*0.7);
        lprc.height = (int)(height*0.5);
        recyclerView.setLayoutParams(lprc);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        dialog = builder.create();
        //设置背景透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //设置view位置
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

//    列表数据
    ArrayList<ShowTextData> mData;
    ShowAdapter mAdater;
//    Data
    class ShowTextData{
        int id;//序号
        @ColorInt
        int color;//颜色文件
        String text;//文字
        boolean hasButton;
        String buttonText;

    public ShowTextData(int id, int color, String text, boolean hasButton, String buttonText) {
        this.id = id;
        this.color = color;
        this.text = text;
        this.hasButton = hasButton;
        this.buttonText = buttonText;
    }
}
//
    class ShowViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_icon;
        TextView tv_value;
        public ShowViewHolder(View itemView){
            super(itemView);
            iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
            tv_value = (TextView) itemView.findViewById(R.id.tv_itemValue);
        }
    }
//    Adapter
    class ShowAdapter extends RecyclerView.Adapter <ShowViewHolder>{
    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShowViewHolder(LayoutInflater.from(getActivity())
                .inflate(R.layout.system_dialog_show_modle_progress_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position) {
        ShowTextData data = mData.get(position);
        LogUtil.i(TAG,String.format("onBindViewHolder:%d,title:%s",position,data.text));
        holder.tv_value.setText(data.text);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
//添加数据 0,1  2
    public void addProgress(String text){
        int count = mData.size();
        ShowTextData data = new ShowTextData((count+1),0,text,false,"");
        mData.add(data);
        LogUtil.d(TAG,String.format("addProgress size:%d,count:%d",mData.size(),count));

        recyclerView.getAdapter().notifyItemChanged(count);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG,"onPause()");
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"onDestroy()");
    }

    interface ShowModleProgressDialogListener{
        void onClose();
        void onCancle(String tag);
    }
}
