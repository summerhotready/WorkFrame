package com.guoxd.workframe.utils.http_info;

import java.io.File;

/**
 * Created by guoxd on 2018/10/22.
 * 下载文件
 */

public interface FileDownloadListener {
    /**
     * 下载成功
     */
    void onDownloadSuccess(File file);

    /**
     * @param progress
     * 下载进度
     */
    void onDownloading(int progress);

    /**
     * 下载失败
     */
    void onDownloadFailed();

}
