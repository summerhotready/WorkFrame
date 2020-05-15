package com.guoxd.workframe.ble;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.text.TextUtils;


import androidx.annotation.NonNull;

import com.guoxd.workframe.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanRecord;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**蓝牙服务Service(仅开锁版本)
 * 运行于主线程
 * 绑定前需要取得定位权限
 * Author: emma Guo
 */
@TargetApi(5)
public class BLEService extends Service {
    private final String TAG="BLEService";
    private OnBLEServiceListener mListener;
    private Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        initHandler();
    }

    public void setBLEListener(OnBLEServiceListener mListener) {
        this.mListener = mListener;
        initAdapter();
    }

    private void initHandler() {
        bleState = new boolean[]{false,false,false,false,false};
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case OnBLEServiceListener.SEND_RECEIVED:
//                        LogUtil.i(TAG,"at thread %d "+Thread.currentThread());
                        String replace = (String)msg.getData().get("data");
                        mListener.bleState(OnBLEServiceListener.SEND_RECEIVED,replace);
                        break;
                    case OnBLEServiceListener.SEND_FAILURE:
                        break;
                }
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new BLEBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class BLEBinder extends Binder {
        public BLEService getService(){
            return BLEService.this;
        }
    }

    /*****/
    private final static UUID UART_SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    final int scanTime = 60*000;//扫描时间实时  60*1000 走集合
    //状态
    boolean[] bleState;//{init,scanState,connectState,actState,keyState}
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScannerCompat mScanner;
    List<BluetoothDevice> mScanDeviceList;
    ScanCallback mScanCallback;
    private String mCurrent;//要扫描连接的imei

    public void initAdapter(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mListener.bleState(OnBLEServiceListener.INIT_FAILURE_NO_BLE);
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isEnabled()) {
                mListener.bleState(OnBLEServiceListener.INIT_SUCCESS);
//                LogUtil.i(TAG,"start scan device");
                // 蓝牙打开 开始扫描蓝牙
                if(!bleState[0]) {
                    bleState[0] = true;
                }else{
                    scanBLEDevice();
                }
            } else {
                mListener.bleState(OnBLEServiceListener.INIT_FAILURE_BLE_CLOSE);
            }
        }
    }

    public void scanBLE(String imei){
        if(!TextUtils.isEmpty(imei)){
            mCurrent = imei;
            initAdapter();
        }
    }
    private void scanBLEDevice() {
        if(mScanner ==null) {//初始化
            mScanner = BluetoothLeScannerCompat.getScanner();
        }
        if(mScanCallback ==null){
            mScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
//                    LogUtil.e(TAG, "------onScanResult------->" + result.getDevice().getAddress());
                    scanDeviceResult(result);
                }
                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);
