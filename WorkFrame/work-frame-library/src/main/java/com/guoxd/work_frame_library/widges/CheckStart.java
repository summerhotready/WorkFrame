package com.guoxd.work_frame_library.widges;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.guoxd.work_frame_library.R;
import com.guoxd.work_frame_library.listeners.StartOnClickListener;
import com.guoxd.work_frame_library.utils.PaintViewUtils;
import com.guoxd.work_frame_library.utils.TextShowUtils;
import com.guoxd.work_frame_library.views.SquareCheckView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by guoxd on 2016/8/19.
 * this view base on checkbox to build like ratting bar
 * this class has four mode:SELECT_START_BEGIN_CHECK,SELECT_START_ONLY_CHECK,SELECT_START_WHICH_CHECK,SELECT_START_RANGE_CHECK
 * cause start image has width and height,when the start too much cann't show.
 */
public class CheckStart extends LinearLayout {
    public final String TAG = "CheckStart";
    /**
     * when you check star, only star who is checked
     * 单一选择，选中的亮起
     */
    public static final int SELECT_START_ONLY_CHECK = 1;
    /**start chedcked who click
     * 多选，哪个星星被点击就会亮起，重新点击不会清空
     */
    public static final int SELECT_START_WHICH_CHECK = 2;
    /**
     * when you check star, from star who is checked is turn on
     * 范围选择，从A到B亮起
     */
    public static final int SELECT_START_RANGE_CHECK = 3;
    /**
     * when start check,when you checked the last one who is on,all turn off
     * 范围选择，从0-N亮起
     */
    public static final int SELECT_START_BEGIN_CHECK = 0;
    @IntDef ({SELECT_START_BEGIN_CHECK,SELECT_START_ONLY_CHECK,SELECT_START_WHICH_CHECK,SELECT_START_RANGE_CHECK})
    @Retention(RetentionPolicy.SOURCE)
    public  @interface StartMode{}
    @StartMode int CheckMode=0;

    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;
    public static final int GRAVITY_TOP = 3;
    public static final int GRAVITY_BOTTOM= 4;
    @IntDef ({GRAVITY_CENTER,GRAVITY_LEFT,GRAVITY_RIGHT,GRAVITY_TOP,GRAVITY_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityMode{}

    //默认高度
    private int mDefaultSize_min;
    private int mDefaultSize_max;

    //当前上下文
    Context mContext;
    //stars存放星星的容器,方便调整
    LinearLayout ll;
    //start
    List<SquareCheckView> dotViewsList;

    //组件宽高
    int viewWidth=0;
    int viewHeight=0;

    //初始选中，范围默认从0-N，单选默认选中N
    private int mNumber=-1;
    //星星总数
    private int mMax=-1;
    //内容居中
    @GravityMode int mGravity=GRAVITY_CENTER;
    //星星方向
    private int mOrientation=HORIZONTAL;
    //startView的margin，默认是5dp
    private int mMargin=-1;

    //是否自动margin flag
    private boolean isAutoMargin=false;
    //startSize,range(1~ViewHeigh|ViewWidth)
    int startSize=0;

    //是否可点击，默认为false
    private boolean isChecked = false;
    /**选中
     * SELECT_START_BEGIN_CHECK模式
     * SELECT_START_ONLY_CHECK模式用于记录当前选中项目，未选中为-1
     * SELECT_START_RANGE_CHECK模式用于记录上一次选中项目，未选中和第二次选中   */
    int checkFrom = -1;

    int checkedId=0,uncheckId=0;
    Drawable checkedDrawable,uncheckDrawable;

    public CheckStart(Context context){
        this(context,null);
    }
    public CheckStart(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public CheckStart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        TypedArray type = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckStart,0,0);
        try{
            mNumber = type.getInteger(R.styleable.CheckStart_Progress,mNumber);
            mMax = type.getInteger(R.styleable.CheckStart_Max,mMax);
            mGravity = type.getInteger(R.styleable.CheckStart_StartGravity,mGravity);
            mOrientation = type.getInteger(R.styleable.CheckStart_StartOrientation,mOrientation);
            mMargin = type.getDimensionPixelOffset(R.styleable.CheckStart_StartMargin,mMargin);
            startSize = type.getDimensionPixelOffset(R.styleable.CheckStart_StartSize,startSize);
            CheckMode = type.getInteger(R.styleable.CheckStart_StartMode,SELECT_START_BEGIN_CHECK);
            //默认不可点击
            isChecked = type.getBoolean(R.styleable.CheckStart_isChecked,isChecked);
            checkedId = type.getResourceId(R.styleable.CheckStart_CheckedDrawable,0);
            uncheckId = type.getResourceId(R.styleable.CheckStart_UncheckDrawable,0);
        }catch (Exception e){
        }finally {
            type.recycle();
        }
        initData();
    }

    private final void initData() {

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        mDefaultSize_min= PaintViewUtils.getSizePX(mContext,30);
        mDefaultSize_max= PaintViewUtils.getSizePX(mContext,150);

        if(checkedId !=0){
            checkedDrawable = PaintViewUtils.getDrawable(mContext,checkedId);
        }
        if(checkedDrawable == null){
            checkedDrawable = PaintViewUtils.getDrawable(mContext,R.drawable.start_chedked);
        }

        if(uncheckId !=0){
            uncheckDrawable = PaintViewUtils.getDrawable(mContext,uncheckId);
        }
        if(uncheckDrawable == null){
            uncheckDrawable = PaintViewUtils.getDrawable(mContext,R.drawable.start_uncheck);
        }
    }

    //初始绘制View
    @SuppressLint("WrongConstant")
    private void setView() {
        //初始化View
        removeAllViews();
        ll = new LinearLayout(mContext);
        ll.setOrientation(mOrientation);
        addView(ll,new LayoutParams(viewWidth,startSize));

        //set margin
        LayoutParams lp=null;
        for(int i=0;i<mMax;i++){
            SquareCheckView view = new SquareCheckView(mContext,startSize);
            view.setDrawable(uncheckDrawable,checkedDrawable);
            if (CheckMode == SELECT_START_ONLY_CHECK) {//范围选择
                view.startChecked((i==mNumber)? true:false);
                checkFrom = mNumber;
            }else{
                view.startChecked((i<mNumber)? true:false);
            }

            view.setTag(i);
            int otherMargin=0;
            if(mOrientation == HORIZONTAL){
                if(mGravity == GRAVITY_CENTER){//居中
                    otherMargin = getLeftRightMargin();
                    if(i==0){
                        int left = otherMargin>0?(otherMargin/2):0;
                        lp = PaintViewUtils.getLayoutParams(startSize,startSize, left,0,0,0);
                    }else if(i == mMax-1){
                        int right = otherMargin>0?(otherMargin-otherMargin/2):0;
                        lp = PaintViewUtils.getLayoutParams(startSize,startSize,mMargin,0,right,0);
                    }else{
                        lp = PaintViewUtils.getLayoutParams(startSize,startSize,mMargin,0,0,0);
                    }
                }else{//left or right
                    otherMargin = mGravity == GRAVITY_RIGHT? getLeftRightMargin():0;
                    lp = PaintViewUtils.getLayoutParams(startSize,startSize,i==0? otherMargin:mMargin,0,0,0);
                }
            }else{
                if(mGravity == GRAVITY_CENTER) {//居中
                    otherMargin = getTopBottomMargin();
                    if(i==0){
                        int top = otherMargin>0?(otherMargin/2):0;
                        lp = PaintViewUtils.getLayoutParams(startSize,startSize, 0,top,0,0);
                    }else if(i == mMax-1){
                        int bottom = otherMargin>0?(otherMargin-otherMargin/2):0;
                        lp = PaintViewUtils.getLayoutParams(startSize,startSize,0,mMargin,0,bottom);
                    }else{
                        lp = PaintViewUtils.getLayoutParams(startSize,startSize,0,mMargin,0,0);
                    }
                }else{
                    otherMargin = mGravity == GRAVITY_BOTTOM? getTopBottomMargin():0;
                    lp = PaintViewUtils.getLayoutParams(startSize,startSize,0,i==0? otherMargin:mMargin,0,0);
                }
            }
            if (isChecked) {//允许点击
                view.setStartViewClickListener(startClickListener);
            }
            dotViewsList.add(view);
            ll.addView(view,lp);
        }

        //设置child宽高,不设置此处后面写onLayout也不能显示
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(mOrientation == HORIZONTAL? viewWidth:startSize,MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(mOrientation == HORIZONTAL? startSize:viewHeight,MeasureSpec.EXACTLY);
        TextShowUtils.ShowLog(TAG,"setView childCount:"+getChildCount());
        //这一步保证onLayout时child的宽高是有值得的
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        TextShowUtils.ShowLog(TAG,"onMeasure padding:"+getPaddingLeft()+", "+getPaddingRight()+", "+getPaddingTop()+", "+getPaddingBottom());
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        TextShowUtils.ShowLog(TAG,"widthSize:"+widthSize+" heightSize:"+heightSize);
        /**
         * 解决宽高为wrap_content时不显示的问题
         */
        // 当模式是AT_MOST（即wrap_content）时设置默认值
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            if(mOrientation == HORIZONTAL){
                setMeasuredDimension(mDefaultSize_max, mDefaultSize_min);
            }else{
                setMeasuredDimension(mDefaultSize_min, mDefaultSize_max);
            }
            // 宽 / 高任意一个模式为AT_MOST（即wrap_content）时，都设置默认值
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultSize_min, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mDefaultSize_min);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        TextShowUtils.ShowLog(TAG,"widthSize:"+w+" heightSize:"+h);

        viewWidth = w-getPaddingLeft()-getPaddingRight();
        viewHeight = h-getPaddingTop()-getPaddingBottom();
        resetValue();
        setView();
    }



    @SuppressLint("WrongConstant")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            if(mOrientation == HORIZONTAL){
                int margin = viewHeight-startSize;
                int marginTop = margin/2+getPaddingTop();
                //设置child位置
                child.layout(getPaddingLeft(), marginTop, getPaddingLeft()+viewWidth, marginTop+startSize);
            }else{
                int margin = viewWidth-startSize;
                int marginLeft = margin/2+getPaddingLeft();
                //设置child位置
                child.layout(marginLeft, getPaddingTop(), marginLeft+startSize, getPaddingTop()+viewHeight);
            }
        }
    }

    //点击事件
    private StartOnClickListener startClickListener = new StartOnClickListener() {
        @Override
        public void onClick(boolean b, String tag) {
            int number= Integer.valueOf(tag);
            if (CheckMode == SELECT_START_RANGE_CHECK) {//范围选择
                //从点1-2
                if(checkFrom<0 || number <checkFrom){//first
                    clearStart();
                    checkStart(number,true);
                    checkFrom = number;
                }else{
                    checkStart(checkFrom,number,true);
                    checkFrom = -1;
                }
            }else if (CheckMode == SELECT_START_ONLY_CHECK) {//唯一选择
                //只修改点击处
                if(number == checkFrom){//the same one
                    checkStart(number,false);
                    checkFrom = -1;
                }else{
                    checkStart(checkFrom,false);
                    checkStart(number,true);
                    checkFrom = number;
                }
            }else if(CheckMode == SELECT_START_BEGIN_CHECK){
                clearStart();
                checkStart(0,number,true);
            }else if(CheckMode == SELECT_START_WHICH_CHECK){
                checkStart(number,!dotViewsList.get(number).isChecked());
            }
        }
    };
    //用于单选 设置点击效果
    private void checkStart(int number,boolean b){
        if(number>=0 && number<dotViewsList.size()) {
            dotViewsList.get(number).startChecked(b);
        }
    }
    //用于范围选择 设置点击效果
    private void checkStart(int from ,int to,boolean b){
        if(!(from >=0 && to >=0 && from <=to)){
            return;
        }
        if(!(from<dotViewsList.size() && to<= dotViewsList.size())){
            return;
        }
        for(int i=from;i<=to;i++){
            dotViewsList.get(i).startChecked(b);
        }
    }
    //清除所有点击效果
    private void clearStart(){
        for(SquareCheckView start:dotViewsList){
            if(start.isChecked()){
                start.startChecked(false);
            }
        }
    }

    private int getLeftRightMargin(){
        return viewWidth-(startSize*mMax)-mMargin*(mMax-1);
    }
    private int getTopBottomMargin(){
        return viewHeight-(startSize*mMax)-mMargin*(mMax-1);
    }
    //重置未取值的默认值
    private void resetValue() {
        dotViewsList = new ArrayList<>();

        if(mOrientation == HORIZONTAL && mGravity>GRAVITY_RIGHT){
            mGravity = GRAVITY_CENTER;
        }
        if(mOrientation == VERTICAL && mGravity!=GRAVITY_CENTER && mGravity<GRAVITY_TOP){
            mGravity = GRAVITY_CENTER;
        }
        if(mMargin<0){//未设置
            isAutoMargin = true;
            //默认值为5dp
            mMargin = Math.round(mContext.getResources().getDisplayMetrics().density*5);
        }
        if(mMax<0){
            mMax = 5;
        }
        int size;
        if(mOrientation == HORIZONTAL){
            if(isAutoMargin){
                size = Math.min(viewWidth/mMax,viewHeight);
                if(startSize==0 || startSize>size){
                    startSize = size;
                }
                mMargin = (viewWidth-startSize*mMax)/(mMax-1);
            }else{
                size = Math.min((viewWidth-(mMax-1)*mMargin)/mMax,viewHeight);
                if(startSize==0 || startSize>size){
                    startSize = size;
                }
            }
        }else{
            if(isAutoMargin){
                size = Math.min(viewWidth,viewHeight/mMax);
                if(startSize==0 || startSize>size){
                    startSize = size;
                }
                mMargin = (viewHeight-startSize*mMax)/(mMax-1);
            }else{
                size = Math.min(viewWidth,(viewHeight-(mMax-1)*mMargin)/mMax);
                if(startSize==0 || startSize>size){
                    startSize = size;
                }
            }
        }
        TextShowUtils.ShowLog(TAG,"view:"+startSize+" margin:"+mMargin);
    }




    public void setMax(int max){
        this.mMax = max;
    }

    public void setProgress(int progress){
        mNumber = progress;
        if(dotViewsList ==null){
            return;
        }
        if(progress<0 || progress>dotViewsList.size()-1)
            return;
        clearStart();
        if(CheckMode == SELECT_START_ONLY_CHECK){
            checkStart(progress,true);
        }else{
            checkStart(0,progress,true);
        }
    }

    public void setMode(int fag){
        this.CheckMode = fag;
    }

    /**
     * checked is 1,uncheck is 0
     * @return
     */
    public int[] getChecked(){
        int size = dotViewsList.size();
        int[] checked =new int[size];
        for(int i=0;i<size;i++){
            checked[i] = dotViewsList.get(i).isChecked()? 1:0;
        }
        return checked;
    }
}
