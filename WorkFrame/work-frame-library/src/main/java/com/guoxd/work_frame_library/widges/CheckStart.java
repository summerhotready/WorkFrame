package com.guoxd.work_frame_library.widges;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guoxd.work_frame_library.R;
import com.guoxd.work_frame_library.listeners.StartOnClickListener;
import com.guoxd.work_frame_library.utils.PaintViewUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoxd on 2016/8/19.
 * this view base on checkbox to build like ratting bar
 * this class has three mode:StartMode
 *
 * cause start image has width and height,when the start too much cann't show.
 */
public class CheckStart extends LinearLayout {
    public final String TAG = "CheckStart";
    /**
     * when you check star, only star who is checked is turn on
     * 单一选择
     */
    public static final int SELECT_START_ONLY_CHECK = 1;
    /**
     * when you check star, from star who is checked is turn on
     * 范围选择
     */
    public static final int SELECT_START_BEGIN_CHECK = 0;
    /**
     * when start check,when you checked the last one who is on,all turn off
     * 仅显示模式，无点击效果
     */
    public static final int SELECT_START_NULL_CHECK = 2;
    @IntDef ({SELECT_START_BEGIN_CHECK,SELECT_START_ONLY_CHECK,SELECT_START_NULL_CHECK})
    @Retention(RetentionPolicy.SOURCE)
    public  @interface StartMode{}
    @StartMode int CheckMode=0;

    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;
    @IntDef ({GRAVITY_CENTER,GRAVITY_LEFT,GRAVITY_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityMode{}

    //默认高度
    private final int defaultStartSize = 35;

    //当前上下文
    Context mContext;
    //stars存放星星的容器,方便调整
    LinearLayout ll;
    //start
    List<TextView> dotViewsList;
    //start's width
    int widthStart;
    //组件宽高
    int viewWidth=0;
    int viewHeight=0;

    private int mMax=5;
    private int mNumber=0;
    //内容居中
    @GravityMode int mGravity=0;
    //星星方向
    private int mOrientation=0;
    //startView的margin，默认是5dp
    private int mMargin=0;
    //startSize
    int startSize=0;


    private boolean isChecked;


    public CheckStart(Context context){
        this(context,null);
    }
    public CheckStart(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public CheckStart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        mMargin = Math.round(mContext.getResources().getDisplayMetrics().density*5);
        TypedArray type = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckStart,0,0);
        try{
            mNumber = type.getInteger(R.styleable.CheckStart_Progress,mNumber);
            mMax = type.getInteger(R.styleable.CheckStart_Max,mMax);
            mGravity = type.getInteger(R.styleable.CheckStart_gravity,mGravity);
            mOrientation = type.getInteger(R.styleable.CheckStart_orientation,LinearLayout.HORIZONTAL);
            mMargin = type.getDimensionPixelOffset(R.styleable.CheckStart_StartMargin,0);
            widthStart = type.getDimensionPixelOffset(R.styleable.CheckStart_StartWidth,0);
            //默认不可点击
            isChecked = type.getBoolean(R.styleable.CheckStart_isChecked,false);

        }catch (Exception e){
        }finally {
            type.recycle();
        }
        setMode(isChecked? SELECT_START_ONLY_CHECK : SELECT_START_NULL_CHECK);
        //获取星星尺寸
        //注意使用此方法不能获得svg的宽高
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.start_chedked);
//        minWidthStart = bitmap.getWidth();
//        Log.i(TAG,"bitmap:"+minWidthStart);

        //设置容器
        dotViewsList = new ArrayList<>();
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure width:"+MeasureSpec.getSize(widthMeasureSpec)+" height:"+MeasureSpec.getSize(heightMeasureSpec));
        Log.d(TAG, "onMeasure padding:"+getPaddingLeft()+", "+getPaddingRight()+", "+getPaddingTop()+", "+getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged width:"+w+" height:"+h);
        Log.d(TAG, "onSizeChanged padding:"+getPaddingLeft()+", "+getPaddingRight()+", "+getPaddingTop()+", "+getPaddingBottom());

        viewWidth = w-getPaddingLeft()-getPaddingRight();
        viewHeight = h-getPaddingTop()-getPaddingBottom();
        setView();
    }


    private StartOnClickListener startClickListener = new StartOnClickListener() {
        @Override
        public void onClick(boolean b, String tag) {
            Log.i(TAG,"tag is"+tag);
            if (CheckMode == SELECT_START_BEGIN_CHECK) {
                //从点1-2

            }else if (CheckMode == SELECT_START_NULL_CHECK) {
                //只修改点击处

            }
        }
    };

    private void setView() {
        Log.i(TAG,"setView");
        removeAllViews();

        ll = new LinearLayout(mContext);
        ll.setBackgroundColor(Color.WHITE);
        int otherMargin =0;
        //设置星星方向
        if(mOrientation == HORIZONTAL){
            ll.setOrientation(HORIZONTAL);
            //viewWidth/mMax-mMargin;
            startSize = Math.min(viewWidth/mMax,viewHeight);
            if( startSize*mMax+mMargin*(mMax-1) > viewWidth){
                startSize = viewWidth-(mMargin*(mMax-1))/mMax;
                otherMargin = viewWidth-(startSize*mMax);
            }
        }else{
            ll.setOrientation(VERTICAL);
            startSize = Math.min(viewWidth,viewHeight/mMax);
            if( startSize*mMax+mMargin*(mMax-1) > viewHeight){
                startSize = viewHeight-(mMargin*(mMax-1))/mMax;
                otherMargin = viewHeight-(startSize*mMax);
            }
        }
        addView(ll,new LayoutParams(viewWidth,viewHeight));
    Log.i(TAG,"item:"+startSize+" margin:"+mMargin+" other:"+otherMargin);
        for(int i=0;i<mMax;i++){
            StartView view = new StartView(mContext,startSize);
            view.startChecked((i<mNumber-1)? true:false);
            view.setTag(i);

            if (CheckMode != SELECT_START_NULL_CHECK) {
                //允许点击
                view.setStartViewClickListener(startClickListener);
            }
            dotViewsList.add(view);
            ll.addView(view,new LayoutParams(startSize,startSize));
        }
        //设置child宽高,不设置此处后面写onLayout也不能显示
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWidth,MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight,MeasureSpec.EXACTLY);
        Log.i(TAG,"setView childCount:"+getChildCount());
        //这一步保证onLayout时child的宽高是有值得的
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            Log.i(TAG,"onLayout width:"+child.getMeasuredWidth()+" height:"+child.getMeasuredHeight());
            //设置child位置
            child.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft()+viewWidth, getPaddingTop()+viewHeight);
        }
    }


