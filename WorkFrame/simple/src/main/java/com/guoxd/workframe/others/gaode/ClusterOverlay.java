package com.guoxd.workframe.others.gaode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.guoxd.work_frame_library.shape.ArrowSharpDrawable;
import com.guoxd.workframe.R;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ViewHelpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by yiyi.qi on 16/10/10.
 * 整体设计采用了一个线程，负责处理Marker相关操作
 *
 */
public class ClusterOverlay implements AMap.OnCameraChangeListener{
    private AMap mAMap;
    private Context mContext;
    //管理地图上的marker点
    private List<Marker> mAddMarkers = new ArrayList<Marker>();
//聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
    private int mClusterSize;
    private ClusterClickListener mClusterClickListener;

    private LruCache<String,BitmapDescriptor> mLruCache;
    //缓存点聚合背景（几种）
    private final int FLAG_BACKGROUND_NORMAL = 21;
    private final int FLAG_BACKGROUND_ALARM = 22;
    private final int FLAG_BACKGROUND_OFFLINE = 23;
//    private final int FLAG_BACKGROUND_ONLINE = 21;
    private HashMap<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
//    private LruCache<Integer, BitmapDescriptor> mLruCache;
    private HandlerThread mMarkerHandlerThread = new HandlerThread("addMarker");
    private HandlerThread mSignClusterThread = new HandlerThread("calculateCluster");
    //绘制线程
    private Handler mMarkerhandler;
//    private Handler mSignClusterHandler;
    private float mPXInMeters;
    private boolean mIsCanceled = false;

//    single marker
    private final int FLAG_MAP_DATA_CLUSTER = 31;
    private final int FLAG_MAP_DATA_POINT = 32;
    private final int FLAG_MAP_DATA_SINGLE = 33;
    ArrayList<Integer> flagMapData = new ArrayList<>(FLAG_MAP_DATA_CLUSTER);

    Marker currentMarker=null;
    /**
     * 构造函数
     *
     * @param amap
     * @param clusterSize 聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
     * @param context
     */
    public ClusterOverlay(AMap amap, int clusterSize, Context context) {
        this(amap, null, clusterSize, context);


    }

    /**
     * 构造函数,批量添加聚合元素时,调用此构造函数
     *
     * @param amap
     * @param pointItems 聚合元素
     * @param clusterSize
     * @param context
     */
    public ClusterOverlay(AMap amap, List<PointItem> pointItems,
                          int clusterSize, Context context) {
        //默认最多会缓存80张图片作为聚合显示元素图片,根据自己显示需求和app使用内存情况,可以修改数量
        mLruCache = new LruCache<String, BitmapDescriptor>(80) {
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                reycleBitmap(oldValue.getBitmap());
            }
        };

        mContext = context;
//        mPointItems = new ArrayList<>();
        this.mAMap = amap;
        mClusterSize = clusterSize;
        mPXInMeters = mAMap.getScalePerPixel();
//        mClusterDistance = mPXInMeters * mClusterSize;
        //开启以中心点进行手势操作的方法
        amap.getUiSettings().setGestureScaleByMapCenter(true);

        amap.setOnCameraChangeListener(this);
        amap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            //点击事件
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mClusterClickListener == null) {
                    return true;
                }
