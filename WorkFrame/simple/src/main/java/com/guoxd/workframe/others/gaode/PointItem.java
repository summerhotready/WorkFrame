package com.guoxd.workframe.others.gaode;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

public class PointItem {
    LatLng latLng;
    int count;
    boolean isAlarm = false;
    boolean isOnline =true;
    String pointName;
//    对应PointItem生成的Marker
    private Marker mMarker;

    public PointItem(LatLng latLng, int count, boolean isAlarm, String pointName) {
        this.latLng = latLng;
        this.count = count;
        this.isAlarm = isAlarm;
        this.pointName = pointName;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public LatLng getCenterLatLng() {
        return latLng;
    }

    public int getCount() {
        return count;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void setMarker(Marker mMarker) {
        this.mMarker = mMarker;
    }
}
