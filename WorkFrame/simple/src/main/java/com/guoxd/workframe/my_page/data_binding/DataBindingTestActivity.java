package com.guoxd.workframe.my_page.data_binding;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.databinding.ActivityDataBindingBinding;
import com.guoxd.workframe.utils.CameraUtils;
import com.guoxd.workframe.utils.EasyGlideEngine;

import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ToastUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.luck.picture.lib.config.PictureConfig;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

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


    private void setPictureSelector(){
        //https://github.com/LuckSiege/PictureSelector/wiki/PictureSelector-Api%E8%AF%B4%E6%98%8E
//            PictureSelector.create(this)
//                    .openGallery(SelectMimeType.ofImage())
//                    .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
//                    //下面这些设置是必要的，否则拍照后不返回当前页面
//                    .selectionMode(PictureConfig.SINGLE )//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
//                    .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回,default false
////                    .isUseCustomCamera(true)// 开启自定义相机,要求录音权限，可能是为了控制拍照声
//                    .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    protected void setCamera(){
        EasyPhotos.createAlbum(this, true, EasyGlideEngine.getInstance())
                .setFileProviderAuthority(String.format("%s.fileprovider",getPackageName()))
                .start(PictureConfig.CAMERA_BEFORE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK ){
        return;
        }
           /* if(requestCode == PictureConfig.CHOOSE_REQUEST){
            List<LocalMedia> selectedPhotos = PictureSelector.obtainMultipleResult(data);
            LogUtil.e("Picture",String.format("PictureConfig result:%d",selectedPhotos.size()));
            for(LocalMedia url:selectedPhotos){
                LogUtil.e("Picture",String.format("get :%s",url.getPath()));
            }
            if(selectedPhotos.size() == 1){
                Glide.with(this).load(selectedPhotos.get(0).getPath()).into(mDataBinding.picPhoto);
            }
        }*/
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
