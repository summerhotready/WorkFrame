package com.guoxd.workframe.system;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.LogUtil;

/**
 * Created by guoxd on 2018/5/8.
 */

public class TextWidgeFragment extends BaseFragment {

    final String TAG="system.TestWidgeFragment";

    @Override
    protected int getCurrentLayoutID() {
        return R.layout.system_fragment_widge;
    }

    @Override
    protected void initView(View root) {
        //TextView
        AppCompatTextView textView = root.findViewById(R.id.tv_textView);
        textView.setText("可滚动内容的textview\n该滚动和ScrollerView是冲突的\n使用时需要注意\nhhhhhhhhhhhhhhhh\nggggggg");
        //edittext筛选
        EditText edit = root.findViewById(R.id.edit_2);
       edit.addTextChangedListener(new SearchWather(edit));

        //Android加密算法中需要随机数时要使用SecureRandom来获取随机数
//        SecureRandom sr = new SecureRandom();
//        byte[] output = new byte[16];
//        sr.nextBytes(output);
    }
    class SearchWather implements TextWatcher {
        //监听改变的文本框
        private EditText editText;
        /** 构造函数 */
        public SearchWather(EditText editText){
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String editable = editText.getText().toString();
            if(! TextUtils.isEmpty(editable)) {
                String str = isFirst(editable.toString());
                LogUtil.d("SearchWather", "afterTextChanged:" + str);
                if (!str.equals(editable)) {//前后不一致更新
                    editText.setText(str);
                    if (!TextUtils.isEmpty(str)) {
                        editText.setSelection(str.length());
                    }
                    LogUtil.d("SearchWather", "update:" + str);
                }
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        }
        public String isFirst(String str){//筛选A-Z开头
            char[] array = str.toCharArray();
            if(array.length == 1){
                if(!(Integer.valueOf(array[0]) >=65 && Integer.valueOf(array[0]) <=90)){//
                    str="";
                }
            }else if(array.length>1){
                int i=0,size=array.length;
                for(;i<size;i++){//查找第一个A-Z开头的
                    if(Integer.valueOf(array[i]) >=65 && Integer.valueOf(array[i]) <=90){
                        break;
                    }
                }
                if(i<size){
                    str=str.substring(i,size);
                }else{
                    str="";//
                }
            }
            return str;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
