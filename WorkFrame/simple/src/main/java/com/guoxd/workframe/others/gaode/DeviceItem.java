package com.guoxd.workframe.others.gaode;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

/**
 * 设备信息
 */
public class DeviceItem implements ClusterItem {
    String title;
    String detail;
    int type=0;
    private LatLng mLatLng;
    boolean isOnline;
    boolean isAlarm;
    Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getType() {
        return type;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public DeviceItem(String title, String detail, int type, LatLng mLatLng, boolean isOnline, boolean isAlarm) {
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.mLatLng = mLatLng;
        this.isOnline = isOnline;
        this.isAlarm = isAlarm;
    }
}
