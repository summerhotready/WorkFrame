package com.guoxd.workframe.others;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.guoxd.workframe.R;
import com.guoxd.workframe.base.BaseFragment;
import com.guoxd.workframe.others.widge.TestBasePop;
import com.guoxd.workframe.utils.LogUtil;
import com.guoxd.workframe.utils.ViewHelpUtils;
import com.lxj.xpopup.XPopup;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.blur.PopupBlurOption;


/**第三方组件库（反射调用）
 * Created by guoxd on 2018/10/17.
 */

public class OtherWidgeFragment extends BaseFragment implements View.OnClickListener {


    @Override
    protected int getCurrentLayoutID() {
        return R.layout.other_fragment_widge;
    }

    TextView pop1,pop2,pop3,pop4;
    @Override
    protected void initView(View root) {
        getBaseActity().setPageTitle("其他第三方组件");
        initTab1(root);
        initTab2(root);
        pop1 = root.findViewById(R.id.pop_am);
        initPop1(pop1);
        pop2 = root.findViewById(R.id.pop_2);
        pop3 = root.findViewById(R.id.pop_3);
        pop4 = root.findViewById(R.id.pop_4);
        initListener();
    }

    private void initListener() {
        pop1.setOnClickListener(this);
        pop2.setOnClickListener(this);
        pop3.setOnClickListener(this);
        pop4.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
    }



    private void initTab1(View root) {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) root.findViewById(R.id.navigationTabStrip);
        navigationTabStrip.setTitles("Nav", "Tab", "Strip","Test");
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setTitleSize(15*getResources().getDisplayMetrics().density);
        navigationTabStrip.setStripColor(Color.RED);
        navigationTabStrip.setStripWeight(6);
        navigationTabStrip.setStripFactor(2);
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
        navigationTabStrip.setTypeface("fonts/typeface.ttf");
        navigationTabStrip.setCornersRadius(3);
        navigationTabStrip.setAnimationDuration(300);
        navigationTabStrip.setInactiveColor(Color.GRAY);
        navigationTabStrip.setActiveColor(Color.GRAY);
        //下面这两个注释，单独使用组件不打印
        navigationTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.i(TAG,"navigationTabStrip onPageScrolled :"+position);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.i(TAG,"navigationTabStrip onPageSelected :"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //下面这两个会输出
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                LogUtil.i(TAG,"navigationTabStrip onStartTabSelected :"+title);
            }

