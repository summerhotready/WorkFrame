package com.guoxd.workframe.others.gaode;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.AnimationSet;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.guoxd.work_frame_library.anmi.AnimViewSize;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ViewHelpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kotlin.math.MathKt;

/**第三方动画库（反射调用）
 * Created by guoxd on 2018/10/17.
 * 调查结论，使用icons时图片大小不能改变实际呈现的大小
 * 对marker点做点击和关闭动画是危险的，表现上是时不时的不执行（何用icon或者icons无关）
 */

public class GaodeMapFragment extends BaseFragment {
    boolean isMarkerInfo = true;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    View codeView;
    LinearLayout menuView;
    AppCompatTextView tv1,tv2,tv3;
    @Override
    protected int getCurrentLayoutID() {
        return R.layout.other_fragment_gaode;
    }

    @Override
    protected void initView(View root) {
        initAnim(root);
    }

    MapView mMapView;
    Bundle mSavedInstanceState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= initView(inflater,container);
        mMapView = rootView.findViewById(R.id.view_map);
        mMapView.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        isMarkerInfo = false;
        initView(rootView);
        return rootView;
    }
    AMap aMap;
    private void initAnim(View root) {
//初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        setMapAttribute();
        viewPagerShowHeight = (int)(80*getResources().getDisplayMetrics().density);
        menuStartHeight = (int)(50*getResources().getDisplayMetrics().density);
        menuEndHeight = (int)(150*getResources().getDisplayMetrics().density);
        viewPager = root.findViewById(R.id.view_pager);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                LogUtil.i(TAG,String.format("onPageScrolled %d,%f,%d",position,positionOffset,positionOffsetPixels));
            }
        });
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        codeView = root.findViewById(R.id.code);
        menuView = root.findViewById(R.id.bottomMenu);
        tv1 = root.findViewById(R.id.tv_1);
        tv2 = root.findViewById(R.id.tv_2);
        tv3 = root.findViewById(R.id.tv_3);
        hindViewPager();
        closeMenu();
        initLocation();
        root.findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aMap.setMyLocationStyle(myLocationStyle);
            }
        });

        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv2.getVisibility() == View.GONE){
                    openMenu();
                }else{
                    closeMenu();
                }
            }
        });
        getBaseActity().setPageTitle("高德地图");
    }
    MyLocationStyle myLocationStyle;
    private void initLocation(){
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
//        定位频次修改只会在定位蓝点的连续定位模式下生效，定位蓝点支持连续定位的模式是：
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        //方法自5.1.0版本后支持
        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource( R.mipmap.icon_saoma_gis));//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
//精度圈 MyLocationStyle
//        myLocationStyle.strokeColor(int color);//设置定位蓝点精度圆圈的边框颜色的方法。
//        myLocationStyle. radiusFillColor(int color);//设置定位蓝点精度圆圈的填充颜色的方法。
//        myLocationStyle. strokeWidth(float width);//设置定位蓝点精度圈的边框宽度的方法。
        //        style
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
//以下三种模式从5.1.0版本开始提供
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。

//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.getUiSettings().setZoomGesturesEnabled(true);;


        mClusterOverlay = new ClusterOverlay(aMap, null, 320, getActivity());

        mClusterOverlay.setOnClusterClickListener(new ClusterClickListener() {
            @Override
            public void onClick(Marker marker, List<ClusterItem> clusterItems) {

            }

            @Override
            public void onClusterClick(PointItem pointItem) {
                //开启查询聚合点
                LogUtil.i(TAG,"onClusterClick"+pointItem.pointName);
                getDeviceList();
            }

            @Override
            public void onPointClick() {
                //开启查询散列点

            }
        });

//定位一次后修改
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
    }
    Marker mCurrentMemMarker=null;
    double latitude,longitude;
    long duration = 1000L;
//  聚合点
    private ClusterOverlay mClusterOverlay;
