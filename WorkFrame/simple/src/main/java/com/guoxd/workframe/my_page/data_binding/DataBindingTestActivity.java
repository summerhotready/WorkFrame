package com.guoxd.workframe.my_page.data_binding;

import static com.luck.picture.lib.config.PictureSelectionConfig.compressFileEngine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.databinding.ActivityDataBindingBinding;
import com.guoxd.workframe.utils.CameraUtils;
import com.guoxd.workframe.utils.EasyGlideEngine;

import com.guoxd.workframe.utils.GlideEngine;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ToastUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnNewCompressListener;

public class DataBindingTestActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_binding;
    }


    CameraUtils cameraUtils;
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

//        mDataBinding.setArrays(strings);
        cameraUtils = new CameraUtils();

        mDataBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMsgToast(DataBindingTestActivity.this,"This is a Button");
            }
        });
        mDataBinding.picPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicePhotoWrapper(PictureConfig.CHOOSE_REQUEST);
            }
        });
        mDataBinding.picPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicePhotoWrapper(PictureConfig.CAMERA_BEFORE);
            }
        });
        mDataBinding.picPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicePhotoWrapper(PictureConfig.REQUEST_CAMERA);
            }
        });
        mDataBinding.btnChangeLanguage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void choicePhotoWrapper(int type) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            switch(type){
                case PictureConfig.CHOOSE_REQUEST:
                    setPictureSelector();
                    break;
                case PictureConfig.CAMERA_BEFORE:
                    setCamera();
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    startActivityForResult(cameraUtils.dispatchCaptureIntent(this),PictureConfig.REQUEST_CAMERA);
                    break;
                default:
                    break;
            }
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", type, perms);
        }
    }


    private void setPictureSelector(){     //https://github.com/LuckSiege/PictureSelector
        //相册
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(9)
                .setMinSelectNum(1)
//                .setCompressEngine(CompressFileEngine)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCompressEngine(compressFileEngine)//                    .compress()// 是否压缩 true or false
                .setImageSpanCount(4)
                .setSelectionMode(SelectModeConfig.MULTIPLE)//                    .selectionMode(PictureConfig.MULTIPLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);
//拍照
/*        PictureSelector.create(this)
                .openCamera(SelectMimeType.ofImage())
                .setCompressEngine(compressFileEngine)
                .forResultActivity(PictureConfig.CHOOSE_REQUEST);*/

    }
    CompressFileEngine compressFileEngine = new CompressFileEngine(){
        @Override
        public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
            //ignoreBy设置的mLeastCompressSize不是图片的尺寸，而是压缩相关的数据，数字越大图片越大。500已经接近原图
            Luban.with(context).load(source).ignoreBy(100)
                    .setCompressListener(new OnNewCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(String source, File compressFile) {
                            if (call != null) {
                                call.onCallback(source, compressFile.getAbsolutePath());
                            }
                        }

                        @Override
                        public void onError(String source, Throwable e) {
                            if (call != null) {
                                call.onCallback(source, null);
                            }
                        }
                    }).launch();
        }
    };


    protected void setCamera(){
        EasyPhotos.createAlbum(this, true, EasyGlideEngine.getInstance())
                .setFileProviderAuthority(String.format("%s.fileprovider",getPackageName()))
                .start(PictureConfig.CAMERA_BEFORE);
    }
//    List<LocalMedia> selectedPhotos=new ArrayList<>()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != RESULT_OK ){
                return;
            }
            if(requestCode == PictureConfig.CHOOSE_REQUEST){
                ArrayList<LocalMedia> selectedPhotos = PictureSelector.obtainSelectorList(data);
            LogUtil.e("Picture",String.format("PictureConfig result:%d",selectedPhotos.size()));
            for(LocalMedia url:selectedPhotos){
                LogUtil.e("Picture",String.format("get :%s",url.getPath()));
            }
            if(selectedPhotos.size() >= 1){
                Glide.with(this).load(selectedPhotos.get(0).getPath()).into(mDataBinding.picPhoto);
            }
//预览，不显示删除
              /*  mDataBinding.picPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PictureSelector.create(DataBindingTestActivity.this)
                                .openPreview()
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .setExternalPreviewEventListener(new OnExternalPreviewEventListener() {
                                    @Override
                                    public void onPreviewDelete(int position) {

                                    }

                                    @Override
                                    public boolean onLongPressDownload(LocalMedia media) {
                                        return false;
                                    }
                                })
                                .startActivityPreview(0, false, selectedPhotos);
                    }
                });*/
        }
            if(requestCode ==PictureConfig.CAMERA_BEFORE){
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> selectedPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);

                LogUtil.e("Picture",String.format("PictureConfig result:%d  %b",selectedPhotos.size(),selectedOriginal));
                if(selectedPhotos.size() == 1){
                    Glide.with(this).load(selectedPhotos.get(0).uri).into(mDataBinding.picPhoto2);
                }
            }
            if(requestCode == PictureConfig.REQUEST_CAMERA){
                Glide.with(this).load(cameraUtils.getCameraUri()).into(mDataBinding.picPhoto3);
            }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch(requestCode){

            case PictureConfig.CHOOSE_REQUEST:
                setPictureSelector();
                break;
            case PictureConfig.CAMERA_BEFORE:
                setCamera();
                break;
            case PictureConfig.REQUEST_CAMERA:
                startActivityForResult(cameraUtils.dispatchCaptureIntent(this),PictureConfig.REQUEST_CAMERA);
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
