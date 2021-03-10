package com.guoxd.workframe;


import android.content.Intent;

import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.utils.LogUtil;

/**
 * Created by guoxd on 2019/1/9.
 */

public class SendMessageActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
//        LogUtil.d(TAG,"SendMessageActivity getLayoutId()");
        return R.layout.activity_send;
    }

    @Override
    public void initView() {
        super.initView();
        LogUtil.d(TAG,"SendMessageActivity initView()");
       /* Intent intent = getAppSettingIntent("");
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                LogUtil.d(TAG,"ACTION_SEND ");
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
//                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
//                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }*/
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }
}
