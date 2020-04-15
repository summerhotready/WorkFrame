package com.guoxd.workframe.system;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.system.dialog.ShowModleProgressDialog;
import com.guoxd.workframe.utils.ToastUtils;

public class BLETestFragment extends BaseFragment {
    @Override
    public void onRefresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.system_fragment_ble, container, false);

        //
        root.findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startScan();
            }
        });
        root.findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RecyclerView recyclerViewVertical = root.findViewById(R.id.recyclerView);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(getActivity()));//default is LinearLayoutManager.VERTICAL

        return root;
    }

    BluetoothAdapter bluetoothAdapter;
    private void startScan() {
        if(bluetoothAdapter == null){
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if(bluetoothAdapter == null) {//设备不支持蓝牙
            ToastUtils.showMsgToast(getActivity(),"设备不支持蓝牙");
            return;
        }
        if(!bluetoothAdapter.isEnabled()) {//蓝牙未开启
            // 开启蓝牙
            bluetoothAdapter.enable();//使用enable操作蓝牙开关，自动开启
        }

    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