//        聚合点，按照
                if(marker.getObject() instanceof PointItem){
                    mClusterClickListener.onClusterClick((PointItem)(marker.getObject()));
                    return true;
                }
                if(marker.getObject() instanceof DeviceItem){
                    showSingleMarker((DeviceItem) marker.getObject());
                    return true;
                }
                LogUtil.i(TAG,"setOnMarkerClickListener");
                return false;
            }
        });
        amap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                int last = flagMapData.size()-1;
                if(flagMapData.get(last) == FLAG_MAP_DATA_SINGLE){
                    flagMapData.remove(last);
                    if(currentMarker!=null){
                        showAllMarkers();
                    }
                }else{
LogUtil.i(TAG,"setOnMapClickListener");
                }
            }
        });
       /* setClusterRenderer(new ClusterRender() {
            @Override
            public Drawable getDrawAble(PointItem pointItem) {
                Drawable bitmapDrawable = mBackDrawAbles.get(pointItem.isAlarm()? 1:2);
                if (bitmapDrawable == null) {
//                    bitmapDrawable =
//                            getResources().getDrawable(
//                                    R.drawable.icon_openmap_mark);
                    bitmapDrawable = new BitmapDrawable(null, drawCircle(ViewHelpUtils.dp2px(mContext, 100),
                            pointItem.isAlarm?
                            Color.argb(199, 217, 114, 0):Color.argb(199, 0, 114, 217)));
//                        bitmapDrawable.set
                    mBackDrawAbles.put(pointItem.isAlarm()? 1:2, bitmapDrawable);
                }
                return bitmapDrawable;
            }
        });*/

       amap.setInfoWindowAdapter(new DeviceInfoWindow());
        initThreadHandler();
        if (pointItems != null) {
            assignClusters(pointItems);
        }
    }
    public void addClusters(List<PointItem> pointItems){
        flagMapData.clear();
        flagMapData.add(FLAG_MAP_DATA_CLUSTER);
        assignClusters(pointItems);
    }

    private Bitmap drawCircle(int radius, int color) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
//        canvas.drawArc(rectF, 0, 360, true, paint);
        canvas.drawRect(rectF,paint);
        return bitmap;
    }

    private void reycleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        //高版本不调用recycle
        if (Build.VERSION.SDK_INT <= 10) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 设置聚合点的点击事件
     *
     * @param clusterClickListener
     */
    public void setOnClusterClickListener(
            ClusterClickListener clusterClickListener) {
        mClusterClickListener = clusterClickListener;
    }


/*
    public void addClusterItem(ClusterItem item) {
        Message message = Message.obtain();
        message.what = SignClusterHandler.CALCULATE_SINGLE_CLUSTER;
        message.obj = item;
        mSignClusterHandler.sendMessage(message);
    }
*/


    public void onDestroy() {
        mIsCanceled = true;
        mMarkerhandler.removeCallbacksAndMessages(null);
        mMarkerHandlerThread.quit();
//        mSignClusterHandler.removeCallbacksAndMessages(null);
//        mSignClusterThread.quit();
        for (Marker marker : mAddMarkers) {
            marker.remove();
        }
        mAddMarkers.clear();

        mLruCache.evictAll();
    }

    private void clearMarkers(){
        //        删除地图上的marker点
        ArrayList<Marker> removeMarkers = new ArrayList<>();
        removeMarkers.addAll(mAddMarkers);
        //设置隐藏动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(1, 0);
        RemoveMarkerAnimationListener myAnimationListener=new RemoveMarkerAnimationListener(removeMarkers);
        for (Marker marker : removeMarkers) {
            marker.setAnimation(alphaAnimation);
            marker.setAnimationListener(myAnimationListener);
            marker.startAnimation();
        }
    }

    //初始化Handler
    private void initThreadHandler() {
        mMarkerHandlerThread.start();
        mSignClusterThread.start();
        mMarkerhandler = new MarkerHandler(mMarkerHandlerThread.getLooper());
//        mSignClusterHandler = new SignClusterHandler(mSignClusterThread.getLooper());
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {


    }

    @Override
    public void onCameraChangeFinish(CameraPosition arg0) {
//        mPXInMeters = mAMap.getScalePerPixel();
//        mClusterDistance = mPXInMeters * mClusterSize;
//        assignClusters();
    }




    /**addMarker
     * 在地图上生成点聚合元素绑定到PointItem，通过mPointItems遍历
     * 此处没有直接清空添加，而是通过RemoveMarkerAnimationListener 动画结束后删除
     */
    private void addClusterToMap(ArrayList<PointItem> pointItems) {
        clearMarkers();
        //存放所有点的经纬度
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//      生成新的marker点绑定到PointItem单元
        for (PointItem cluster : pointItems) {
            addSingleClusterToMap(cluster);
            //将所有marker经纬度include到boundsBuilder中
            boundsBuilder.include(cluster.getCenterLatLng());
        }
        //更新地图状态
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), mClusterSize));
    }


    //全局共用一个
    private AlphaAnimation mADDAnimation=new AlphaAnimation(0, 1);
    /**
     * 生成单个点聚合元素，添加到地图并绑定到PointItem单元上
     *
     * @param cluster
     */
    private void addSingleClusterToMap(PointItem cluster) {
        cluster.setMarker( addMarker(getClusterBitmap(cluster), null,cluster.getCenterLatLng(),cluster));
    }
    /**
     * 更新已加入地图聚合点的样式
     */
    private void updateCluster(PointItem cluster) {
        Marker marker = cluster.getMarker();
        marker.setIcon(getClusterBitmap(cluster));
    }

