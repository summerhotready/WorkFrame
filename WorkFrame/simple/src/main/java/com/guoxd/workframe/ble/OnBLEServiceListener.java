package com.guoxd.workframe.ble;
//ble蓝牙服务监听
public interface OnBLEServiceListener {
    //状态码
    int INIT_SUCCESS = 1;//初始化成功
    int INIT_FAILURE_NO_BLE = 2;//初始化失败 无ble
    int INIT_FAILURE_BLE_CLOSE = 10;//初始化失败 ble没开

    int SCAN_SUCCESS = 3;//扫描到指定设备
    int SCAN_FAILURE_NO_DEVICE = 9;//扫描 没扫到设备
    int SCAN_FAILURE_SCANNING = 4;//扫描 正在扫描
    int CONN_SUCCESS = 5;//连接到指定设备
    int CONN_DISCONNECTED = 11;//连接到指定设备
    int CONN_FAILURE = 6;//连接设备失败
    int SEND_RECEIVED = 12;//发送指令成功
    int SEND_ACK_FAILURE = 13;//发送指令失败
    int SEND_SUCCESS = 7;//发送指令成功
    int SEND_FAILURE = 8;//发送指令失败

    void bleState(int state, Object obj);//通过状态码判断读取
    void bleState(int state);//通过状态码判断读取
}
