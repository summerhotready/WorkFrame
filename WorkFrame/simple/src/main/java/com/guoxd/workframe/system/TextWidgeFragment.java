package com.guoxd.workframe.system;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.snackbar.Snackbar;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ToastUtils;
import com.guoxd.workframe.utils.ViewHelpUtils;
import com.rey.material.widget.SnackBar;

import java.util.ArrayList;

/**
 * Created by guoxd on 2018/5/8.
 */

public class TextWidgeFragment extends BaseFragment {

    AppCompatTextView widthText;
    View bgview;
    final String TAG="system.TestWidgeFragment";

    @Override
    protected int getCurrentLayoutID() {
        return R.layout.system_fragment_widge;
    }

    @Override
    protected void initView(View root) {
        setPageTitle("Text类组件");
        //TextView
        AppCompatTextView textView = root.findViewById(R.id.tv_textView);
//        textView.setText("可滚动内容的textview\n该滚动和ScrollerView是冲突的\n使用时需要注意\nhhhhhhhhhhhhhhhh\nggggggg");
        //edittext筛选
        EditText edit = root.findViewById(R.id.edit_2);
        edit.addTextChangedListener(new SearchWather(edit));
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO){//需要手动关闭软键盘

                    if(TextUtils.isEmpty(edit.getText().toString())){
                        return true;
                    }else {
                        ToastUtils.showMsgToast(getActivity(),"go");
                        return true;
                    }
                }
                return false;
            }
        });

       widthText =  root.findViewById(R.id.width_text);
        widthText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getTextViewSize();
           }
       });
        //Android加密算法中需要随机数时要使用SecureRandom来获取随机数
//        SecureRandom sr = new SecureRandom();
//        byte[] output = new byte[16];
//        sr.nextBytes(output);
//        snakeBar test
//        View snackbarView = Lay
        root.findViewById(R.id.btn_snake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnakeBar();
            }
        });
        snackbar =Snackbar.make(getActivity().getWindow().getDecorView(),snakeBarText , 3000)
                .setAction("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtil.i(TAG,String.format("SnakeBar btn click %d",(snakebar_click++)));
                        showSnakeBar();
                    }
                });
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
                if(snakeList.size()>0){
                    snakeList.remove(0);
                }
                LogUtil.i(TAG,String.format("SnakeBar onShown click %d",(snakebar_click)));
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                LogUtil.i(TAG,String.format("SnakeBar onDissmiss click %d event:%d",(snakebar_click),event));
                if(snakeList.size()>0){
                    showSnakeView();
                }
            }
        });
//        Snackbar.make(coordinatorLayout, "我是普通的Snackbar", Snackbar.LENGTH_LONG).show();
//setting
        root.findViewById(R.id.btn_set_to_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
//                this flag is
//                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                mIntent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                startActivity(mIntent);
            }
        });
    }

    Snackbar snackbar;
    int snakebar_click = 0;
    ArrayList<Integer> snakeList = new ArrayList<>();
    String snakeBarText = "过年了，过年了，快回家";
    private void showSnakeBar() {
        snakeList.add(snakebar_click);
        LogUtil.i(TAG,String.format("SnakeBar showSnakeBar %b count:%d",snackbar.isShown(),(++snakebar_click)));
        if(snackbar.isShown()){
            snackbar.dismiss();
//            snackbar.setText(String.format("%s %d",snakeBarText,snakebar_click));
//            snackbar.show();
        }else{
            showSnakeView();
        }
    }

    private void showSnakeView() {
        if(snakeList.size()>0) {
            snackbar.setText(String.format("%s %d", snakeBarText, snakeList.get(0)));
            snackbar.show();
        }
    }

    private void getTextViewSize() {
//        LogUtil.i(TAG,String.format("getTextViewSize width:%d height:%d padding:%f",
//                widthText.getWidth(),widthText.getHeight(),2*getResources().getDimension(R.dimen.padding_10)));
//        LogUtil.i(TAG,"getTextViewSize Layout.getDesiredWidth:"+ViewHelpUtils.getTextViewTextWidth(getActivity(),widthText.findViewById(R.id.width_text)));

        Paint paint = widthText.getPaint();
        String text = widthText.getText().toString();
        LogUtil.i(TAG,"getTextViewSize measureText:"+paint.measureText(text));
        Rect rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);
        LogUtil.i(TAG,String.format("getTextViewSize getTextBounds width:%d height:%d",(rect.right-rect.left),(rect.bottom-rect.top)));
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        LogUtil.i(TAG,"getTextViewSize height fontMetrics:"+(fontMetrics.bottom-fontMetrics.top));
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