/*    private Marker addClusterMarker(BitmapDescriptor bitmapDescriptor,LatLng latLng,Object object){
        Marker marker =addMarker(bitmapDescriptor,null,latLng,object);
        marker.set
        return ;
    }*/
    private Marker addMarker(BitmapDescriptor bitmapDescriptor,ArrayList<BitmapDescriptor> list,LatLng latLng,Object object){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f, 0.5f)
                .position(latLng);
        if(bitmapDescriptor!=null) {
            markerOptions.icon(bitmapDescriptor);
        }
        if(list!=null){
            markerOptions.icons(list).period(10);
        }

        Marker marker = mAMap.addMarker(markerOptions);
        marker.setAnimation(mADDAnimation);
        marker.setObject(object);
        marker.startAnimation();

        mAddMarkers.add(marker);
        return marker;
    }

//重新计算聚合点
 /*   private void calculateClusters() {
        Log.i(TAG,"calculateClusters");
        mIsCanceled = false;
        mClusters.clear();
        //此处是计算的关键
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
        for (ClusterItem clusterItem : mClusterItems) {
            if (mIsCanceled) {
                return;
            }
            LatLng latlng = clusterItem.getPosition();
            if (visibleBounds.contains(latlng)) {
                Cluster cluster = getCluster(latlng,mClusters);
                if (cluster != null) {
                    cluster.addClusterItem(clusterItem);
                } else {
                    cluster = new Cluster(latlng);
                    mClusters.add(cluster);
                    cluster.addClusterItem(clusterItem);
                }

            }
        }

        //复制一份数据，规避同步
        List<Cluster> clusters = new ArrayList<Cluster>();
        clusters.addAll(mClusters);
        Message message = Message.obtain();
        message.what = MarkerHandler.ADD_CLUSTER_LIST;
        message.obj = clusters;
        if (mIsCanceled) {
            return;
        }
        mMarkerhandler.sendMessage(message);//通知
    }*/

    /**
     * 对点进行聚合入口
     */
    private void assignClusters(List<PointItem> pointItems) {
        mIsCanceled = true;
        mMarkerhandler.removeMessages(MarkerHandler.ADD_CLUSTER_LIST);
        Message message = Message.obtain();
        message.obj = pointItems;
        message.what = MarkerHandler.ADD_CLUSTER_LIST;
        mMarkerhandler.sendMessage(message);
    }


    /**
     * 获取每个聚合点的绘制样式
     */
    private BitmapDescriptor getClusterBitmap(PointItem pointItem) {
        BitmapDescriptor bitmapDescriptor = mLruCache.get(pointItem.getPointName());
        if(bitmapDescriptor!=null){
            bitmapDescriptor.recycle();
        }
//        if (bitmapDescriptor == null) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            if(!pointItem.isOnline()){
                linearLayout.setBackground(getClusterBackground(FLAG_BACKGROUND_OFFLINE));
            }else{
                if(pointItem.isAlarm()){
                    linearLayout.setBackground(getClusterBackground(FLAG_BACKGROUND_ALARM));
                }else{
                    linearLayout.setBackground(getClusterBackground(FLAG_BACKGROUND_NORMAL));
                }
            }

            TextView textView = new TextView(mContext);
            TextView textTitle = new TextView(mContext);
            textTitle.setText(pointItem.pointName);
            textView.setText(String.format("总数：%d",pointItem.count));
            textTitle.setTextColor(Color.WHITE);
            textTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            linearLayout.addView(textTitle);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            linearLayout.addView(textView);
            bitmapDescriptor = BitmapDescriptorFactory.fromView(linearLayout);
            mLruCache.put(pointItem.getPointName(), bitmapDescriptor);

