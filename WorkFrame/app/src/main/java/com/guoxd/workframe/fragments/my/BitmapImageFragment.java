package com.guoxd.workframe.fragments.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoxd.work_frame_library.views.bigImage.BrowsePictureAdapter;
import com.guoxd.work_frame_library.views.bigImage.UrlTouchImageView;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoxd on 2018/10/16.
 */

public class BitmapImageFragment extends BaseFragment implements OnPageChangeListener{

    private ViewPager mVp;
    private Bundle bundle;
    private BrowsePictureAdapter adapter;
    private Button closeBtn;
    private List<UrlTouchImageView> viewList;
    private ArrayList<String> urls;
//    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private TextView picNumTv;
    private String baseurl;
//    private List<UrlTouchImageView> viewList = new ArrayList<UrlTouchImageView>();


    @Override
    public void onRefresh() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_pic, container, false);
        mVp = (ViewPager) root.findViewById(R.id.vp);
        closeBtn = (Button) root.findViewById(R.id.close_btn);
        picNumTv = (TextView) root.findViewById(R.id.picnum_tv);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                finish();
            }
        });
        viewList = new ArrayList<UrlTouchImageView>();
        /*Intent intent = getActivity().getIntent();
        if (intent != null) {
            bundle = intent.getBundleExtra("msg");
            urls = bundle.getStringArrayList("Urls");
            baseurl = bundle.getString("baseurl");
        }*/
        for (int i = 0; i < 5; i++) {
            //如果不需要支持缩放直接用Imageview
            // ImageView iv = new ImageView(this);

            //支持缩放
            final UrlTouchImageView iv = new UrlTouchImageView(getActivity());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.getImageView().setImageResource(R.mipmap.ic_launcher);

//            iv.setUrl(baseurl+urls.get(i));
            viewList.add(iv);
        }
        adapter = new BrowsePictureAdapter(viewList);
        // getInternetData();

        mVp.setAdapter(adapter);
//        int index = bundle.getInt("index", 1);
        mVp.setCurrentItem(0);
        picNumTv.setText("");
        mVp.setOnPageChangeListener(this);
        return root;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        Log.i("onPageScrollStateChange","arg0:"+arg0);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        Log.i("onPageScrolled","arg0:"+arg0);
    }

    @Override
    public void onPageSelected(int arg0) {
        Log.i("onPageSelected","arg0:"+arg0);
        //显示底部图片文字
        picNumTv.setText("arg0:"+arg0);
    }

}
