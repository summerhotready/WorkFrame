package com.guoxd.work_frame_library.widges;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guoxd.work_frame_library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoxd on 2016/8/19.
 * this view base on checkbox to build like ratting bar
 * this class has three mode
 *
 * cause start image has width and height,when the start too much cann't show.
 */
public class CheckStart extends LinearLayout {
    public final String TAG = "CheckStart";

    /**
     * when you check star, only star who is checked is turn on
     */
    public static int SELECT_START_ONLY_CHECK = 1;
    /**
     * when you chedk star, from star who is checked is turn on
     */
    public static int SELECT_START_BEGIN_CHECK = 0;
    /**
     * when start check,when you checked the last one who is on,all turn off
     */
    public static int SELECT_START_NULL_CHECK = 2;


    public int flag;


    Context context;
    public  LinearLayout ll;
    public List<TextView> dotViewsList;
    public int widthStart,minWidthStart;

    public CheckStart(Context context){
        this(context,null);
    }
    public CheckStart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        Log.d(TAG,"CheckStart constructor");
        TypedArray tyep = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckStart,0,0);
        try{
            mNumber = tyep.getInteger(R.styleable.CheckStart_Progress,mNumber);
            mMax = tyep.getInteger(R.styleable.CheckStart_Max,mMax);
            mGravity = tyep.getInteger(R.styleable.CheckStart_gravity,mGravity);
            mMargin = tyep.getDimensionPixelOffset(R.styleable.CheckStart_StartMargin,0);
            widthStart = tyep.getDimensionPixelOffset(R.styleable.CheckStart_StartWidth,0);

            isChecked = tyep.getBoolean(R.styleable.CheckStart_isChecked,true);
            if(mMargin>0){
                isAutoMargin = false;
            }else{
                isAutoMargin = true;
            }

            if(widthStart>0){
                isAutoWidth = false;
            }else{
                isAutoWidth = true;
            }

        }catch (Exception e){
        }finally {
            tyep.recycle();
        }



        LayoutInflater.from(context).inflate(R.layout.view, this, true);
        ll = (LinearLayout)findViewById(R.id.view_ll);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        if(mGravity ==0) {
            ll.setGravity(Gravity.CENTER);
        }else if(mGravity ==1){
            ll.setGravity(Gravity.LEFT);
        }else{
            ll.setGravity(Gravity.RIGHT);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.star_on);
        minWidthStart = bitmap.getWidth();
    }

    private int mMax=0;
    private int mNumber=0;
    private int mGravity=0;
    private int mMargin=0;
    private int mWidth;
    public  int mStartSize = 0;
    private boolean isAutoMargin,isAutoWidth,isChecked,isFirst=true;

    public CheckStart(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w1 = getMeasuredWidth();
        Log.d(TAG, "onMeasure");
        int i = mMax;
        if(w1 >0  && isFirst){//need setView
            if(w1 != mWidth){//set width
                mWidth = w1;
            }

            boolean bb = false;
            int mm = 3*context.getResources().getDisplayMetrics().densityDpi;
//            int mm = context.getResources().getDimensionPixelSize(R.dimen.line);
            if(minWidthStart* mMax+mm *(mMax-1) >w1){//不能满足最小需求时更改Max
                mMax = (w1+mm)/(minWidthStart+mm);
                bb = true;
            }

            if(bb){
                widthStart = minWidthStart;
                mMargin = mm;
                mStartSize = widthStart;
            }else if(isAutoWidth){//宽度自定
                if(isAutoMargin){//完全自定义，margin占宽度的五分之一
                    int width = w1/mMax;
                    mMargin = width/5;
                    widthStart = (w1 - (mMargin *(mMax-1)))/mMax;
                    if(widthStart <minWidthStart){//重置
                        widthStart = minWidthStart;
                        mMargin = ( w1 - mMax * widthStart)/(mMax-1);
                    }
                }else{//有margin
                    widthStart = (w1 - mMargin*(mMax-1))/mMax;
                    if(widthStart<minWidthStart){//重置
                        widthStart = minWidthStart;
                        mMargin = ( w1 - mMax * widthStart)/(mMax-1);
                    }
                }
                mStartSize = widthStart;
            }else{//has width
                if(isAutoMargin){//set
                    mWidth = w1;
                    if( widthStart*mMax >w1){
                        mMargin = context.getResources().getDimensionPixelSize(R.dimen.line);
                        widthStart =( w1- mMargin*(mMax-1) )/mMax;
                    }else{
                        mMargin = w1/mMax -widthStart;
                        mStartSize = widthStart;
                    }
                }else{//确定宽度和间隔，需计算宽度和间隔是否小于w1
                    if( ( widthStart * mMax )+ mMargin*(mMax-1)>w1){

                        if(widthStart*mMax >w1){//需要更改宽度
                            mMargin = 3*context.getResources().getDisplayMetrics().densityDpi;
//                            mMargin = context.getResources().getDimensionPixelSize("1dp");
                            widthStart =( w1- mMargin*(mMax-1) )/mMax;
                        }else{//近更改margin
                            mMargin = (w1 - (widthStart*mMax))/(mMax-1);
                        }
                    }else{
                        mMargin =  (w1 - (widthStart*mMax))/(mMax-1);
                    }
                }
                mStartSize = widthStart;
            }

            isFirst = false;
            if(mMax >0){
                setData(mNumber);
            }
        }
    }

    public void setMax(int max){
        this.mMax = max;
    }

    public void setProgress(int progress){
        if(null != dotViewsList && dotViewsList.size()>= progress){
            if(dotViewsList.size()<progress){
                progress = dotViewsList.size();
            }

            if (flag == SELECT_START_BEGIN_CHECK) {
                mNumber = progress;
                for (int i = 0; i < dotViewsList.size(); i++) {
                    if (i <= progress) {
                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
                    } else {
                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                    }
                }
            } else if (flag == SELECT_START_ONLY_CHECK) {
                mNumber = progress;
                for (int i = 0; i < dotViewsList.size(); i++) {
                    if (i == progress) {
                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
                    } else {
                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                    }
                }
            } else if (flag == SELECT_START_NULL_CHECK) {
                if(mNumber == progress){
                    mNumber = -1;
                    for (int i = 0; i < dotViewsList.size(); i++) {
                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                    }
                } else {
                    mNumber = progress;
                    for (int i = 0; i < dotViewsList.size(); i++) {
                        if (i <= progress) {
                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
                        } else {
                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                        }
                    }
                }
            }
        }else{
            setData(progress);
        }
    }

    public void setFlag(int fag){
        this.flag = fag;
    }
    public void setData(final int num){
        Log.d(TAG,"setData:"+num);
        if(mMax == 0){
            mMax = num;
        }

        mNumber = num;
        if( mStartSize >0) {
            ll.removeAllViews();
            dotViewsList = new ArrayList<>();
            if (mMax > 0) {
                for (int i = 0; i < mMax; i++) {
                    TextView cb = new TextView(context);
                    cb.setTag(i);
                    cb.setWidth(mStartSize);
                    cb.setHeight(mStartSize);
                    if (flag == SELECT_START_ONLY_CHECK) {
                        if (mNumber > 0 && i == mNumber - 1) {
                            cb.setBackgroundResource(R.mipmap.star_on);
                        } else {
                            cb.setBackgroundResource(R.mipmap.star_off);
                        }
                    } else {
                        if (mNumber > 0 && i <= mNumber - 1) {
                            cb.setBackgroundResource(R.mipmap.star_on);
                        } else {
                            cb.setBackgroundResource(R.mipmap.star_off);
                        }
                    }
                    cb.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int number = Integer.valueOf(v.getTag().toString());
                            ViewGroup.LayoutParams lp = dotViewsList.get(number).getLayoutParams();

                            if (flag == SELECT_START_BEGIN_CHECK) {
                                mNumber = number;
                                for (int i = 0; i < dotViewsList.size(); i++) {
                                    if (i <= number) {
                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
                                    } else {
                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                                    }
                                }
                            } else if (flag == SELECT_START_ONLY_CHECK) {
                                mNumber = number;
                                for (int i = 0; i < dotViewsList.size(); i++) {
                                    if (i == number) {
                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
                                    } else {
                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                                    }
                                }
                            } else if (flag == SELECT_START_NULL_CHECK) {
                                if (mNumber == number) {
                                    mNumber = -1;
                                    for (int i = 0; i < dotViewsList.size(); i++) {
                                        dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                                    }
                                } else {
                                    mNumber = number;
                                    for (int i = 0; i < dotViewsList.size(); i++) {
                                        if (i <= number) {
                                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_on);
                                        } else {
                                            dotViewsList.get(i).setBackgroundResource(R.mipmap.star_off);
                                        }
                                    }
                                }
                            }
                        }
                    });
                    if (!isChecked) {
                        cb.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    dotViewsList.add(cb);

                    if (i != 0) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(mMargin, 0, 0, 0);
                        cb.setLayoutParams(lp);
                        ll.addView(cb);
                    } else {
                        ll.addView(cb);
                    }
                }
            }
        }
    }

    public int getChecked(){
        return mNumber;
    }

}