//    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private int clusterRadius = 100;


    HashMap<Marker,MapData> mapHash = new HashMap<>();

    private void clearMarkers(){
        for(Marker marker:mapHash.keySet()){
            marker.remove();//从地图上删除marker
        }
//        LogUtil.i(TAG,String.format("marker:%d allMarker:%d",mapHash.size(),aMap.getMapScreenMarkers().size()));
        viewPagerAdapter.removeData();
        mapHash.clear();
    }

    private void showMark(){
//        aMap.clear();//会一并清除定位点
        clearMarkers();//使用该方式能保留定位点

//        MarkerOptions markerOption = new MarkerOptions();
//        markerOption.draggable(true);//设置Marker可拖动
//        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(getResources(),R.mipmap.icon_gis_on)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果


        //使用自定义shape失败=>显示默认点
        //使用png图片可以，但不同尺寸的图片导致marker大小不一致，大小一致时又会出现其他问题


        //存放所有点的经纬度
//        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        List<PointItem> items = new ArrayList<PointItem>();
        for(int i=0;i<10;i++) {
            LatLng latLng = new LatLng( latitude + Math.random()*10,  longitude +Math.random()*10,false);
//            MapData mapData = new MapData(String.format("TJ %d",i),String.format("天津市：%f, %s",latLng.latitude,latLng.longitude));
//            markerOption.position(latLng);
            //title必须，否则会显示不出来
//            markerOption.title(mapData.getTitleStr()).snippet(mapData.getDetailStr());
//            Marker marker = aMap.addMarker(markerOption);
//            marker.setClickable(true);
//            marker.setDraggable(true);
            //将所有marker经纬度include到boundsBuilder中
//            boundsBuilder.include(latLng);
//            mapHash.put(marker,mapData);

//            if(!isMarkerInfo){
//                viewPagerAdapter.addData(mapData);
//            }

            PointItem deviceItem = new PointItem(latLng,i,i%4==0,String.format("天津：%.1f, %.1f",latLng.latitude,latLng.longitude));
            deviceItem.setOnline(i%5==0);

            items.add(deviceItem);
        }

//        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        // 地图显示包含全部的点 40 表示padding=40，如果你想让你的marker布局全部显示出来就需要考虑到marker的高度来设置padding值
        //设置默认缩放级别
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
//        showLocationAnim();//第一次不执行，此时还没有生成该marker
//
      mClusterOverlay.addClusters(items);
        //更新地图状态
//        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 320));
    }

    void getDeviceList(){
        //存放所有点的经纬度
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        ArrayList<DeviceItem> deviceItems = new ArrayList<>();
        for(int i=1;i<11;i++) {
            LatLng latLng = new LatLng( latitude + Math.random()/i*10,  longitude +Math.random()*10,false);
            deviceItems.add(new DeviceItem("device" + i,"",1,latLng,i%5!=0,i%3==0));
            boundsBuilder.include(latLng);
        }

        //更新地图状态
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 320));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        mClusterOverlay.addDevice(deviceItems);
    }



    private void showLocationAnim(){//不建议随时设置，建议全局使用一个anim，然后执行
        LogUtil.i(TAG,String.format("marker:%d allMarker:%d",mapHash.size(),aMap.getMapScreenMarkers().size()));
        for(Marker marker:aMap.getMapScreenMarkers()){
            if(mapHash.get(marker) ==null){
                try {
                    Marker breatheMarker = marker;//定位,获取点位失败
                    // 中心的marker
                    // 动画执行完成后，默认会保持到最后一帧的状态
                    AnimationSet animationSet = new AnimationSet(true);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0f);
                    alphaAnimation.setDuration(2000);
                    // 设置不断重复
                    alphaAnimation.setRepeatCount(Animation.INFINITE);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1, 3.5f, 1, 3.5f);
                    scaleAnimation.setDuration(2000);
                    // 设置不断重复
                    scaleAnimation.setRepeatCount(Animation.INFINITE);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.addAnimation(scaleAnimation);
                    animationSet.setInterpolator(new LinearInterpolator());
                    breatheMarker.setAnimation(animationSet);
                    breatheMarker.startAnimation();
                    LogUtil.i(TAG,String.format("marker side finish"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void removeMarker(String name){
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            if(marker.getObject()!=null&&
                    marker.getObject().toString().equals(name)){
                marker.remove();//移除当前Marker
            }//需要注意，地图上默认的定位点也是一个marker，此时会造成getObject()的空指针异常
        }
        aMap.reloadMap();//刷新地图，替换原本的invalidate();
    }


    /**
     * 设置地图属性
     */
    private void setMapAttribute() {
        geocoderSearch = new GeocodeSearch(getActivity());
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {//返回结果成功或者失败的响应码。1000为成功，其他为失败
                //解析result获取地址描述信息
                LogUtil.i(TAG,String.format("setAMapGestureListener: onRegeocodeSearched %d code:%s Province:%s city:%s getTownship:%s build:%s"
                        ,i,regeocodeResult.getRegeocodeAddress().getAdCode(),regeocodeResult.getRegeocodeAddress().getProvince(),regeocodeResult.getRegeocodeAddress().getCity()
                        ,regeocodeResult.getRegeocodeAddress().getTownship()
                ,regeocodeResult.getRegeocodeAddress().getBuilding()));
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        //设置默认缩放级别
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        //隐藏的右下角缩放按钮
//        uiSettings.setZoomControlsEnabled(false);
        //设置marker点击事件监听
       /* aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                LogUtil.i(TAG,String.format("MarkerClick %s isSHow:%b",marker.getTitle(),marker.isInfoWindowShown()));
                if(isMarkerInfo) {
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                    } else {
                        marker.showInfoWindow();
                    }
                }else{//marker
                    if (mCurrentMemMarker != null) { // 判断之前放大的marker是否还在放大的状态
                        mCurrentMemMarker.startAnimation(); // 将之前放大的marker实现还原的状态，启动动画
//                        setNotClickedMarkerAnim(mCurrentMemMarker); // 给之前的marker设置下一次点击需要放大的动画效果
                    }
                    mCurrentMemMarker = marker; // 当前放大的marker赋值
                    marker.startAnimation(); // 放大marker的动画播放
                    marker.showInfoWindow(); // 显示当前marker的infowindow
//                    setClickedMarkerAnim(marker); // 设置放大marker的还原状态动画

                    if(viewPager.getVisibility() == View.GONE){
                        showViewPager();
//                        viewPager.setVisibility(View.VISIBLE);
                    }
                    viewPager.setCurrentItem(viewPagerAdapter.getPosition( mapHash.get(marker)));
                }
                return true; // 返回:true 表示点击marker 后marker 不会移动到地图中心；返回false 表示点击marker 后marker 会自动移动到地图中心
            }
        });*/


        //设置地图点击事件监听
/*        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//               //判断当前marker信息窗口是否显示
                LogUtil.i(TAG,String.format("onMapClick  isSHow:"));
                hindViewPager();
//                viewPager.setVisibility(View.GONE);
            }
        });*/


        //        获取经纬度信息：实现 AMap.OnMyLocationChangeListener 监听器
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
                LogUtil.i(TAG,String.format("onMyLocationChange location(%f,%f) city:%s",location.getLatitude(),location.getLongitude(),location.getProvider()));
                showMark();
//                showLocationAnim();//第一次不执行，此时还没有生成该marker
            }//网络连接异常 请到http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/查看错误码说明,错误详细信息:please check the network
        });

//拖拽marker
      /*  aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
            // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            @Override
            public void onMarkerDragStart(Marker marker) {
                LogUtil.i(TAG,String.format("onMarkerDragStart %s isSHow:%b",marker.getTitle(),marker.isInfoWindowShown()));
            }
            // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            @Override
            public void onMarkerDrag(Marker marker) {
                LogUtil.i(TAG,String.format("onMarkerDrag %s isSHow:%b",marker.getTitle(),marker.isInfoWindowShown()));
            }
            // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            @Override
            public void onMarkerDragEnd(Marker marker) {
                LogUtil.i(TAG,String.format("onMarkerDragEnd %s isSHow:%b",marker.getTitle(),marker.isInfoWindowShown()));
            }
        });*/
//
//aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
//    @Override
//    public void onMapLoaded() {
//        LogUtil.i(TAG,"OnMapLoadedListener");
//    }
//});
//aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
//    @Override
//    public void onTouch(MotionEvent motionEvent) {
//        LogUtil.i(TAG,"OnMapTouchListener:"+motionEvent.getAction());
//    }
//});
// https://a.amap.com/lbs/static/unzip/Android_Map_Doc/3D/com/amap/api/maps/model/AMapGestureListener.html
        aMap.setAMapGestureListener(new AMapGestureListener() {//屏幕层级
            @Override
            public void onDoubleTap(float v, float v1) {
//                LogUtil.i(TAG,String.format("setAMapGestureListener:onDoubleTap (%f,%f)",v,v1));
            }

            @Override
            public void onSingleTap(float v, float v1) {
//                LogUtil.i(TAG,String.format("setAMapGestureListener:onSingleTap (%f,%f)",v,v1));
            }

            @Override
            public void onFling(float v, float v1) {
//                LogUtil.i(TAG,String.format("setAMapGestureListener:onFling (%f,%f)",v,v1));
            }

            @Override
            public void onScroll(float v, float v1) {
//                LogUtil.i(TAG,String.format("setAMapGestureListener:onScroll (%f,%f) ",v,v1));
            }

            @Override
            public void onLongPress(float v, float v1) {
//                LogUtil.i(TAG,String.format("setAMapGestureListener:onLongPress (%f,%f)",v,v1));
            }

            @Override
            public void onDown(float v, float v1) {
//                LogUtil.i(TAG,String.format("setAMapGestureListener:onDown (%f,%f)",v,v1));
            }

            @Override
            public void onUp(float v, float v1) {
//                LogUtil.i(TAG,String.format("setAMapGestureListener:onUp (%f,%f)",v,v1));
            }

            @Override
            public void onMapStable() {//稳定后会调用此函数
                //其中aMap.getCameraPosition().zoom能获取到当前缩放层级，这个是会随缩放变化的,
                //获取屏幕上某点的地理坐标,监听aMap.setOnCameraChangeListener接口就可以获取屏幕中心的经纬度坐标了
                LatLng pt = aMap.getProjection().fromScreenLocation(new Point(((int)mMapView.getRight()/2), ((int)mMapView.getBottom()/2)));
                LogUtil.i(TAG,String.format("setAMapGestureListener: onMapStable zoom:%f map(%d,%d,%d,%d)(%f,%f) location(%f,%f)",aMap.getCameraPosition().zoom,
                        mMapView.getRight(),mMapView.getLeft(),mMapView.getBottom(),mMapView.getTop(),mMapView.getX(),mMapView.getY(),
                        pt.latitude,pt.longitude));
                getLocation(new LatLonPoint(pt.latitude,pt.longitude));
            }
        });

/*        //当aMap主动设置时调用
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LogUtil.i(TAG,String.format("onMapStable setOnCameraChangeListener:onCameraChangeFinish zoom:%f camera(%f,%f)",cameraPosition.zoom,cameraPosition.target.latitude,cameraPosition.target.longitude));
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LogUtil.i(TAG,String.format("onMapStable onCameraChangeFinish:onCameraChangeFinish zoom:%f camera(%f,%f)",cameraPosition.zoom,cameraPosition.target.latitude,cameraPosition.target.longitude));
            }
        });*/
    }
    GeocodeSearch geocoderSearch;

    void getLocation(LatLonPoint latLonPoint){
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            onPause();
        }else{
            onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁资源
        mClusterOverlay.onDestroy();
        mMapView.onDestroy();
    }

    class MapData{
        String titleStr;
        String detailStr;

        public MapData(String titleStr, String detailStr) {
            this.titleStr = titleStr;
            this.detailStr = detailStr;
        }

        public String getTitleStr() {
            return titleStr;
        }

        public void setTitleStr(String titleStr) {
            this.titleStr = titleStr;
        }

        public String getDetailStr() {
            return detailStr;
        }

        public void setDetailStr(String detailStr) {
            this.detailStr = detailStr;
        }
    }
    class ViewPagerHolder extends RecyclerView.ViewHolder{
        AppCompatTextView title,detail;
        public ViewPagerHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            detail = itemView.findViewById(R.id.detail);
        }

        public AppCompatTextView getTitle() {
            return title;
        }

        public void setTitle(AppCompatTextView title) {
            this.title = title;
        }

        public AppCompatTextView getDetail() {
            return detail;
        }

        public void setDetail(AppCompatTextView detail) {
            this.detail = detail;
        }
    }
    class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerHolder>{
        ArrayList<MapData> mData;
        public ViewPagerAdapter(){
            mData = new ArrayList<>();
        }

        public void setData(ArrayList<MapData> mData) {
            this.mData = new ArrayList<>(mData);
        }
        public void removeData(){
            mData.clear();
            notifyDataSetChanged();
        }
        public void addData(MapData data){
            if(mData.indexOf(data)<0){
                mData.add(data);
                notifyItemChanged(mData.size()-1);
            }
        }
        public int getPosition(MapData data){
            return mData.indexOf(data);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerHolder holder, int position) {
            MapData data = mData.get(position);
            holder.getTitle().setText(data.getTitleStr());
            holder.getDetail().setText(data.getDetailStr());
        }

        @NonNull
        @Override
        public ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewPagerHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.base_adapter_item,parent,false));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }


