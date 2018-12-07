package com.guoxd.workframe.fragments.my;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Android SDK 21（LOLLIPOP） 开始已经弃用了之前的 Camera 类，提供了 camera2 相关 API
 */
public class CameraViewFragment extends BaseFragment implements View.OnClickListener {
    final String TAG="CameraViewFragment";
    private Button btnTakePic;
    private Button btnCancle;
    private TextureView mTextureView;

    boolean isCamera = true;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private String mCameraId;
    private Size mPreviewSize;
    private int mWidth ;//获取相机宽度
    private int mHeight ;//获取相机高度
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
    public void onRefresh() {

    }


    View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_wigth_camera, container, false);
        //按钮
        btnTakePic = (Button)findViewById(R.id.bnt_takepicture);
        btnCancle = (Button)findViewById(R.id.bnt_enter);
        btnCancle.setVisibility(View.GONE);

        btnTakePic.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        mCameraManager = (CameraManager) getActivity().getSystemService(getActivity().CAMERA_SERVICE);
        //照相机预览的空间
        mTextureView = (TextureView) findViewById(R.id.surfaceView);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override //解决画面拉伸的问题
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mWidth = width;
                mHeight = height;
                getCameraId();
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        return rootView;
    }

    View findViewById(int id){
        return rootView.findViewById(id);
    }

    private void getCameraId() {
        try {
            for (String cameraId : mCameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                mCameraId = cameraId;
                LogUtil.d(TAG,"cameraID:"+mCameraId);
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
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getHeight() > width && option.getWidth() > height) {
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
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            LogUtil.d(TAG,"openCamera no permission");
           this.requestPermissions(new String[]{
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
            int deviceOrientation = getActivity(). getWindowManager().getDefaultDisplay().getOrientation();
            int totalRotation = sensorToDeviceRotation(characteristics, deviceOrientation);
            boolean swapRotation = totalRotation == 90 || totalRotation == 270;
            int rotatedWidth = mWidth;
            int rotatedHeight = mHeight;
            if (swapRotation) {
                rotatedWidth = mHeight;
                rotatedHeight = mWidth;
            }
            mPreviewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            LogUtil.e("CameraActivity", "OptimalSize width: " + mPreviewSize.getWidth() + " height: " + mPreviewSize.getHeight());
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
                    ToastUtils.showMsgToast(getActivity(), "Camera configuration change");
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private static int sensorToDeviceRotation(CameraCharacteristics characteristics, int deviceOrientation) {
        int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    Bitmap mBitmap;
    boolean setBimapForFile(){

        if(mBitmap !=null){
            File file = new File(getActivity().getCacheDir(),"save.jpg");
            mBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogUtil.e(TAG,"FileNotFoundException");
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e(TAG,"IOException");
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                        LogUtil.d(TAG,"save");
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 三个按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnt_takepicture:
                if(isCamera){//拍照
                    mBitmap= mTextureView.getBitmap();
                    btnTakePic.setText("确定");
                    btnCancle.setVisibility(View.VISIBLE);
                    isCamera = false;
                }else{//确认
                    if(setBimapForFile()){//save
                        LogUtil.d(TAG,"save ok");
                    }else{
                        LogUtil.e(TAG,"save error");
                    }
                }
                break;

            case R.id.bnt_enter:
                mBitmap = null;
                isCamera = true;
                btnTakePic.setText("拍照");
                btnCancle.setVisibility(View.GONE);
                    LogUtil.d(TAG,"bundle null");
                break;
        }
    }


}
