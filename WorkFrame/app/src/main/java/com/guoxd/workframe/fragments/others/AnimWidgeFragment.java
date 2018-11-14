package com.guoxd.workframe.fragments.others;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

/**
 * Created by guoxd on 2018/10/17.
 */

public class AnimWidgeFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.system_fragment_anim, container, false);

        return root;
    }

    @Override
    public void onRefresh() {

    }
}