//************   Marker 动画 ************
    /**点击之后还原动画
     * setting original animation
     * 该动画并不能保证每次都成功不推荐使用
     */
    private void setClickedMarkerAnim(Marker mCurrentMemMarker) {
        if (mCurrentMemMarker != null) {
            Animation markerAnimation = new ScaleAnimation(1.6f, 1.0f, 1.6f, 1.0f); // update original view
            markerAnimation.setDuration(0);  //set anim time
            markerAnimation.setFillMode(1);
            mCurrentMemMarker.setAnimation(markerAnimation);
        }
    }
    /**点击之前放大动画
     * click then big view marker
     */
    private void setNotClickedMarkerAnim(Marker mCurrentMemMarker) {
        if (mCurrentMemMarker != null) {
            Animation markerAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f); //click then big view marker
            markerAnimation.setDuration(0);
            markerAnimation.setFillMode(1);
            mCurrentMemMarker.setAnimation(markerAnimation);
        }
    }
    int viewPagerShowHeight,menuStartHeight,menuEndHeight;
    private void openMenu(){
        AnimatorSet animatorSet = new AnimatorSet();
        AnimViewSize menuAnimView = new AnimViewSize(menuView);
        animatorSet.play(ObjectAnimator.ofInt(menuAnimView,"height",menuStartHeight,menuEndHeight))
                .before(ObjectAnimator.ofInt(tv2,"visibility",View.GONE,View.VISIBLE))
                .before(ObjectAnimator.ofInt(tv3,"visibility",View.GONE,View.VISIBLE));;
        animatorSet.setDuration(300);

        animatorSet.start();
    }
    private void closeMenu(){
        AnimatorSet animatorSet = new AnimatorSet();
        AnimViewSize menuAnimView = new AnimViewSize(menuView);
        animatorSet.play(ObjectAnimator.ofInt(tv2,"visibility",View.VISIBLE,View.GONE))
                .with(ObjectAnimator.ofInt(tv3,"visibility",View.VISIBLE,View.GONE))
                .before(ObjectAnimator.ofInt(menuAnimView,"height",menuEndHeight,menuStartHeight));
        animatorSet.setDuration(300);

        animatorSet.start();
    }