//        if(mGravity ==0) {
//            ll.setGravity(Gravity.CENTER);
//        }else if(mGravity ==1){
//            ll.setGravity(Gravity.LEFT);
//        }else{
//            ll.setGravity(Gravity.RIGHT);
//        }
//
//        if(w1 >0  && isFirst){//need setView
//            if(w1 != mWidth){//set width
//                mWidth = w1;
//            }
//
//            boolean bb = false;
//            int mm = 3*mContext.getResources().getDisplayMetrics().densityDpi;
////            int mm = context.getResources().getDimensionPixelSize(R.dimen.line);
//            if(minWidthStart* mMax+mm *(mMax-1) >w1){//不能满足最小需求时更改Max
//                mMax = (w1+mm)/(minWidthStart+mm);
//                bb = true;
//            }
//
//            if(bb){
//                widthStart = minWidthStart;
//                mMargin = mm;
//                mStartSize = widthStart;
//            }else if(true){//宽度自定
//                if(true){//完全自定义，margin占宽度的五分之一
//                    int width = w1/mMax;
//                    mMargin = width/5;
//                    widthStart = (w1 - (mMargin *(mMax-1)))/mMax;
//                    if(widthStart <minWidthStart){//重置
//                        widthStart = minWidthStart;
//                        mMargin = ( w1 - mMax * widthStart)/(mMax-1);
//                    }
//                }else{//有margin
//                    widthStart = (w1 - mMargin*(mMax-1))/mMax;
//                    if(widthStart<minWidthStart){//重置
//                        widthStart = minWidthStart;
//                        mMargin = ( w1 - mMax * widthStart)/(mMax-1);
//                    }
//                }
//                mStartSize = widthStart;
//            }else{//has width
//                if(true){//set
//                    mWidth = w1;
//                    if( widthStart*mMax >w1){
//                        mMargin = 3*mContext.getResources().getDisplayMetrics().densityDpi;
////                        mMargin = context.getResources().getDimensionPixelSize(R.dimen.line);
//                        widthStart =( w1- mMargin*(mMax-1) )/mMax;
//                    }else{
//                        mMargin = w1/mMax -widthStart;
//                        mStartSize = widthStart;
//                    }
//                }else{//确定宽度和间隔，需计算宽度和间隔是否小于w1
//                    if( ( widthStart * mMax )+ mMargin*(mMax-1)>w1){
//
//                        if(widthStart*mMax >w1){//需要更改宽度
//                            mMargin = 3*mContext.getResources().getDisplayMetrics().densityDpi;
////                            mMargin = context.getResources().getDimensionPixelSize("1dp");
//                            widthStart =( w1- mMargin*(mMax-1) )/mMax;
//                        }else{//近更改margin
//                            mMargin = (w1 - (widthStart*mMax))/(mMax-1);
//                        }
//                    }else{
//                        mMargin =  (w1 - (widthStart*mMax))/(mMax-1);
//                    }
//                }
//                mStartSize = widthStart;
//            }
//
//            isFirst = false;
//            if(mMax >0){
//                setData(mNumber);
//            }
//        }


    public void setMax(int max){
        this.mMax = max;
    }

