package com.guoxd.workframe.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by guoxd on 2018/10/23.
 */

public class FileUtils {
    static FileUtils utils=null;
    private FileUtils(){
    }

    /**入口
     * @return
     */
    public static FileUtils getIntent(){
        if(utils == null){
            utils = new FileUtils();
        }
        return utils;
    }

    /**暂存：缓存路径获取
     * @param context
     * @return
     */
    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**长存：文件路径获取
     * @param context
     * @return
     */
    public String getDiskFileDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(context.getExternalFilesDir(""))
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir("").getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

    /**判断文件目录是否存在
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }



}
