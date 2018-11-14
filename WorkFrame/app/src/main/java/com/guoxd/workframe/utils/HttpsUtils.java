package com.guoxd.workframe.utils;

import android.content.Context;
import android.os.Handler;

import com.guoxd.workframe.utils.http_info.FileDownloadListener;
import com.guoxd.workframe.utils.http_info.HttpCallListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
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
                .hostnameVerifier(new HostnameVerifier() {//设置tsl的主域名验证
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(getSocketFactory(mContext))
                .build();
    }
    private static SSLSocketFactory getSocketFactory(Context context) {
        InputStream mTrust_input = null;
        InputStream mClient_input = null;
        try {
            //服务器授信证书
            mTrust_input = context.getAssets().open("ca.bks");
            //客户端证书
            mClient_input = context.getAssets().open("outgoing.CertwithKey.pkcs12");


            // 1 Import the CA certificate of the server
            KeyStore trustStore = KeyStore.getInstance("BKS");
            trustStore.load(mTrust_input, "Huawei@123".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);


            // 2 Import your own certificate
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(mClient_input, "IoM@1234".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "IoM@1234".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return factory;
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {//关闭
            try{
                if(mTrust_input !=null) {
                    mTrust_input.close();
                }
                if(mClient_input !=null) {
                    mClient_input.close();
                }
            }catch (IOException e){

            }

        }
        return null;
    }


    /**
     * 给http做判断
     * @param url
     * @param callback
     */
    String checkHttp(String url, final HttpCallListener callback){
        if(url.equals("")){
            return "无效的url";
        }
        if(!NetUtils.isNetConnect(mContext)){
            return "当前网络不可用，请检查！";
        }
        return null;
    }

    /**get请求
     * success:200;failure:204
     * @param url
     * @param callback
     */
    public void getRequest(String url, final HttpCallListener callback){
        String check = checkHttp(url, callback);
        if(check !=null){
            sendCallback(false,callback,check);
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
                sendCallback(false,callback,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 200) {
                    sendCallback(true,callback,tempResponse);
                }else{
                    sendCallback(false,callback,"code error");
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
        String check = checkHttp(url, mListener);
        if(check !=null){
            sendCallback(false,mListener,check);
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
                sendCallback(false,mListener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 201) {
                    sendCallback(true,mListener,tempResponse);
                }else{
                    sendCallback(false,mListener,"code error");
                }
            }
        });
    }

    /**put请求  整体修改
     * success:204;failure:404
     * @param url
     * @param jsonStr
     * @param callback
     */
    public void putRequest(String url, String jsonStr, final HttpCallListener callback){
        String check = checkHttp(url, callback);
        if(check !=null){
            sendCallback(false,callback,check);
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
                sendCallback(false,callback,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 204) {
                    sendCallback(true,callback,tempResponse);
                }else{
                    sendCallback(false,callback,"code error");
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
        String check = checkHttp(url, mListener);
        if(check !=null){
            sendCallback(false,mListener,check);
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
                sendCallback(false,mListener,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 204) {
                    sendCallback(true,mListener,tempResponse);
                }else{
                    sendCallback(false,mListener,"code error");
                }
            }
        });
    }

    /**del请求
     * success:204;failure:404
     * @param url
     * @param callback
     */
    public void delRequest(String url, final HttpCallListener callback){
        String check = checkHttp(url, callback);
        if(check !=null){
            sendCallback(false,callback,check);
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
                sendCallback(false,callback,e.getMessage() == null? "null":e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 204) {
                    sendCallback(true,callback,tempResponse);
                }else{
                    sendCallback(false,callback,"code error");
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
        String check = checkHttp(url, mListener);
        if(check !=null){
            sendCallback(false,mListener,check);
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
                sendCallback(false,mListener,e.getMessage() == null? "null":e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse =  response.body().string();
                LogUtil.d(TAG, "on success:"+tempResponse);
                if(response.code() == 201) {
                    sendCallback(true,mListener,tempResponse);
                }else{
                    sendCallback(false,mListener,"code error");
                }
            }
        });
    }


    /*** 成功失败处理
     * 简化了mainHandler.post操作
     * @param flag
     * @param callback
     * @param data
     */
    void sendCallback(final boolean flag, final HttpCallListener callback, final String data){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(flag){
                    callback.Success(data);
                }else {
                    callback.Failure(data);
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