//    public void setProgress(int progress){
//        if(null != dotViewsList && dotViewsList.size()>= progress){
//            if(dotViewsList.size()<progress){
//                progress = dotViewsList.size();
//            }
//
//            if (CheckMode == SELECT_START_BEGIN_CHECK) {
//                mNumber = progress;
//                for (int i = 0; i < dotViewsList.size(); i++) {
//                    if (i <= progress) {
//                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
//                    } else {
//                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                    }
//                }
//            } else if (CheckMode == SELECT_START_ONLY_CHECK) {
//                mNumber = progress;
//                for (int i = 0; i < dotViewsList.size(); i++) {
//                    if (i == progress) {
//                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
//                    } else {
//                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                    }
//                }
//            } else if (CheckMode == SELECT_START_NULL_CHECK) {
//                if(mNumber == progress){
//                    mNumber = -1;
//                    for (int i = 0; i < dotViewsList.size(); i++) {
//                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                    }
//                } else {
//                    mNumber = progress;
//                    for (int i = 0; i < dotViewsList.size(); i++) {
//                        if (i <= progress) {
//                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
//                        } else {
//                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                        }
//                    }
//                }
//            }
//        }else{
//            setData(progress);
//        }
//    }

    public void setMode(int fag){
        this.CheckMode = fag;
    }
//    public void setData(final int num){
//        Log.d(TAG,"setData:"+num);
//        if(mMax == 0){
//            mMax = num;
//        }
//
//        mNumber = num;
//        if( mStartSize >0) {
//            ll.removeAllViews();
//            dotViewsList = new ArrayList<>();
//            if (mMax > 0) {
//                for (int i = 0; i < mMax; i++) {
//                    TextView cb = new TextView(mContext);
//                    cb.setTag(i);
//                    cb.setWidth(mStartSize);
//                    cb.setHeight(mStartSize);
//                    if (CheckMode == SELECT_START_ONLY_CHECK) {
//                        if (mNumber > 0 && i == mNumber - 1) {
//                            cb.setBackgroundResource(R.mipmap.star_on);
//                        } else {
//                            cb.setBackgroundResource(R.mipmap.star_off);
//                        }
//                    } else {
//                        if (mNumber > 0 && i <= mNumber - 1) {
//                            cb.setBackgroundResource(R.mipmap.star_on);
//                        } else {
//                            cb.setBackgroundResource(R.mipmap.star_off);
//                        }
//                    }
//                    cb.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int number = Integer.valueOf(v.getTag().toString());
//                            ViewGroup.LayoutParams lp = dotViewsList.get(number).getLayoutParams();
//
//                            if (CheckMode == SELECT_START_BEGIN_CHECK) {
//                                mNumber = number;
//                                for (int i = 0; i < dotViewsList.size(); i++) {
//                                    if (i <= number) {
//                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
//                                    } else {
//                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                                    }
//                                }
//                            } else if (CheckMode == SELECT_START_ONLY_CHECK) {
//                                mNumber = number;
//                                for (int i = 0; i < dotViewsList.size(); i++) {
//                                    if (i == number) {
//                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
//                                    } else {
//                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                                    }
//                                }
//                            } else if (CheckMode == SELECT_START_NULL_CHECK) {
//                                if (mNumber == number) {
//                                    mNumber = -1;
//                                    for (int i = 0; i < dotViewsList.size(); i++) {
//                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                                    }
//                                } else {
//                                    mNumber = number;
//                                    for (int i = 0; i < dotViewsList.size(); i++) {
//                                        if (i <= number) {
//                                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
//                                        } else {
//                                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    });
//                    if (!isChecked) {
//                        cb.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        });
//                    }
//                    dotViewsList.add(cb);
//
//                    if (i != 0) {
//                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT);
//                        lp.setMargins(mMargin, 0, 0, 0);
//                        cb.setLayoutParams(lp);
//                        ll.addView(cb);
//                    } else {
//                        ll.addView(cb);
//                    }
//                }
//            }
//        }
//    }

    public int getChecked(){
        return mNumber;
    }

}
