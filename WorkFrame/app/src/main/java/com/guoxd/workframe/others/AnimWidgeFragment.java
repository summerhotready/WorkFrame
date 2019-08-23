package com.guoxd.workframe.others;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

/**第三方动画库（反射调用）
 * Created by guoxd on 2018/10/17.
 */

public class AnimWidgeFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.other_fragment_anim, container, false);
        return root;
    }

    @Override
    public void onRefresh() {

    }
}