//        }
        return bitmapDescriptor;
    }

    private Drawable getClusterBackground(int flag){
        Drawable bitmapDrawable = mBackDrawAbles.get(flag);
        if (bitmapDrawable == null) {
            switch(flag){
                case FLAG_BACKGROUND_ALARM:
                    bitmapDrawable = new BitmapDrawable(null, drawCircle(ViewHelpUtils.dp2px(mContext, 100),ContextCompat.getColor(mContext,android.R.color.holo_red_dark)));
                    break;
                case FLAG_BACKGROUND_OFFLINE:
                    bitmapDrawable = new BitmapDrawable(null, drawCircle(ViewHelpUtils.dp2px(mContext, 100),ContextCompat.getColor(mContext,android.R.color.darker_gray)));
                    break;
                default:
                    bitmapDrawable = new BitmapDrawable(null, drawCircle(ViewHelpUtils.dp2px(mContext, 100),ContextCompat.getColor(mContext,android.R.color.holo_blue_dark)));
            }
            mBackDrawAbles.put(flag,bitmapDrawable);
        }
       return bitmapDrawable;
    }
//--------------------------------散列点--------------------------------------------
public void addDevice(List<DeviceItem> deviceItems){
    flagMapData.add(FLAG_MAP_DATA_POINT);
    mIsCanceled = true;
    mMarkerhandler.removeMessages(MarkerHandler.ADD_POINT_LIST);
    Message message = Message.obtain();
    message.what = MarkerHandler.ADD_POINT_LIST;
    message.obj = deviceItems;
    mMarkerhandler.sendMessage(message);
    mMarkerhandler.sendMessage(message);
}
    private void addPointToMap(List<DeviceItem> deviceItems) {
        clearMarkers();
//      生成新的marker点绑定到PointItem单元
        for (DeviceItem cluster : deviceItems) {
            addSinglePointToMap(cluster);
        }
    }
    private void addSinglePointToMap(DeviceItem cluster) {
//        ArrayList<BitmapDescriptor> bitmapDescriptors = getDeviceBitmap(cluster);
        cluster.setMarker( addMarker(
                getDeviceBitmap(cluster),null,
                cluster.getPosition(),cluster));
    }
private BitmapDescriptor getDeviceBitmap(DeviceItem deviceItem) {
    LinearLayout linearLayout = new LinearLayout(mContext);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setGravity(Gravity.CENTER);
    ArrowSharpDrawable bgDrawable = new ArrowSharpDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
    bgDrawable.setCornerRadius(50);
    bgDrawable.setRelativePosition(0.5f);
    bgDrawable.setArrowPath(ArrowSharpDrawable.ArrowDirection.BOTTOM,
            ViewHelpUtils.dp2px(mContext,6),ViewHelpUtils.dp2px(mContext,10));
//    linearLayout.setPadding(,sp_6,CommonUtil.dp2px(mContext,13),CommonUtil.dp2px(mContext,16));
    linearLayout.setGravity(Gravity.CENTER);

    AppCompatTextView textTitle = new AppCompatTextView(mContext);
    textTitle.setText(deviceItem.getTitle());
    textTitle.setTextColor(Color.WHITE);
    textTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
    textTitle.setPadding(ViewHelpUtils.dp2px(mContext,6),0,0,0);
    bgDrawable.setBgColor(Color.parseColor("#FE4F4C"));

    linearLayout.setBackground(bgDrawable);
    linearLayout.addView(textTitle,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(linearLayout);
    return bitmapDescriptor;
}

    public Bitmap getScaleBitmap(Bitmap bitmap,int size){
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            //放大為屏幕的1/2大小
//        float screenWidth  = getWindowManager().getDefaultDisplay().getWidth();		// 屏幕宽（像素，如：480px）
//        float screenHeight = getWindowManager().getDefaultDisplay().getHeight();		// 屏幕高（像素，如：800p）
//        Log.d("screen",screenWidth+"");
            float scaleWeight = size / width;
//        float scaleHeight = screenWidth/2/width;

            if(scaleWeight<1){
                return bitmap;
            }
            // 取得想要缩放的matrix參數
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWeight, scaleWeight);
            // 得到新的圖片
            Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return newbm;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
//    -----------------------------单点使用-------------------------

    /**地图上仅显示
     * 点击后将点移动到中心
     * @param
     */
    public void showSingleMarker( DeviceItem deviceItem) {
        flagMapData.add( FLAG_MAP_DATA_SINGLE);
        currentMarker = deviceItem.getMarker();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                deviceItem.getPosition(), 30, 0, 0));
        mAMap.animateCamera(cameraUpdate, 800, null);
        for (Marker marker : mAddMarkers) {
            if(currentMarker.equals(marker)){
                marker.setVisible(true);
            }else{
                marker.setVisible(false);
            }
        }
        if (!currentMarker.isInfoWindowShown()) {
            currentMarker.showInfoWindow();
        }

    }
    /**
     * 显示全部Marker点
     */
    public void showAllMarkers() {//
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (Marker marker : mAddMarkers) {
            marker.setVisible(true);
            if(marker.isInfoWindowShown()){
                marker.hideInfoWindow();
            }
            boundsBuilder.include(marker.getPosition());
        }
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), mClusterSize));
    }
