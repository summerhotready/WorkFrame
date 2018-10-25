package com.guoxd.workframe.utils

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.os.Handler


/**
 * Created by guoxd on 2018/10/23.
 */

class BleUtils(var mContext: AppCompatActivity){
    internal val TAG="BleUtils"
    //获得BluetoothAdapter
    var mBluetoothAdapter: BluetoothAdapter? = null
    //搜索到设备列表
    var mSearchBluetoothList:ArrayList<BluetoothDevice> ?=null
    //搜索倒计时
    var searchTimer: CountDownTimer? = null
    //监听
    var mListener:BleUtilListener?=null
    //
    var listener: OnBlueWriteReadListener? = null

    //缓存连接设备的Characteristic
    var characteristics: MutableList<BluetoothGattCharacteristic> = java.util.ArrayList<BluetoothGattCharacteristic>()
    var characteristicsNotify: MutableList<BluetoothGattCharacteristic> = java.util.ArrayList<BluetoothGattCharacteristic>()

    //常量
    private var mScanning: Boolean = false//搜索状态的标示：true为搜索中
    private var SCAN_PERIOD = 60 * 1000//搜索时间,倒计时时间
    private val REQUEST_ENABLE_BT = 1//请求启用蓝牙请求码
    var isInit = false;//确保服务只被初始化一次
    var hasData = false;//是否有ble设备
    val MSG_UPDATE_ADDAPTER = 104//update adapter
    val MSG_FINISH_REQUEST = 110//完成请求

    init{
        mSearchBluetoothList = ArrayList()
        //初始化蓝牙服务
        initBluetoothAdapter()
        //初始化倒计时服务
        initSearch()
    }