// viewPager展示动画
    private void showViewPager(){
        AnimatorSet animatorSet = new AnimatorSet();
        AnimViewSize menuAnimView = new AnimViewSize(menuView);
        animatorSet.play(ObjectAnimator.ofFloat(viewPager,"translationY",0,-500,-viewPagerShowHeight))
                .with(ObjectAnimator.ofFloat(codeView,"translationX",0,-200,-100));

        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewPager.setVisibility( View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                menuView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }
    private void hindViewPager(){
        if(viewPager.getVisibility()==View.GONE){
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        AnimViewSize menuAnimView = new AnimViewSize(menuView);
        animatorSet.play(ObjectAnimator.ofFloat(viewPager,"translationY",-viewPagerShowHeight,-500,0))
                .with(ObjectAnimator.ofFloat(codeView,"translationX",-100,-200,0));

        animatorSet.setDuration(170);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewPager.setVisibility( View.GONE);
                menuView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }
//    聚合点测试
    /**
     * 获取每个聚合点的绘制样式
     */
   /* private BitmapDescriptor getBitmapDes(Cluster mCluster) {
        BitmapDescriptor bitmapDescriptor;
        if(mCluster.getClusterCount() > 1){//当数量》1设置个数
            bitmapDescriptor = mLruCache.get(mCluster.getClusterCount());
            if (bitmapDescriptor == null) {
                TextView textView = new TextView(mContext);
                String tile = String.valueOf(mCluster.getClusterCount());
//                textView.setText("附近有\n"+tile+"个"+"\n坐标");
                textView.setText(tile);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                if (mClusterRender != null && mClusterRender.getDrawAble(mCluster.getClusterCount()) != null) {
                    textView.setBackgroundDrawable(mClusterRender.getDrawAble(mCluster.getClusterCount()));
                } else {
                    textView.setBackgroundResource(R.drawable.yuan);
                }
                bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
                mLruCache.put(mCluster.getClusterCount(), bitmapDescriptor);
            }
        }else{//否则，设置名称
            RegionItem mRegionItem= (RegionItem) mCluster.getClusterItems().get(0);
            bitmapDescriptor = mLruCacheName.get(mRegionItem.getTitle());
            if (bitmapDescriptor == null) {
                TextView textView = new TextView(mContext);
                textView.setText(mRegionItem.getTitle());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                if (mClusterRender != null && mClusterRender.getDrawAble(mCluster.getClusterCount()) != null) {
                    textView.setBackgroundDrawable(mClusterRender.getDrawAble(mCluster.getClusterCount()));
                } else {
                    textView.setBackgroundResource(R.drawable.yuan);
                }
                bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
                mLruCacheName.put(mRegionItem.getTitle(), bitmapDescriptor);
            }
        }
        return bitmapDescriptor;
    }*/
}