//-----------------------辅助内部类用---------------------------------------------

    class DeviceInfoWindow implements AMap.ImageInfoWindowAdapter{
        View infoWindow = null;//缓存后不显示
        @Override
        public long getInfoWindowUpdateTime() {
            return 0;
        }

        @Override
        public View getInfoWindow(Marker marker) {//
//            if(infoWindow == null) {
                infoWindow = LayoutInflater.from(mContext).inflate(
                        R.layout.include_pickerview_topbar, null);
//            }
            render(marker,infoWindow);
            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
        public void render(Marker marker,View view){
            DeviceItem deviceItem = (DeviceItem) marker.getObject();
            TextView title = view.findViewById(R.id.tvTitle);
            title.setText(deviceItem.getTitle());
        }
    }
    /**
     * marker渐变动画，动画结束后将Marker删除
     */
    class RemoveMarkerAnimationListener implements Animation.AnimationListener {
        private  List<Marker> mRemoveMarkers ;

        RemoveMarkerAnimationListener(List<Marker> removeMarkers) {
            mRemoveMarkers = removeMarkers;
        }

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            for(Marker marker:mRemoveMarkers){
                marker.remove();
            }
            mRemoveMarkers.clear();
        }
    }

    /**
     * 处理market添加，更新等操作
     */
    class MarkerHandler extends Handler {
//      点聚合
        static final int ADD_CLUSTER_LIST = 0;
        static final int ADD_SINGLE_CLUSTER = 1;
        static final int UPDATE_SINGLE_CLUSTER = 2;
//        散列点
        static final int ADD_POINT_LIST = 10;
        static final int ADD_SINGLE_POINT = 11;
        static final int UPDATE_SINGLE_POINT = 12;

        MarkerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Log.i(TAG,String.format("MarkerHandler thread:%s %d",Thread.currentThread().getName(),message.what));
            switch (message.what) {
                case ADD_CLUSTER_LIST:
                    ArrayList<PointItem> pointItems = (ArrayList<PointItem>)message.obj;
                    addClusterToMap(pointItems);
                    break;
                case ADD_SINGLE_CLUSTER:
                    PointItem cluster = (PointItem) message.obj;
                    addSingleClusterToMap(cluster);
                    break;
                case UPDATE_SINGLE_CLUSTER:
                    PointItem updateCluster = (PointItem) message.obj;
                    updateCluster(updateCluster);
                    break;
                case ADD_POINT_LIST:
                    ArrayList<DeviceItem> deviceItems = (ArrayList<DeviceItem>)message.obj;
                    addPointToMap(deviceItems);
                    break;
            }
        }
    }
String TAG = getClass().getName();
    /**
     * 处理聚合点算法线程
     */
/*    class SignClusterHandler extends Handler {
//        static final int CALCULATE_CLUSTER = 0;
        static final int CALCULATE_SINGLE_CLUSTER = 1;

        SignClusterHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Log.i(TAG,String.format("SignClusterHandler thread:%s %d",Thread.currentThread().getName(),message.what));
            switch (message.what) {
//                case CALCULATE_CLUSTER:
//                    calculateClusters();
                    break;
                case CALCULATE_SINGLE_CLUSTER:
                    ClusterItem item = (ClusterItem) message.obj;
                    mClusterItems.add(item);
//                    Log.i("yiyi.qi","calculate single cluster");
                    calculateSingleCluster(item);
                    break;
            }
        }
    }*/
}