//                    LogUtil.e(TAG, "-------------Scan------->"+results.size());
                    if (results != null && results.size()>0) {
                        scanDeviceRestltList(results);
                    }else{
                        mListener.bleState(OnBLEServiceListener.SCAN_FAILURE_NO_DEVICE);
                    }
                }
                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                }
            };
        }
        if(!bleState[1]) {//可以扫描
            bleState[1] = true;
            ArrayList<ScanFilter> filters = new ArrayList<>();
            ScanFilter scanFilter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(UART_SERVICE_UUID))
                    .build();
            filters.add(scanFilter);
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setReportDelay(scanTime)
                    .setUseHardwareBatchingIfSupported(true)//这里设置为true解决了部分手机搜索不到的问题
                    .build();
            mScanDeviceList = new ArrayList<>();
            //启动搜索
            mScanner.startScan(filters, settings, mScanCallback);
        }else{
//            LogUtil.i(TAG,"正在扫描");
            mListener.bleState(OnBLEServiceListener.SCAN_FAILURE_SCANNING);
        }
    }


    /**处理蓝牙扫描结果
     * @param result
     */
    public void scanDeviceResult(ScanResult result){
        //解析判断
        ScanRecord scanRecord = result.getScanRecord();
        String deviceName = result.getScanRecord() != null ? result.getScanRecord().getDeviceName() : null;
        String code="";
        if(scanRecord !=null && scanRecord.getManufacturerSpecificData()!=null && scanRecord.getManufacturerSpecificData().size()>0 ){
            byte[] bytes = scanRecord.getManufacturerSpecificData().valueAt(0);
            code=new String(bytes);
        }else{
            return;
        }
        //判断是否为imei
        if(!TextUtils.isEmpty(code) && code.equals(mCurrent)){//是指定的imei
            mScanDeviceList .clear();
            mScanDeviceList.add(result.getDevice());
            //开始连接并且停止扫描
            stopBleScan();
            mListener.bleState(OnBLEServiceListener.SCAN_SUCCESS);
        }else{
        }
    }
    public void scanDeviceRestltList(final List<ScanResult> results) {
        HashMap<String,BluetoothDevice> cacheDeviceMap = new HashMap<>();
        for (ScanResult result : results) {
            if(cacheDeviceMap.get(result.getDevice().getAddress()) == null){//没存
                //解析判断
                ScanRecord scanRecord = result.getScanRecord();
                String deviceName = result.getScanRecord() != null ? result.getScanRecord().getDeviceName() : null;
                String code="";
                if(scanRecord !=null && scanRecord.getManufacturerSpecificData()!=null && scanRecord.getManufacturerSpecificData().size()>0 ){
                    byte[] bytes = scanRecord.getManufacturerSpecificData().valueAt(0);
                    code=new String(bytes);
                }else{
                    return;
                }
                //判断是否为imei
                if(!TextUtils.isEmpty(code) && code.equals(mCurrent)){//是指定的imei
                    mScanDeviceList .clear();
                    mScanDeviceList.add(result.getDevice());
                    cacheDeviceMap.put(result.getDevice().getAddress(),result.getDevice());
                    //开始连接并且停止扫描
                    stopBleScan();
                    mListener.bleState(OnBLEServiceListener.SCAN_SUCCESS);
                }else{
                }
            }else {//存过
            }
        }
        //扫描结束
//        LogUtil.i(TAG,String.format("-------scan finish,find device:%d",mScanDeviceList.size()));
        if(mScanDeviceList == null || mScanDeviceList.size()<=0){
            mListener.bleState(OnBLEServiceListener.SCAN_SUCCESS);
        }
    }
    //停止扫描
    public void stopBleScan() {
        bleState[1] = false;//可以开启下一次扫描
        if(mScanner !=null) {
            mScanner.stopScan(mScanCallback);
        }
    }
    //**********连接************
    public void connBLE(String imei){
        if(mUartManager ==null) {
            initUartManager();
        }
        if(!bleState[2] && mScanDeviceList!=null && mScanDeviceList.size()>0) {
            mUartManager.connect(mScanDeviceList.get(0));
        }
    }
    private UARTManager mUartManager;
    private void initUartManager(){
        mUartManager = new UARTManager(this);
        mUartManager.setGattCallbacks(new UARTManagerCallbacks() {
            @Override
            public void onDataReceived(BluetoothDevice device, String data) {
                String replace = data.replace("\n", "").replace("$", "");
                Message message = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("data",replace);
                message.setData(b);
                message.what = OnBLEServiceListener.SEND_RECEIVED;
                mHandler.sendMessage(message);
            }

            @Override
            public void onDeviceConnecting(BluetoothDevice device) {//"正在连接..."
                LogUtil.i(TAG, "------connect onDeviceConnecting");
            }

            @Override
            public void onDeviceConnected(BluetoothDevice device) {//"已连接"
                LogUtil.i(TAG, "------connect onDeviceConnected");
            }

            @Override
            public void onLinkLossOccurred(@NonNull BluetoothDevice device) {

            }

            @Override
            public void onBondingFailed(@NonNull BluetoothDevice device) {

            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device) {//连接断开
                super.onDeviceDisconnected(device);
                LogUtil.i(TAG, "------connect onDeviceDisconnected");
                mListener.bleState(OnBLEServiceListener.CONN_DISCONNECTED);
            }

            @Override
            public void onDeviceReady(BluetoothDevice device) {//"准备开锁中..."
                LogUtil.i(TAG, "------connect onDeviceReady start Timer");
                mListener.bleState(OnBLEServiceListener.CONN_SUCCESS);
            }

            @Override
            public void onError(BluetoothDevice device, String message, int errorCode) {
                super.onError(device, message, errorCode);
                LogUtil.i(TAG, "------connect onError");
                mListener.bleState(OnBLEServiceListener.CONN_FAILURE);
            }
        });
    }
    public void sendMessage(String msg){
        mUartManager.send(msg);
    }
    //关闭
    public void closeConnectBle() {
        if (mUartManager != null) {
            boolean connected = mUartManager.isConnected();
            if (connected) {
                mUartManager.disconnect();
                mUartManager.close();
            } else {
                mUartManager.close();
            }
        }
    }
}
