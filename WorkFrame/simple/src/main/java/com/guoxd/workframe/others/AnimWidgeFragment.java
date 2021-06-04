package com.guoxd.workframe.others;

import android.graphics.drawable.Animatable;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**第三方动画库（反射调用）
 * Created by guoxd on 2018/10/17.
 */

public class AnimWidgeFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getCurrentLayoutID() {
        return R.layout.other_fragment_anim;
    }

    @Override
    protected void initView(View root) {
        initAnim(root);
    }

//    ImageView svg_image_1,svg_image_2,svg_image_3,svg_image_4,svg_image_5,svg_image_6,svg_image_7,svg_image_8;
    Animatable[] animatables;
    boolean[] animatableStatus;
    ImageView[] imageViews;

    private void initAnim(View root) {
        getBaseActity().setPageTitle("动画测试");
        animatables = new Animatable[8];
        imageViews = new ImageView[8];
        animatableStatus = new boolean[]{true,true,true,true,true,true,true,true};
        imageViews[0] = root.findViewById(R.id.anim_image_svg1);
        imageViews[1] = root.findViewById(R.id.anim_image_svg2);
        imageViews[2] = root.findViewById(R.id.anim_image_svg3);
        imageViews[3] = root.findViewById(R.id.anim_image_svg4);
        imageViews[4] = root.findViewById(R.id.anim_image_svg5);
        imageViews[5] = root.findViewById(R.id.anim_image_svg6);
        imageViews[6] = root.findViewById(R.id.anim_image_svg7);
        imageViews[7] = root.findViewById(R.id.anim_image_svg8);

        imageViews[0].setTag(0);
        imageViews[1].setTag(1);
        imageViews[2].setTag(2);
        imageViews[3].setTag(3);
        imageViews[4].setTag(4);
        imageViews[5].setTag(5);
        imageViews[6].setTag(6);
        imageViews[7].setTag(7);

        imageViews[0].setOnClickListener(this);
        imageViews[1].setOnClickListener(this);
        imageViews[2].setOnClickListener(this);
        imageViews[3].setOnClickListener(this);
        imageViews[4].setOnClickListener(this);
        imageViews[5].setOnClickListener(this);
        imageViews[6].setOnClickListener(this);
        imageViews[7].setOnClickListener(this);


    }
    void showAnimtor(int position){
        try{
            if(position == 0){
                imageViews[position].setImageResource(animatableStatus[position]?
                        R.drawable.svg_anim_ic_line_change:R.drawable.svg_anim_ic_line_change2);
            }
            Animatable animatable = (Animatable) imageViews[position].getDrawable();
            animatables[position] = animatable;
            animatableStatus[position] = !animatableStatus[position];
            animatables[position].start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(Animatable animatable: animatables){
            if(animatable !=null && animatable.isRunning()){
                animatable.stop();
                animatable = null;
            }
        }
    }

    public Pair<List<String>,List<String>> thisFun(){

        return new Pair(new ArrayList<String>(),new ArrayList<String>());
    }
    public Pair<Integer,Integer> integerFun(){
        return new Pair<>(1,2);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.anim_image_svg1:
            case R.id.anim_image_svg2:
            case R.id.anim_image_svg3:
            case R.id.anim_image_svg4:
            case R.id.anim_image_svg5:
            case R.id.anim_image_svg6:
            case R.id.anim_image_svg7:
            case R.id.anim_image_svg8:
                try {
                    int position = Integer.valueOf(v.getTag().toString());
                    showAnimtor(position);
                }catch (Exception e){
                    e.printStackTrace();
                }
//                animatableStatus[]

                break;

        }
    }

}
