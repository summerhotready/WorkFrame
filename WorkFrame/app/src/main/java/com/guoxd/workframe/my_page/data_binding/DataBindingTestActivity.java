package com.guoxd.workframe.my_page.data_binding;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseActivity;
import com.guoxd.workframe.databinding.ActivityDataBindingBinding;
import com.guoxd.workframe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBindingTestActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_binding;
    }


    ActivityDataBindingBinding mDataBinding;
    @Override
    public void initView() {
        super.initView();
        mDataBinding = DataBindingUtil.setContentView(this,getLayoutId());

        PersonData personData = new PersonData("韩梅梅","18","排球");
        mDataBinding.setPerson(personData);

        HashMap<String,String> maps = new HashMap<>();
        maps.put("email","emma_guo@163.com");
        mDataBinding.setMaps(maps);
        List<String> lists = new ArrayList<>();
        lists.add("ArrayList01");
        lists.add("ArrayList02");
        lists.add("ArrayList03");
        mDataBinding.setLists(lists);
        String[] strings = new String[]{"Array01","Array02"};
        mDataBinding.setArrays(strings);

        mDataBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMsgToast(DataBindingTestActivity.this,"Button");
            }
        });
    }
}