            @Override
            public void onEndTabSelected(String title, int index) {
                LogUtil.i(TAG,"navigationTabStrip onEndTabSelected :"+title);
            }
        });
    }

    FragmentContainerHelper fragmentContainerHelper;

    private void initTab2(View root){
        MagicIndicator magicIndicator = root.findViewById(R.id.magic_indicator);
        String[] mDataList = new String[]{"Tab1","Tab2","Tab3","Tab4"};
        int width = getResources().getDisplayMetrics().widthPixels/3- UIUtil.dip2px(getActivity(), 16.0);

        //tabLayout样式
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        //是否平铺，设置为true时会平均分配占满空间，默认为false几个tab紧挨着聚在一起
        commonNavigator.setAdjustMode(true);
        commonNavigator.setBackground(getResources().getDrawable(android.R.color.white));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                //设置单个tab的样式
                ColorTransitionPagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(getActivity());
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.YELLOW);
                simplePagerTitleView.setText(mDataList[index]);
                simplePagerTitleView.setTag(index);//用于获得当前位置
                //设置点击操作
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = view.getTag().toString()==null? 0:Integer.valueOf(view.getTag().toString());
//                        LogUtil.e(TAG,position);
                        fragmentContainerHelper.handlePageSelected(position);

                    }
                });
                return simplePagerTitleView;
            }
            //设置tab下面的指示器
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(getActivity());
                //设置指示器颜色
                linePagerIndicator.setColors(Color.YELLOW);
                //设置指示器宽度,设置模式为MODE_EXACTLY才会显示设置的宽度
                linePagerIndicator.setMode( LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineWidth( width);
                return linePagerIndicator;
            }
        });


        magicIndicator.setNavigator( commonNavigator);
        //设置tab之间的分割线
        LinearLayout titleContainer =   commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers( LinearLayout.SHOW_DIVIDER_MIDDLE);

        //设置指示器延迟到达的动画（弹簧效果）
        fragmentContainerHelper = new FragmentContainerHelper(magicIndicator);
        fragmentContainerHelper.setInterpolator(new OvershootInterpolator(2.0f));
        fragmentContainerHelper.setDuration(150);
    }

    PopupWindow mPop1;

    /**原生pop+小箭头绘制背景
     * backgroundColor得来的背景只能是純色的，且不能使用安卓原生的背景相关属性（如:background,backgroundResource)
     * radius	dimension	圆角大小
     * backgroundColor	color	背景颜色（注意不要是用安卓原生的背景相关的属性（如:background,backgroundResource等））
     * arrowDirection	enum	尖叫汽包相对位置(top,left,right,bottom)
     * relativePosition	fraction	尖叫汽包相对位置，百分比
     * sharpSize	dimension	尖叫汽包大小，为0表示不显示尖叫，默认是0
     * border	dimension	边框大小，默认是0
     * borderColor	color	边框颜色
     * startBgColor	color	渐变初始颜色(渐变时必选)
     * middleBgColor	color	渐变中间颜色(渐变时可选)
     * endBgColor	color	渐变结束颜色(渐变时必选)
     * https://blog.csdn.net/zengxianzi/article/details/78741148
     * @param popView
     */
    private void initPop1(TextView popView){
//        pop1 = popView;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.pop_sharp_bg_popupwindow, null);

        // 创建PopupWindow对象
        // 此处之所以给了 PopupWindow一个固定的宽度  是因为我要让 PopupWindow的中心位置对齐TextView的中心位置
        mPop1 = new PopupWindow(view, ViewHelpUtils.dipTopx(getActivity(),150), ViewGroup.LayoutParams.WRAP_CONTENT);

        // 需要设置一下此参数，点击外边可消失
        mPop1.setBackgroundDrawable(new BitmapDrawable());
        mPop1.setOutsideTouchable(true);
        //设置点击窗口外边窗口消失
        mPop1.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mPop1.setFocusable(true);
    }

    private void initPop2(TextView popView){
//

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_am:
                mPop1.showAsDropDown(pop1);
                break;
            case R.id.pop_2:
                QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.pop_sharp_bg_popupwindow)
//                        .wrapContentMode()
                        .config(new QuickPopupConfig()
//                                .withShowAnimation(enterAnimation)
//                                .withDismissAnimation(dismissAnimation)
//                                .offsetX(offsetX, offsetRatioOfPopupWidth)
//                                .offsetY(offsetY, offsetRatioOfPopupHeight)
                                .blurBackground(true, new BasePopupWindow.OnBlurOptionInitListener() {
                                    @Override
                                    public void onCreateBlurOption(PopupBlurOption option) {
                                        option.setBlurRadius(6)
                                                .setBlurPreScaleRatio(0.9f);
                                    }
                                })
                                .withClick(R.id.text1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LogUtil.i(TAG,"text1 click");
//                                        ToastUtils.ToastMessage(getContext(), "tx1");
                                    }
                                }))
                        .show((int)pop2.getX(),(int)pop2.getY());
                LogUtil.i(TAG,String.format("(x,y):(%f,%f);(px,py):(%f,%f);(rx,rx):(%f,%f)",
                        pop2.getX(),pop2.getY(),pop2.getPivotX(),pop2.getPivotY(),pop2.getRotationX(),pop2.getRotationY()));
                break;
            case R.id.pop_3://可以在View下显示
                new TestBasePop(getActivity()).showPopupWindow(pop3);
                break;
            case R.id.pop_4:
                new XPopup.Builder(getActivity())
                        .asLoading("a XPopup")
                        .show();
                break;
        }
    }
}
