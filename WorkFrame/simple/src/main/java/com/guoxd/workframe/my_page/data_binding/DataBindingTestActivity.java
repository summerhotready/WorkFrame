package com.guoxd.workframe.my_page.data_binding;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.databinding.ActivityDataBindingBinding;
import com.guoxd.workframe.utils.GlideEngine;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ToastUtils;
//import com.huantansheng.easyphotos.EasyPhotos;
//import com.huantansheng.easyphotos.callback.SelectCallback;
//import com.huantansheng.easyphotos.models.album.entity.Photo;
//import com.huantansheng.easyphotos.setting.Setting;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class DataBindingTestActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_binding;
    }


    ActivityDataBindingBinding mDataBinding;
    @Override
    public void initView() {
        super.initView();
        mDataBinding = DataBindingUtil.setContentView(this,getLayoutId());
        PersonData personData = new PersonData("韩梅梅","18","排球");
        mDataBinding.setPerson(personData);

        HashMap<String,String> maps = new HashMap<>();
        maps.put("email","map:emma_guo@163.com");
        mDataBinding.setMaps(maps);
        List<String> lists = new ArrayList<>();
        lists.add("ArrayList01");
        lists.add("ArrayList02");
        lists.add("ArrayList03");
        mDataBinding.setLists(lists);
        String[] strings = new String[]{"Array01","Array02"};
        mDataBinding.setArrays(strings);

        mDataBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMsgToast(DataBindingTestActivity.this,"This is a Button");
            }
        });
        mDataBinding.picPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicePhotoWrapper(RC_CHOOSE_PHOTO);
            }
        });
        mDataBinding.picPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicePhotoWrapper(PictureConfig.CHOOSE_REQUEST);
            }
        });
        mDataBinding.picPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicePhotoWrapper(PictureConfig.REQUEST_CAMERA);
            }
        });

        // 给图片展示页面设置图片加载器,必选项目
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });


    }



    final int RC_CHOOSE_PHOTO = 101;


    private void choicePhotoWrapper(int type) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            switch(type){
                case RC_CHOOSE_PHOTO:
                    setISListConfig();
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    setPictureSelector();
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    setCamera();
                default:
                    break;
            }

        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", type, perms);
        }
    }
    private void setISListConfig(){
            // 自由配置选项
            ISListConfig config = new ISListConfig.Builder()
                    // 是否多选, 默认true
                    .multiSelect(false)
                    // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                    .rememberSelected(false)
                    // “确定”按钮背景色
                    .btnBgColor(Color.GRAY)
                    // “确定”按钮文字颜色
                    .btnTextColor(Color.BLUE)
                    // 使用沉浸式状态栏
                    .statusBarColor(Color.parseColor("#3F51B5"))
                    // 返回图标ResId
                    .backResId(R.drawable.ic_back)
                    // 标题
                    .title("图片")
                    // 标题文字颜色
                    .titleColor(Color.WHITE)
                    // TitleBar背景色
                    .titleBgColor(Color.parseColor("#3F51B5"))
                    // 裁剪大小。needCrop为true的时候配置
                    .cropSize(1, 1, 200, 200)
                    .needCrop(true)
                    // 第一个是否显示相机，默认true
                    .needCamera(true)
                    // 最大选择图片数量，默认9
                    .maxNum(9)
                    .build();

// 跳转到图片选择器
            ISNav.getInstance().toListActivity(this, config, RC_CHOOSE_PHOTO);
    }

    private void setPictureSelector(){
        //https://github.com/LuckSiege/PictureSelector/wiki/PictureSelector-Api%E8%AF%B4%E6%98%8E
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                    //下面这些设置是必要的，否则拍照后不返回当前页面
                    .selectionMode(PictureConfig.SINGLE )//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                    .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回,default false
//                    .isUseCustomCamera(true)// 开启自定义相机,要求录音权限，可能是为了控制拍照声
                    .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    protected void setCamera(){
        //创建相机：
      /*  EasyPhotos
//                .createCamera(this)//参数说明：上下文
        //创建相册：
        .createAlbum(this, true,new com.huantansheng.easyphotos.engine.ImageEngine(){
            @Override
            public void loadPhoto(@NonNull Context context, @NonNull Uri uri, @NonNull ImageView imageView) {
                Glide.with(context).load(uri).into(imageView);
            }

            @Override
            public void loadGif(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {

            }

            @Override
            public void loadGifAsBitmap(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {

            }

            @Override
            public Bitmap getCacheBitmap(@NonNull Context context, @NonNull Uri uri, int width, int height) throws Exception {
                return null;
            }
        })//参数说明：上下文，是否显示相机按钮，图片加载引擎实现(ImageEngine说明)

//        配置FileProvider字符串：
        .setFileProviderAuthority("com.huantansheng.easyphotos.sample.fileprovider")//参数说明：见下方FileProvider的配置

//        设置选择数：
//        .setCount(9)//参数说明：最大可选数，默认1
//        设置选择图片数(设置此参数后setCount失效)
        .setPictureCount(1)



//        设置显示照片的最小文件大小：
//        setMinFileSize(1024*10)//参数说明：最小文件大小，单位Bytes
//        设置显示照片的最小宽度：
        .setMinWidth(200)//参数说明：显示照片的最小宽度，单位Px
//        设置显示照片的最小高度：
        .setMinHeight(200)//参数说明：显示照片的最小高度，单位Px

//        设置是否显示Gif动图：
        .setGif(false)//参数说明：相册中是否显示Gif动图。默认不显示，boolean类型。

//        设置是否显示视频：
        .setVideo(false)//参数说明：相册中是否显示视频。默认不显示，boolean类型。
//        设置选择视频数(设置此参数后setCount失效)
//        .setVideoCount(int selectorMaxCount)
//        过滤掉小于多少时长的视频：
//        setVideoMinSecond(int second)
//        过滤掉大于多少时长的视频：
//        setVideoMaxSecond(int second)

//        设置只显示某种类型的文件：
//        filter(String... types)//支持Type.GIF和Type.VIDEO，前提是已经选择显示了gif和video

//        设置默认选择图片集合方式一：
//        setSelectedPhotos(mSelectedPhotos)//参数说明：用户上一次勾选过的图片集合，ArrayList<Photo>类型
//        设置默认选择图片集合方式二：
//        setSelectedPhotoPaths(selectedPhotoPathList)//参数说明:用户上一次勾选过的图片地址集合，ArrayList<String>类型
//        设置原图按钮：
        .setOriginalMenu(false, true, "该功能为VIP会员特权功能")//参数说明：是否默认选中，是否可用，不可用时用户点击将toast信息。不执行该方法则不显示原图按钮。是否默认选中可以根据EasyPhotos的回调走，回调中会给出用户上一次是否选择了原图选项的标识

//        设置是否显示拼图按钮：
        .setPuzzleMenu(false)//参数说明：是否显示。默认是显示的。

//        设置是否显示清空按钮：
        .setCleanMenu(true)//参数说明：是否显示。默认是显示的。

//        设置是否显示相册页底部中间的编辑按钮
//        当清空按钮、原图按钮、拼图按钮都不显示时，编辑按钮自动隐藏。其余条件均显示。

//        设置广告：
//        setAdView(photosAdView, photosAdIsLoaded, albumItemsAdView, albumItemsAdIsLoaded)//参数说明：相册中的广告view，相册中的广告View数据是否绑定完毕，专辑列表广告View，专辑列表广告View数据是否绑定完毕。不执行该方法则不使用广告填充功能。广告view可以传空，适用于VIP不显示广告场景

//        刷新图片列表广告数据:
//        EasyPhotos.notifyPhotosAdLoaded()
//        刷新专辑项目列表广告:
//        EasyPhotos.notifyAlbumItemsAdLoaded()
//        设置相机按钮位置：
        .setCameraLocation(Setting.LIST_FIRST)//@Setting.Location默认左下角，通过设置可设置为相册第一张图片的位置
//        新的启动相机或相册，通过接口获取回调数据：
        .start(new SelectCallback(){
            @Override
            public void onResult(ArrayList<Photo> selectedPhotos, boolean isOriginal) {
//                List<String> selectedPhotos = data.getStringArrayListExtra("result");
                LogUtil.e("Picture",String.format("Easy result:%d",selectedPhotos.size()));
                for(Photo url:selectedPhotos){
                    LogUtil.e("Picture",String.format("get :%s",url.path));
                }
                if(selectedPhotos.size() == 1){
                    Glide.with(DataBindingTestActivity.this).load(selectedPhotos.get(0)).into(mDataBinding.picPhoto3);
                }
            }
        });//SelectCallback*/

//        图片最小文件大小、图片最小宽度、图片最小高度，如果单一设置，满足条件即过滤，如果多项设置，满足一项即过滤
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK ){
        return;
        }
            if(requestCode == RC_CHOOSE_PHOTO) {
            List<String> selectedPhotos = data.getStringArrayListExtra("result");
            LogUtil.e("Picture",String.format("IS result:%d",selectedPhotos.size()));
            for(String url:selectedPhotos){
                LogUtil.e("Picture",String.format("get :%s",url));
            }
            if(selectedPhotos.size() == 1){
                Glide.with(this).load(selectedPhotos.get(0)).into(mDataBinding.picPhoto);
            }
        }
            if(requestCode == PictureConfig.CHOOSE_REQUEST){
                List<LocalMedia> selectedPhotos = PictureSelector.obtainMultipleResult(data);
                LogUtil.e("Picture",String.format("PictureConfig result:%d",selectedPhotos.size()));
                for(LocalMedia url:selectedPhotos){
                    LogUtil.e("Picture",String.format("get :%s",url.getPath()));
                }
                if(selectedPhotos.size() == 1){
                    Glide.with(this).load(selectedPhotos.get(0).getPath()).into(mDataBinding.picPhoto2);
                }
            }
            if(requestCode ==PictureConfig.REQUEST_CAMERA){
                List<LocalMedia> selectedPhotos = PictureSelector.obtainMultipleResult(data);
                LogUtil.e("Picture",String.format("PictureConfig result:%d",selectedPhotos.size()));
                if(selectedPhotos.size() == 1){
                    Glide.with(this).load(selectedPhotos.get(0).getPath()).into(mDataBinding.picPhoto3);
                }
            }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch(requestCode){
            case RC_CHOOSE_PHOTO:
                setISListConfig();
                break;
            case PictureConfig.CHOOSE_REQUEST:
                setPictureSelector();
                break;
            case PictureConfig.REQUEST_CAMERA:
                setCamera();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this);
    }
}