    //检查蓝牙
    fun checkBluetooth():Boolean{
        //判断mBluetoothAdapter是否被初始化
        if (mBluetoothAdapter == null || !(mBluetoothAdapter?.isEnabled?:false)) {//未初始化
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            mContext.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            return false
        }else{//已初始化
            if(isInit == false){
//                initServer()
                initSearch()
                isInit = true;
            }
            return true
        }
    }
    /**
     * 初始化蓝牙Adapter
     */
    fun initBluetoothAdapter():Boolean{
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            LogUtil.d(TAG, "您的设备不支持蓝牙BLE" )
            return false
        }
        //拿到BluetoothAdapter
        if (mBluetoothAdapter == null) {
            var blueManager: BluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if(blueManager == null){
                LogUtil.d(TAG, "初始化蓝牙服务失败" )
                return false
            }
            mBluetoothAdapter = blueManager.adapter;
        }
        //没有蓝牙ble功能的才会为null，关闭了是inEnable = false
        if(mBluetoothAdapter == null){
            LogUtil.d(TAG, "您的设备不支持蓝牙BLE" )
            return false
        }
        if(mBluetoothAdapter?.enable()?:false){
            mBluetoothAdapter?.enable()//直接开启蓝牙
        }
        isInit = true
        LogUtil.d(TAG, "初始化蓝牙成功" )
        return true
    }
    //搜索
    //初始化倒计时searchTimer
    fun initSearch(){
        searchTimer = object : CountDownTimer(SCAN_PERIOD.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                LogUtil.d(TAG,String.format("%d%s",(millisUntilFinished / 1000) , "秒后可重发"))
            }
            override fun onFinish() {
                LogUtil.d(TAG,"扫描结束")
                stopSearch()
                if(hasData){
                    hasData = false
                }
                mListener?.hasData(hasData)
            }
        }
    }

    /**
     * 启动扫描
     */
    fun startSearch(){
        if(!checkBluetooth()){
            initBluetoothAdapter()//初始化蓝牙
        }
        if (mScanning) {//已启动
            LogUtil.d(TAG, "正在搜索蓝牙设备");
        }else{//需要启动
            //清空缓存
            mSearchBluetoothList?.clear();
            searchTimer?.start();//开启倒计时
            //扫描之前要先取消当前的设备发现过程
            mBluetoothAdapter?.cancelDiscovery();
            handle.postDelayed(Runnable {
                if(mScanning){//判断是否结束
                    LogUtil.d(TAG, "scanLeDevice达成结束搜索条件");
                    stopSearch()
                }
            }, SCAN_PERIOD.toLong())
            mScanning = true;
            mBluetoothAdapter?.bluetoothLeScanner?.startScan(mScanCallBack)
            LogUtil.d(TAG, "startSearch start");
        }
    }

    /**
     * 结束扫描
     */
    fun stopSearch(){
        searchTimer?.cancel();//结束倒计时
        mScanning = false;
        mBluetoothAdapter?.bluetoothLeScanner?.stopScan(mScanCallBack)
        LogUtil.d(TAG, "scanLeDevice结束");
    }
    /**
     * 扫描蓝牙用回执
     **/
    var mScanCallBack: ScanCallback = object: ScanCallback(){
        //使用这个
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            LogUtil.d(TAG,"mLeScanCallback :"+callbackType+" "+result.device.address);
            //可以判断是否添加
            if (!(mSearchBluetoothList?.contains(result.device)?:false)) {
                if(!hasData){
                    hasData = true;
                    mListener?.hasData(hasData)
                }
                mSearchBluetoothList?.add(result.device);//08:D0:CC:38:65:E9
                handle.sendEmptyMessage(MSG_UPDATE_ADDAPTER);
            }else{
                LogUtil.d(TAG,"mLeScanCallback :"+result.device?.address+" chec");
            }
        }
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
        //有时候返回不成功，不推荐
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }
    }


    //无连接
    private val STATE_DISCONNECTED = 0
    //连接中
    private val STATE_CONNECTING = 1
    //已连接
    private val STATE_CONNECTED = 2
    //当前连接状态
    var mConnectionState = STATE_DISCONNECTED
    //当前连接的蓝牙地址
    var mBluetoothDeviceAddress:String =""

    var mBluetoothGatt: BluetoothGatt? = null

    //连接
    fun connect(address: String): Boolean {
        LogUtil.d(TAG, "将要连接设备 " + address)
        if(!checkBluetooth()){
            initBluetoothAdapter()
            return false
        }else{
            //重连设备
            if (mBluetoothDeviceAddress != null
                    && address == mBluetoothDeviceAddress
                    && mBluetoothGatt != null) {
                LogUtil.d(TAG, "尝试使用现在的 mBluetoothGatt连接")
                if (mBluetoothGatt?.connect() ?:false) {//手机是否已连接此设备
                    mConnectionState = STATE_CONNECTING
                    LogUtil.d(TAG, "连接重试 已连接")
                    return true
                } else {//未连接该设备
                    LogUtil.d(TAG, "连接重试 未连接")
                    return false
                }
            }
            //开始连接
            val device = mBluetoothAdapter?.getRemoteDevice(address)//获取设备
            if (device == null) {
                LogUtil.e(TAG, "设备没找到，不能连接")
                return false
            }
            ////这个方法需要三个参数：一个Context对象，自动连接（boolean值,表示只要BLE设备可用是否自动连接到它），和BluetoothGattCallback调用。
            mBluetoothGatt = device.connectGatt(mContext, true, mGattCallBack)
            LogUtil.d(TAG, "Trying to create a new connection.")
            return true
        }
    }

    val ACTION_GATT_CONNECTED = "com.charon.www.NewBluetooth.ACTION_GATT_CONNECTED"
    val ACTION_GATT_DISCONNECTED = "com.charon.www.NewBluetooth.ACTION_GATT_DISCONNECTED"
    val ACTION_GATT_SERVICES_DISCOVERED = "com.charon.www.NewBluetooth.ACTION_GATT_SERVICES_DISCOVERED"
    val ACTION_DATA_AVAILABLE = "com.charon.www.NewBluetooth.ACTION_DATA_AVAILABLE"
    val EXTRA_DATA = "com.charon.www.NewBluetooth.EXTRA_DATA"
    val READ_RSSI = "com.charon.www.NewBluetooth.READ_RSSI"


    private val mGattCallBack = object : BluetoothGattCallback() {
        override
        fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {//当连接状态发生改变
            val intentAction: String
            if (newState == BluetoothProfile.STATE_CONNECTED) {//连接状态发生改变
                intentAction = ACTION_GATT_CONNECTED
                mConnectionState = STATE_CONNECTED
//                broadcastUpdate(intentAction)
                LogUtil.d(TAG, "连接GATT server")
                // Attempts to discover services after successful connection.
                //连接之后，搜索设备支持的service
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//断开连接
                intentAction = ACTION_GATT_DISCONNECTED
                mConnectionState = STATE_DISCONNECTED
                LogUtil.d(TAG, "mGattCallBack status:$status Disconnected from GATT server.")
//                broadcastUpdate(intentAction)
            }
        }

        override //发现新服务端
        fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                characteristics.clear()
                characteristicsNotify.clear()
                val supportedGattServices = gatt.services//三条
                LogUtil.d(TAG, "PROPERTY_READ :" + BluetoothGattCharacteristic.PROPERTY_READ + " PROPERTY_WRITE:" + BluetoothGattCharacteristic.PROPERTY_WRITE + " PROPERTY_NOTIFY:" + BluetoothGattCharacteristic.PROPERTY_NOTIFY)
                for (gattService in supportedGattServices) {
                    val gattCharacteristics = gattService.characteristics
                    LogUtil.d(TAG, "gattCharacteristics size:" + gattCharacteristics.size)
                    for (gattCharacteristic in gattCharacteristics) {
                        LogUtil.d(TAG, "gattCharacteristic的UUID为:" + gattCharacteristic.uuid + " decp:" + gattCharacteristic.descriptors.size)
                        /*int charaProp = gattCharacteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            Log.d(TAG, "gattCharacteristic的属性为:  可读 "+(charaProp | BluetoothGattCharacteristic.PROPERTY_READ));
                            readUuid.add(gattCharacteristic.getUuid());
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            Log.d(TAG, "gattCharacteristic的属性为:  可写 "+(charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE));
                            writeUuid.add(gattCharacteristic.getUuid());
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            Log.d(TAG, "gattCharacteristic的属性为:  具备通知属性 "+(charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY));
                            notifyUuid.add(gattCharacteristic.getUuid());
                        }*/
                        /* boolean b = true;
                        if ((BluetoothGattCharacteristic.PROPERTY_READ | gattCharacteristic.getProperties()) <= 0) {
// READ set one
                            b=false;
                        }else{
                            Log.d(TAG, "gattCharacteristic的属性为:  可读 "+(BluetoothGattCharacteristic.PROPERTY_READ | gattCharacteristic.getProperties()));
                        }
                        if ((BluetoothGattCharacteristic.PROPERTY_WRITE & gattCharacteristic.getProperties()) <= 0) {//筛掉了部分uuid
// write set one
                            b=false;
                        }else{
                            Log.d(TAG, "gattCharacteristic的属性为:  可写 "+(BluetoothGattCharacteristic.PROPERTY_WRITE & gattCharacteristic.getProperties()));
                        }*/
                        //(characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) == 0
                        //                        Log.e(TAG,"gattCharacteristic的属性1 "+(gattCharacteristic.getProperties() &
                        //                                BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) );
                        ////                        Log.e(TAG,"gattCharacteristic的属性2 "+((gattCharacteristic.getProperties() &
                        ////                                (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) == 0? true:false) );
                        //                        int pro = gattCharacteristic.getProperties();
                        //                        Log.e(TAG, "uuid:" + gattCharacteristic.getUuid() + " Properties:"+pro);
                        if (gattCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE > 0) {
                            characteristics.add(gattCharacteristic)
                        }
                        if (gattCharacteristic.descriptors.size > 0) {
                            characteristicsNotify.add(gattCharacteristic)
                        }
                    }
                }
                //发现服务
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
            } else {
                LogUtil.d(TAG, "onServicesDiscovered received: " + status)
            }
        }

        // 读写特性
        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                listener?.onRead(String(characteristic.getValue()))
                LogUtil.d(TAG, Thread.currentThread().toString() + " onCharacteristicRead:" + if (listener == null) true else false)
            } else {
                listener?.onRead("ERROR")
            }
            LogUtil.d(TAG, Thread.currentThread().toString() + " onCharacteristicRead status:" + status + " UUID:" + characteristic.getUuid() + " characteristic:" + String(characteristic.getValue()))
        }


        //如果对一个特性启用通知,当远程蓝牙设备特性发送变化，回调函数onCharacteristicChanged( ))被触发。
        //当启用setCharacteristicNotification时会走这个
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            LogUtil.d(TAG, "onCharacteristicChanged characteristic UUID:" + characteristic.getUuid() + " characteristic:" + String(characteristic.getValue()))
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                listener?.onWrite(String(characteristic.getValue()))//BluetoothGatt: Unhandled exception in callback android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
            } else {
                listener?.onWrite("ERROR")
            }
            LogUtil.d(TAG, Thread.currentThread().toString() + " onCharacteristicWrite status:" + status + " UUID:" + characteristic.getUuid() + " characteristic:" + String(characteristic.getValue()))
        }


        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            LogUtil.d(TAG, "onReadRemoteRssi:" + rssi)
            //ControlActivity.rssi = rssi;
            broadcastUpdate(READ_RSSI)
        }


    }


    //发送广播通讯
    internal fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        mContext.sendBroadcast(intent)
    }





    //传输数据
    //蓝牙服务读写情况
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, notify: BluetoothGattCharacteristic) {
        try {
            if (mBluetoothAdapter == null || mBluetoothGatt == null) {
                LogUtil.d(TAG, "BluetoothAdapter not initialized")
                return
            } else {
                mBluetoothGatt?.writeCharacteristic(characteristic)

                val isEnableNotification = mBluetoothGatt?.setCharacteristicNotification(notify, true)
                LogUtil.d(TAG, "Bluetooth setCharacteristicNotification：" + isEnableNotification)
                if (isEnableNotification?:false) {
                    val descriptorList = notify.descriptors
                    if (descriptorList != null && descriptorList.size > 0) {
                        for (descriptor in descriptorList) {
                            //00002902-0000-1000-8000-00805f9b34fb
                            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            mBluetoothGatt?.writeDescriptor(descriptor)
                            LogUtil.d(TAG, "Bluetooth descriptor：" + descriptor.uuid)
                        }
                    }else{
                        LogUtil.d(TAG, "Bluetooth getDescriptors is null")
                    }

                    /* else{
                        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(UUID_CCC,0);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        characteristic.getDescriptors().add(descriptor);
                        mBluetoothGatt.writeDescriptor(descriptor);
                    }*/
                }


                /*List<BluetoothGattDescriptor> mDescriptors = characteristic.getDescriptors();
                if(mDescriptors.size() == 0){
                    BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(UUID_CCC,0);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor);
                }else {

                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                            UUID_CCC
//                        characteristic.getUuid()
//                        UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)
                    );
                    if (descriptor != null) {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBluetoothGatt.writeDescriptor(descriptor);
                    }
                }*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e(TAG, "error:" + e.message)
        }


    }


    fun readCharacteristic() {
        LogUtil.d(TAG, "readCharacteristic has characteristics" + characteristics.size)


        for (characteristic in characteristics) {
            if (characteristic.uuid.toString() == "6e400002-b5a3-f393-e0a9-e50e24dcca9e") {//6e400002-b5a3-f393-e0a9-e50e24dcca9e
                readCharacteristic(characteristic)
                break
            }

        }
    }

    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            LogUtil.d(TAG, "BluetoothAdapter not initialized")
            return
        } else
            mBluetoothGatt?.readCharacteristic(characteristic)
    }

    //断开连接
    fun disconnect() {
        mBluetoothGatt?.discoverServices()
    }

    /*var mServiceConnection: ServiceConnection =object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBluetoothLeService = (service as BluetoothService.LocalBinder).service;
            mBluetoothLeService?.setListener(object: OnBlueWriteReadListener {
                override fun onRead(str: String) {
                    Log.e(TAG,Thread.currentThread().toString()+" is read:"+str)
                    var msg: Message = Message.obtain()
                    msg.what = msg1;
                    msg.data.putString("data",str)
                    handle.sendMessage(msg)
                }
                override fun onWrite(str: String) {
                    Log.i(TAG,Thread.currentThread().toString()+" is write:"+str)
                    var msg: Message = Message.obtain()
                    msg.what = msg1;
                    msg.data.putString("data",str)
                    handle.sendMessage(msg)
                }});
            if(!(mBluetoothLeService?.initialize()?:false)){
                Log.e(TAG, "Unable to initialize Bluetooth");
                mContext.finish();
            }else{
                Log.d(TAG, "能初始化");
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected");
            mBluetoothLeService = null;
        }
    }*/

    var handle: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_UPDATE_ADDAPTER->{//更新adapter

                }
            }
        }
    }


    interface BleUtilListener{
        fun hasData(hasData:Boolean)//是否有数据
    }

    interface OnBlueWriteReadListener {
        fun onWrite(msg: String)
        fun onRead(msg: String)
    }
}
