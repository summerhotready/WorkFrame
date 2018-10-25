package com.guoxd.workframe.utils.http_info;

/**
 * Created by guoxd on 2018/9/12.
 */

public interface HttpCallListener {
    void Success(String data);
    void Failure(String message);
}
