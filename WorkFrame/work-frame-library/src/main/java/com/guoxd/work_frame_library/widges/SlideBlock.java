package com.guoxd.work_frame_library.widges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guoxd on 2018/5/16.
 */

public class SlideBlock extends LinearLayout {
    final String TAG="SlideBlock";
    //组件列数
    int columnSize = 4;
    //组件行数
    int rowSize =2;
    //单元宽高
    int rowWidth=0;
    int rowHeight = 0;
    //child宽高
    int childWidth=0;
    int childHeight = 0;

    HashMap<Integer,List<TextView>> childViews;
    //当前上下文
    Context mContext;
    //组件背景色
    int backgroundColor = Color.GRAY;
    //组件横列还是纵列
    @IntDef({ROW, COLUME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ModeTable {}

    public static final int ROW = 0;//行模式
    public static final int COLUME = 1;//列模式
    //设置默认table模式，即添加方向
    @ModeTable int defaultTableMode = ROW;



    public SlideBlock(Context context) {
        this(context,null);
    }

    public SlideBlock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initData();
    }

    private void initData() {
        linearLayouts = new ArrayList<>();
        childViews = new HashMap<>();
    }

    ArrayList<LinearLayout> linearLayouts;


    /**
     * 绘制View准备
     */
    private void setView() {
        if(desiredWidth==0) {//没有宽高度不绘制
            Log.i(TAG,"getSceneHeight:0");
        }else{//有宽高
            Log.i(TAG,"getSceneHeight:"+desiredHeight);
            //清空子View
            removeAllViews();
            for(LinearLayout ll:linearLayouts){
                ll.removeAllViews();
            }
            linearLayouts.clear();
            //设置LinearLayout
            setBackgroundColor(backgroundColor);

            if(defaultTableMode == COLUME){//列模式添加
                setOrientation(HORIZONTAL);
                //计算单元宽高
                rowWidth = desiredWidth/rowSize;
                rowHeight = desiredHeight/columnSize;
                //计算child宽高
                childWidth = rowWidth;
                childHeight = desiredHeight;//注意这里不能直接等于LayoutParams.MATCH_PARENT，否则会导致显示不出来
                addChild(rowSize,columnSize);
            }else{
                setOrientation(VERTICAL);
                //计算单元宽高
                rowWidth = desiredWidth/columnSize;
                rowHeight = desiredHeight/rowSize;
                //计算child宽高
                childWidth = desiredWidth;
                childHeight = rowHeight;
                addChild(rowSize,columnSize);
            }
            Log.i(TAG,"setView child:"+childWidth+" "+childHeight);
            Log.i(TAG,"setView call:"+rowWidth+" "+rowHeight);
            //设置child宽高
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth,MeasureSpec.EXACTLY);
            int heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY);
            Log.i(TAG,"setView childCount:"+getChildCount());
            //这一步保证onLayout时child的宽高是有值得的
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                if(desiredWidth!=0) {
                    child.measure(widthMeasureSpec, heightMeasureSpec);
                }
            }

            //计算偏移量，可通过增加title的方式用掉
//            int marginHorizon = desiredWidth -(rowWidth*row);
//            int marginVer = desiredHeight -(rowHeight*column);
//            Log.i(TAG,"call w:"+rowWidth+" h:"+rowHeight+" marginHorizon:"+marginHorizon+" marginVer:"+marginVer);
//            setPadding(marginHorizon/2,marginVer/2,(marginHorizon-marginHorizon/2),marginVer-marginVer/2);
        }
    }

    /**
     * 逐一添加child和其中的子view
     * @param first
     * @param second
     */
    void addChild(int first,int second){
        childViews.clear();
        for (int i = 0; i < first; i++) {
            //增加子容器
            LinearLayout ll = createLinearLayout();
            addView(ll, new LayoutParams(childWidth, childHeight));
            List<TextView> textViews = new ArrayList<>();
            childViews.put(i,textViews);

            //增加call
            for (int j = 0; j < second; j++) {
                //向child中添加View
                TextView tv =createTextView("text"+i+"" + j,(i + j) % 2 == 0 ? Color.RED : Color.CYAN);
                textViews.add(tv);
                ll.addView(tv,new LayoutParams(rowWidth, rowHeight));
                //向child中添加ViewGroup
//                    ll.addView(createLinearLayout((i + j) % 2 == 0 ? Color.RED : Color.DKGRAY),new LayoutParams(rowWidth, rowHeight));
            }
        }
    }

    int desiredWidth=0;
    int desiredHeight=0;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        desiredWidth = w;
        desiredHeight = h;
        Log.i(TAG,"on size change:"+w+" "+h);
        setView();
    }



    /**
     * 本步骤仅用于测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG,"onMeasure");
        Log.i(TAG,"width:"+MeasureSpec.getSize(widthMeasureSpec));
        Log.i(TAG,"height:"+ MeasureSpec.getSize(heightMeasureSpec));

    }

    //onLayout方法是ViewGroup对其所有的子childView进行定位摆放，所以onLayout方法属于ViewGroup而在View中并没有onLayout
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i(TAG,"onLayout");
        Log.i(TAG,"left:"+l+" right:"+r+" top:"+t+" bottom:"+b);
        //子View数目
        int count = getChildCount();
        //组件子View的起始点
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            //经过child.measure设置，值仍可能为零
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            //设置child位置
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            Log.i(TAG,"child childLeft:"+childLeft+" childTop:"+childTop+" childRight:"+childLeft + childWidth+" childBottom:"+childTop + childHeight);
            //自增位置
            if(defaultTableMode == COLUME){
                //列模式增加x点位置
                childLeft+=childWidth;
            }else{
                //行模式增加y点位置
                childTop+=childHeight;
            }
        }
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG,"onDraw");
    }


    /**容器创建
     * 创建与容器本身添加view方向相反的父容器
     * @return
     */
    public LinearLayout createLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(defaultTableMode==COLUME? LinearLayout.VERTICAL:LinearLayout.HORIZONTAL);
        return linearLayout;
    }

    /**View创建
     * 创建子View
     * @return
     */
    public TextView createTextView(String text,int color) {
        TextView view = new TextView(mContext);
        view.setText(text);
        view.setTextColor(Color.WHITE);
        view.setTextSize(16);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundColor(color);
        view.setTag(text);
        return view;
    }

    /**
     * 经过测试只有down
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"X:"+event.getX()+" Y:"+event.getY());//组件内Y值
        Log.i(TAG,"RawX:"+event.getRawX()+" RawY:"+event.getRawY());//屏幕中Y值

        switch (event.getAction()& MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"getAction:down");
                //确定组件位置
              //  "X:"+event.getX()+" Y:"+event.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"getAction:up");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"getAction:move");
                break;
            default:
                Log.i(TAG,"getAction:default");
                break;
        }
        return true;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getRowSize() {
        return rowSize;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getDefaultTableMode() {
        return defaultTableMode;
    }

    public void setDefaultTableMode(int defaultTableMode) {
        this.defaultTableMode = defaultTableMode;
    }
}
