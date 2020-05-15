package com.guoxd.workframe.my_page.data_binding;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.databinding.ActivityDataBindingBinding;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ToastUtils;
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
                choicePhotoWrapper();
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



    int RC_CHOOSE_PHOTO = 101;
    int PRC_PHOTO_PICKER = 102;

    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {

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
                    .backResId(R.drawable.eye_off)
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
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            List<String> selectedPhotos = data.getStringArrayListExtra("result");
            LogUtil.e("Picture",String.format("result:%d",selectedPhotos.size()));
            for(String url:selectedPhotos){
                LogUtil.e("Picture",String.format("get :%s",url));
            }
            if(selectedPhotos.size() == 1){
                Glide.with(this).load(selectedPhotos.get(0)).into(mDataBinding.picPhoto);
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == PRC_PHOTO_PICKER){
            choicePhotoWrapper();
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
