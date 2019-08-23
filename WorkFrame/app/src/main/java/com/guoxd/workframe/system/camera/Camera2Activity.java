package com.guoxd.workframe.system.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.utils.FileUtils;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Camera2+TextureView
 * TextureView只能运行在硬件加速窗口
 */
@TargetApi(21)
public class Camera2Activity extends BaseActivity implements View.OnClickListener {
    private Button btnTakePic;
    private Button btnCancle;
    private TextureView mTextureView;

    private ImageView view;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private String mCameraId;
    private Size mPreviewSize;
    private int mImageSize = 0;
    private int mWidth ;//获取相机宽度
    private int mHeight ;//获取相机高度

    Bitmap mBitmap;
    //
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private static SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }
    private CameraCaptureSession mSession;
    private CaptureRequest.Builder mBuilder;

    //CameraDeviceandroid.hardware.Camera也就是Camera1的Camera
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    };
    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.system_camera_activity_camera2;
    }

    public void initView() {
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        //照相机预览的空间
        mTextureView = (TextureView) findViewById(R.id.surfaceView);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override //解决画面拉伸的问题
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //可用
                mWidth = width;
                mHeight = height;
                getCameraId();
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                //改变
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                //释放
                stopCamera();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                //更新
            }
        });

        btnTakePic = (Button)findViewById(R.id.bnt_takepicture);
        btnCancle = (Button)findViewById(R.id.bnt_enter);
        btnCancle.setVisibility(View.GONE);

        btnTakePic.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        view = findViewById(R.id.view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImageSize = view.getWidth();
                view.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bnt_takepicture:
                if(btnCancle.getVisibility() == View.GONE){//拍照
                    Bitmap bitmap = mTextureView.getBitmap();
                    LogUtil.e(TAG,String.format("getBitmap width:%d,height:%d",bitmap.getWidth(),bitmap.getHeight()));
                    mBitmap= cropBitmap(bitmap);
                    LogUtil.e(TAG,String.format("mBitmap width:%d,height:%d",mBitmap.getWidth(),mBitmap.getHeight()));
                    view.setImageBitmap(mBitmap);
                    btnTakePic.setText("确定");
                    btnCancle.setVisibility(View.VISIBLE);
                }else{//确认
                    if(setBimapForFile()){//save
                        LogUtil.e(TAG,"save ok");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }else{
                        LogUtil.e(TAG,"save error");
                    }
                }
                break;
            case R.id.bnt_enter://取消
                mBitmap = null;
                view.setImageResource(android.R.color.transparent);
                btnTakePic.setText("拍照");
                btnCancle.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 活动获取照相机ID
     * 前置为1，后置为0
     */
    private void getCameraId() {
        try {
            //获取可用相机列表
            LogUtil.e(TAG,"可用相机的个数是:"+mCameraManager.getCameraIdList().length);
            for (String cameraId : mCameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                LogUtil.e(TAG,"可用相机:"+characteristics.get(CameraCharacteristics.LENS_FACING));
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                mCameraId = cameraId;
                LogUtil.e(TAG,"cameraID:"+mCameraId);
                return;
            }
        }catch (CameraAccessException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sizes 是相机返回的支持的分辨率，从我们传递的参数找找到一个最接近的分辨率
     * @param sizes
     * @param width
     * @param height
     * @return
     */
    private Size getPreferredPreviewSize(Size[] sizes, int width, int height) {
        List<Size> collectorSizes = new ArrayList<>();
        for (Size option : sizes) {
            if (width > height) {
                if (option.getWidth() >= width && option.getHeight() >= height) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getHeight() >= width && option.getWidth() >= height) {
                    collectorSizes.add(option);
                }
            }
        }
        if (collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Size>() {
                @Override
                public int compare(Size s1, Size s2) {
                    return Long.signum(s1.getWidth() * s1.getHeight() - s2.getWidth() * s2.getHeight());
                }
            });
        }
        return sizes[0];
    }

    final int REQUEST_CAMERA_PERMISSION = 110;

    /**
     * 初始化相机
     */
    private void openCamera() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            LogUtil.e(TAG,"openCamera no permission");
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            },REQUEST_CAMERA_PERMISSION);
        }else{
            try {
                mCameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, null);
                startBackgroundThread();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    public void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**解决画面拉伸的问题
     * 就是要为预览界面设置一个合适比例的 SurfaceTexture buffer size
     *这里根据当前设备及传感器的旋转角度来判断是否交换宽高值，然后通过
     * CameraCharacteristics 来得到最适合当前大小比例的宽高，然后把这个宽高设置给 SurfaceTexture
     */
    private void createCameraPreview() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            int deviceOrientation =getWindowManager().getDefaultDisplay().getOrientation();
            //纠正角度
            int totalRotation = sensorToDeviceRotation(characteristics, deviceOrientation);
            boolean swapRotation = totalRotation == 90 || totalRotation == 270;
            int rotatedWidth = mWidth;
            int rotatedHeight = mHeight;
            if (swapRotation) {
                rotatedWidth = mHeight;
                rotatedHeight = mWidth;
            }
            Size[] sizes = map.getOutputSizes(SurfaceTexture.class);
            mPreviewSize = getPreferredPreviewSize(sizes, rotatedWidth, rotatedHeight);
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            LogUtil.e(TAG, "OptimalSize width: " + mPreviewSize.getWidth() + " height: " + mPreviewSize.getHeight());
            Surface surface = new Surface(texture);
            mBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice) {
                        return;
                    }
                    mSession = cameraCaptureSession;
                    mBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    try {
                        mSession.setRepeatingRequest(mBuilder.build(), mSessionCaptureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    ToastUtils.showMsgToast(Camera2Activity.this,"Camera configuration change");
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止拍照释放资源*/
    private void stopCamera(){
        if(mCameraDevice!=null){
            mCameraDevice.close();
            mCameraDevice=null;
        }
    }

    private static int sensorToDeviceRotation(CameraCharacteristics characteristics, int deviceOrientation) {
        int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    boolean setBimapForFile(){
        String fileName = getIntent().getStringExtra("fileName");
        if(mBitmap !=null){
            String fileUrl =FileUtils.getIntent().getDiskCacheDir(this);
            File file=new File(fileUrl,fileName);//将要保存图片的路径
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                bos.flush();
                bos.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        LogUtil.e(TAG,String.format("cropBitmap width:%d,height:%d",w,h));
        if(w>h){
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            // 围绕原地进行旋转
            Bitmap newBM = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
            bitmap = newBM;
            newBM.recycle();
        }
        w = bitmap.getWidth(); // 得到图片的宽度
        Bitmap current = Bitmap.createBitmap(bitmap, 0, 0, w, w, null, false);
        if(w>mImageSize){//缩放
            float size = mImageSize;
            float w_dip = w/size;
            Matrix matrix = new Matrix();
            matrix.preScale(w_dip, w_dip);
            Bitmap newBM = Bitmap.createBitmap(current, 0, 0, mImageSize, mImageSize, matrix, false);
            return newBM;
        }else {//w<=mImageSize
            mImageSize = w;
            return current;
        }
    }
}
