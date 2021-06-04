package com.guoxd.workframe.others.gaode;

import com.amap.api.maps.model.LatLng;

/**
 * 聚合元素的item
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
     LatLng getPosition();
}
