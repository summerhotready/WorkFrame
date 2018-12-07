package com.guoxd.workframe.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.guoxd.workframe.utils.http_info.FileDownloadListener;
import com.guoxd.workframe.utils.http_info.HttpCallListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by guoxd on 2018/8/14.
 * 带联网判断的okhttp3封装工具
 * 符合rest模式
 */

public class HttpsUtils {
    final String TAG="HttpsUtils";
    // 将请求成功的数据返回到主线程进行数据更新
    Handler mainHandler;
    static Context mContext;

    //
    static HttpsUtils utils = null;
    OkHttpClient mHttpClient;
    final MediaType jsonMediaType= MediaType.parse("application/json;charset=utf-8");


    /**初始化&获取
     * 必须操作，不初始化会报错
     * 第一次由MainApplication调用，保证了获取的getMainLooper
     * @param context
     */
    public static HttpsUtils getIntent(Context context){
        if(utils == null){
            utils = new HttpsUtils(context);
        }
        return utils;
    }

    private HttpsUtils(Context context){
        mContext = context;
        mainHandler = new Handler(mContext.getMainLooper());

        mHttpClient = new OkHttpClient.Builder()
                .readTimeout(60*1000L, TimeUnit.MILLISECONDS)
                .connectTimeout(60*1000L, TimeUnit.MILLISECONDS)
                .build();
    }

    /**给http做检查，通过为true，未通过为false
     * @param url
     * @param callback
     * @return true:ok false:can't
     */
    boolean isRequeseHttp(String url, final HttpCallListener callback){
        if(TextUtils.isEmpty(url)){
            sendCallback(false,Constant.HTTP_NO_URL,callback,"Invalid URL");
            return false;
        }
        if(!NetUtils.isNetConnect(mContext)){
            sendCallback(false,-Constant.HTTP_NO_NETWORK,callback,"No Network");
            return false;
        }
        return true;
    }


