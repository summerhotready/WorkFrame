package com.guoxd.workframe.fragments.my;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.guoxd.work_frame_library.menus.arcmenu.ArcMenu;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.PointMenuViewUtils;
import com.guoxd.workframe.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by guoxd on 2018/10/25.
 */

public class MenuWidgeFragment extends BaseFragment {
    @Override
    public void onRefresh() {

    }

    PointMenuViewUtils utils;
    PointMenuViewUtils utilsX;
    ArcMenu mArc;
    final String TAG="MenuWidgeFragment" ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.my_fragment_menus,container,false);

        initTranY(root);
        initTranX(root);

        mArc = (ArcMenu) root. findViewById(R.id.arcMenu);
        mArc.setOnSubItemClickListener(new ArcMenu.onSubItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtil.d(TAG, "position" + position);
            }
        });
        return root;
    }

    void initTranY(View root){
        View parentView = root.findViewById(R.id.rl_constan_y);
        ImageView base_menu = (ImageView)root.findViewById(R.id.iv_menu);
        ImageView iv_1 = (ImageView)root.findViewById(R.id.iv_1);
        ImageView iv_2 = (ImageView)root.findViewById(R.id.iv_2);
        ImageView iv_3 = (ImageView)root.findViewById(R.id.iv_3);

        utils = new PointMenuViewUtils();
        utils.setImageViews(180,parentView,iv_1,iv_2,iv_3);
        base_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(utils.isOpen){
                    if(!utils.isMoveing) {
                        utils.closeMenu();
                    }else{
                        ToastUtils.showMsgToast(getActivity(),"can't open");
                    }
                }else{
                    if(!utils.isMoveing) {
                        utils.showMenu();
                    }else{
                        ToastUtils.showMsgToast(getActivity(),"can't close");
                    }
                }
            }
        });
        iv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMsgToast(getActivity(),"view1");
            }
        });
        iv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMsgToast(getActivity(),"view2");
            }
        });

        iv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMsgToast(getActivity(),"view3");
            }
        });
    }
    void initTranX(final View root){
        View parentView = root.findViewById(R.id.rl_constan_x);
        ImageView base_menu = (ImageView)root.findViewById(R.id.iv_x_menu);
        ImageView iv_1 = (ImageView)root.findViewById(R.id.iv_x_1);
        ImageView iv_2 = (ImageView)root.findViewById(R.id.iv_x_2);
        utilsX = new PointMenuViewUtils();
        utilsX.setOrientation(LinearLayout.HORIZONTAL);
        utilsX.setImageViews(120,parentView,iv_1,iv_2);

        base_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(utilsX.isOpen){
                    if(!utilsX.isMoveing) {
                        utilsX.closeMenu();
                    }else{
                        ToastUtils.showMsgToast(getActivity(),"can't open");
                    }
                }else{
                    if(!utilsX.isMoveing) {
                        utilsX.showMenu();
                    }else{
                        ToastUtils.showMsgToast(getActivity(),"can't close");
                    }
                }
            }
        });

        /*root.findViewById(R.id.iv_x_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator mObjectAnimator= ObjectAnimator.ofFloat(root.findViewById(R.id.iv_x_1), "translationX", 0,  120);
                mObjectAnimator.setDuration(2500);
                mObjectAnimator.start();
            }
        });*/
    }


}