    /**get请求
     * success:200;failure:204
     * @param url
     * @param mListener
     */
    public void getRequest(String url, final HttpCallListener mListener){
        if(!isRequeseHttp(url,mListener)){
            return;
        }
        Request request = new Request.Builder()
                .header("Content-Type","application/json")
                .url(url)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, "on failure:"+e.getMessage() == null? "null":e.getMessage());
                sendCallback(false,Constant.HTTP_FAILURE,mListener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 200) {
                    sendCallback(true,response.code(),mListener,tempResponse);
                }else{
                    sendCallback(false,response.code(),mListener,"code error");
                }
            }
        });
    }
    /**post请求 新增
     * success:201;failure:404
     * @param url
     * @param jsonStr
     * @param mListener
     */
    public void postRequest(String url, String jsonStr, final HttpCallListener mListener){
        if(!isRequeseHttp(url,mListener)){
            return;
        }
        RequestBody requestBody=RequestBody.create(jsonMediaType,jsonStr);
        final Request request = new Request.Builder()
                .post(requestBody)
                .header("Content-Type","application/json")
                .url(url)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, "on failure:"+e.getMessage() == null? "null":e.getMessage());
                sendCallback(false,Constant.HTTP_FAILURE,mListener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 201) {
                    sendCallback(true,response.code(),mListener,tempResponse);
                }else{
                    sendCallback(false,response.code(),mListener,"code error");
                }
            }
        });
    }

    /**put请求  整体修改
     * success:204;failure:404
     * @param url
     * @param jsonStr
     * @param mListener
     */
    public void putRequest(String url, String jsonStr, final HttpCallListener mListener){
        if(!isRequeseHttp(url,mListener)){
            return;
        }
        RequestBody requestBody= RequestBody.create(jsonMediaType,jsonStr);
        Request request = new Request.Builder()
                .put(requestBody)
                .url(url)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, "on failure:"+e.getMessage() == null? "null":e.getMessage());
                sendCallback(false,Constant.HTTP_FAILURE,mListener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 204) {
                    sendCallback(true,response.code(),mListener,tempResponse);
                }else{
                    sendCallback(false,response.code(),mListener,"code error");
                }
            }
        });
    }

    /**patch 部分修改
     * success:204;failure:404
     * @param url
     * @param jsonStr
     * @param mListener
     */
    public void patchRequest(String url, String jsonStr, final HttpCallListener mListener){
        if(!isRequeseHttp(url,mListener)){
            return;
        }
        RequestBody requestBody=RequestBody.create(jsonMediaType,jsonStr);
        final Request request = new Request.Builder()
                .patch(requestBody)
                .header("Content-Type","application/json")
                .url(url)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, "on failure:"+e.getMessage() == null? "null":e.getMessage());
                sendCallback(false,Constant.HTTP_FAILURE,mListener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 204) {
                    sendCallback(true,response.code(),mListener,tempResponse);
                }else{
                    sendCallback(false,response.code(),mListener,"code error");
                }
            }
        });
    }

    /**del请求
     * success:204;failure:404
     * @param url
     * @param mListener
     */
    public void delRequest(String url, final HttpCallListener mListener){
        if(!isRequeseHttp(url,mListener)){
            return;
        }
        Request request = new Request.Builder()
                .delete()
                .url(url)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, "on failure:"+e.getMessage() == null? "null":e.getMessage());
                sendCallback(false,Constant.HTTP_FAILURE,mListener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 204) {
                    sendCallback(true,response.code(),mListener,tempResponse);
                }else{
                    sendCallback(false,response.code(),mListener,"code error");
                }
            }
        });
    }


    /**port form请求
     * success:201;failure:204
     * @param url
     * @param jsonStr
     * @param mListener
     */
    public void postFormRequest(String url, String jsonStr, final HttpCallListener mListener){
        if(!isRequeseHttp(url,mListener)){
            return;
        }
        RequestBody requestBody=RequestBody.create(jsonMediaType,jsonStr);
        final Request request = new Request.Builder()
                .post(requestBody)
                .header("Content-Type","application/json")
                .url(url).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, "on failure:"+e.getMessage() == null? "null":e.getMessage());
                sendCallback(false,Constant.HTTP_FAILURE,mListener,e.getMessage() == null? "null":e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 201) {
                    sendCallback(true,response.code(),mListener,tempResponse);
                }else{
                    sendCallback(false,response.code(),mListener,"code error");
                }
            }
        });
    }


    /*** 成功失败处理
     * 简化了mainHandler.post操作
     * @param flag
     * @param code
     * @param listener
     * @param data
     */
    void sendCallback(final boolean flag,final int code ,final HttpCallListener listener, final String data){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(flag){
                    listener.Success(code,data);
                }else {
                    listener.Failure(code,data);
                }
            }
        });
    }



    //文件操作
    /**文件下载
     * @param url 下载连接
     * @param fileName 下载文件的名字
     * @param listener 下载监听
     */
    public void fileDownload(final String url, final String fileName, final FileDownloadListener listener) {

        Request request = new Request.Builder().url(url).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDownloadFailed();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    final File file = getFile(fileName);// new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        final int progress = (int) (sum * 1.0f / total * 100);
                        LogUtil.d("Update","progress:"+progress);
                        // 下载中
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onDownloading(progress);
                            }
                        });
                    }
                    fos.flush();
                    // 下载完成
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadSuccess(file);
                        }
                    });

                } catch (Exception e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadFailed();
                        }
                    });
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     *
     * @param url
     * @param fileName
     * @param listener
     */
    public void fileUpload(String url, String fileName, final HttpCallListener listener){
        if(!isRequeseHttp(url,listener)){//未通过检查
            return;
        }
        File file = getFile(fileName);// new File(savePath, getNameFromUrl(url));
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //可以根据自己的接口需求在这里添加上传的参数
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder().url(url)//上传接口
                .post(requestBody)
                .header("Content-Type","application/x-www-form-urlencoded")
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,IOException e) {
                LogUtil.d(TAG, "fileUpload onFailure message:"+e.getMessage() == null? "null":e.getMessage());
                sendCallback(false,Constant.HTTP_FAILURE,listener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call,final Response response) throws IOException {
                String tempResponse = response.body().string();
                LogUtil.d(TAG, "on success:" + tempResponse);
                sendCallback(true,response.code(),listener,tempResponse);
            }
        });
    }

    /**
     * 获取文件
     * @param fileName
     * @return
     */
    private File getFile(String fileName){
        String fileUrl =FileUtils.getIntent().getDiskCacheDir(mContext);
        LogUtil.d("update file url:",fileUrl);
        File downloadFile = new File(fileUrl, fileName);
        return downloadFile;
    }

}
