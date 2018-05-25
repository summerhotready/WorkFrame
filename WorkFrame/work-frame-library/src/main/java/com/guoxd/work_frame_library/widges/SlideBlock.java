package com.guoxd.work_frame_library.widges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by guoxd on 2018/5/16.
 * 组件作用:
 * 这是一个指定行列的拉拽组件，通过左划右划的方式改变View的大小
 * 组件问题：
 * 滑动时有空白，此问题推测是view的绘制造成的
 * 滑动结束时有空白，此问题有几种猜测并没有解决，如果你解决了请提交给我
 */
public class SlideBlock extends LinearLayout {
    final String TAG="SlideBlock";
    //组件宽高
    int desiredWidth=0;
    int desiredHeight=0;
    //组件列数
    int columnSize = 5;
    //组件行数
    int rowSize =2;
    //单元宽高
    int rowWidth=0;
    int rowHeight = 0;
    //child宽高
    int childWidth=0;
    int childHeight = 0;

    ArrayList<ArrayList<TextView>> childViews;
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


    public static final int MOVE_TOP=0;
    public static final int MOVE_BOTTOM=1;
    public static final int MOVE_LEFT=2;
    public static final int MOVE_RIGHT=3;
    @IntDef({MOVE_TOP,MOVE_BOTTOM,MOVE_LEFT,MOVE_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    private @interface MoveMotion{}
    @MoveMotion int defaultMotion=0;


    public SlideBlock(Context context) {
        this(context,null);
    }

    public SlideBlock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
    }

    private void initData() {
        childViews = new ArrayList<>();
    }
    /**
     * 绘制View准备
     */
    private void setView() {
        if(desiredWidth==0) {//没有宽高度不绘制
            Log.i(TAG,"getSceneHeight:0");
        }else{//有宽高
            Log.i(TAG,"getSceneHeight:"+desiredHeight);
            //初始化数据
            initData();
            //清空子View
            removeAllViews();
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
            }else{//排模式添加
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
            invalidate();
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
            ll.setBackgroundColor(Color.BLACK);
            addView(ll, new LayoutParams(childWidth, childHeight));
            ArrayList<TextView> textViews = new ArrayList<>();
            childViews.add(textViews);

            //增加call
            for (int j = 0; j < second; j++) {
                //向child中添加View
                TextView tv =createTextView(j,(i + j) % 3 == 0 ? Color.YELLOW :(i+j)%3 ==1?  Color.CYAN:Color.GRAY);
                textViews.add(tv);
                ll.addView(tv,new LayoutParams(rowWidth, rowHeight));
                //向child中添加ViewGroup
//                    ll.addView(createLinearLayout((i + j) % 2 == 0 ? Color.RED : Color.DKGRAY),new LayoutParams(rowWidth, rowHeight));
            }
        }
    }

    //flag 用于判定是否走onlayout
    boolean isChange=false;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        desiredWidth = w;
        desiredHeight = h;
        isChange = true;
        Log.i(TAG,"on size change:"+w+" "+h);
        setView();
    }

    /**
     * 本步骤仅用于测量
     * 一般会走三次，最后一次结果是准的
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.i(TAG,"onMeasure");
//        MeasureSpec.getSize(widthMeasureSpec)
//        MeasureSpec.getSize(heightMeasureSpec)
    }

    //onLayout方法是ViewGroup对其所有的子childView进行定位摆放，所以onLayout方法属于ViewGroup而在View中并没有onLayout
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.i(TAG,"onLayout");
        //子View数目
        int count = getChildCount();
        //组件子View的起始点
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        if(isChange) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                //经过child.measure设置，值仍可能为零
    //            int width = child.getMeasuredWidth();
    //            int height = child.getMeasuredHeight();
                //设置child位置
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                //自增位置
                if (defaultTableMode == COLUME) {
                    //列模式增加x点位置
                    childLeft += childWidth;
                } else {
                    //行模式增加y点位置
                    childTop += childHeight;
                }
            }
        }else{
            isChange = false;
        }
    }

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
    public TextView createTextView(int text,int color) {
        TextView view = new TextView(mContext);
        view.setText(""+text);//注意这里不能直接set数字
        view.setTextColor(Color.WHITE);
        view.setTextSize(12);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundColor(color);
        view.setTag(text);
        return view;
    }

    float clickDownX=0,clickDownY=0;
    float clickX=0,clickY=0;
    TextView moveTextView=null,pastTextView=null,nextTextView=null;
    int pointLayout=0;
    ArrayList<TextView> nowList;//当前选中列/行
    /**
     * 经过测试return super时只有down
     * @param event
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()& MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"getAction:down");
                //确定组件位置
                //获取点击瞬间的组件
                clickDownX = Math.round(event.getX());
                clickDownY = Math.round(event.getY());
                clickX = clickDownX;
                clickY = clickDownY;
                if(defaultTableMode == COLUME){//列，依靠X轴定位确定组件属于哪一列
                    pointLayout = (int)Math.floor(clickDownX/ rowWidth);
                    nowList = childViews.get(pointLayout);
                    for(TextView view:nowList){
                        if(clickY>=view.getY()&& clickY<(view.getY()+view.getHeight())){
                            moveTextView = view;
                            //除去边缘例外的情况
                            defaultMotion = clickY<(view.getY()+view.getPivotY())?MOVE_TOP:MOVE_BOTTOM;
                            Log.e(TAG,moveTextView.getTag().toString()+" is click Y:"+clickY+" "+view.getY()+" , "+(view.getY()+view.getWidth())+" "+defaultMotion);
                            resetLeftRight();
                        }
                    }
                }else{//排，依靠Y轴定位确定组件属于哪一排
                    pointLayout = (int)Math.floor(clickDownY/ rowHeight);
                    nowList = childViews.get(pointLayout);
                    for(TextView view:nowList){
                        if(clickX>=view.getX()&& clickX<(view.getX()+view.getWidth())){
                            moveTextView = view;
                            //除去边缘例外的情况
                            defaultMotion = clickX<(view.getX()+view.getPivotX())?MOVE_LEFT:MOVE_RIGHT;
                            Log.e(TAG,moveTextView.getTag().toString()+" is click X:"+clickX+" "+view.getX()+" , "+(view.getX()+view.getWidth())+" "+defaultMotion);
                            resetLeftRight();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"getAction:up");
//                Log.i(TAG,"X:"+event.getX()+" Y:"+event.getY());//组件内Y值
//                Log.i(TAG,"RawX:"+event.getRawX()+" RawY:"+event.getRawY());//屏幕中Y值
                pastTextView = null;
                nextTextView = null;
                moveTextView = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (moveTextView == null){
                    return true;
                }
                if(defaultTableMode == COLUME){
                    int moveSize = Math.round(Math.min(event.getY(),desiredHeight)-clickDownY) ;
                    Log.i(TAG,"getAction:move "+moveSize);
                    if(moveSize>0) {//向下
                        if ((nextTextView != null && pastTextView == null) ||
                                (nextTextView != null && pastTextView!=null && defaultMotion == MOVE_BOTTOM)) {
                            moveBottomToBottom(moveSize, nextTextView, moveTextView);
                        }
                        if((pastTextView!=null && nextTextView == null) ||
                                (nextTextView != null && pastTextView!=null&&defaultMotion == MOVE_TOP)){
                            moveTopToBottom(moveSize, pastTextView, moveTextView);
                        }
                    }else{//向上
                        if ((nextTextView != null && pastTextView == null) ||
                                (nextTextView != null && pastTextView!=null && defaultMotion == MOVE_BOTTOM)) {
                            moveBottomToTop(moveSize, nextTextView, moveTextView);
                        }
                        if((pastTextView!=null && nextTextView == null) ||
                                (nextTextView != null && pastTextView!=null&&defaultMotion == MOVE_TOP)){
                            moveTopToTop(moveSize, pastTextView, moveTextView);
                        }
                    }
                }else{
                    int moveSize = Math.round(Math.min(event.getX(),desiredWidth)-clickDownX) ;
                    Log.i(TAG,"getAction:move "+moveSize);
                    if(moveSize>0) {//向右
                        if ((nextTextView != null && pastTextView == null) ||
                                (nextTextView != null && pastTextView!=null && defaultMotion == MOVE_RIGHT)) {
                            moveRightToRight(moveSize, nextTextView, moveTextView);
                        }
                        if((pastTextView!=null && nextTextView == null) ||
                                (nextTextView != null && pastTextView!=null&&defaultMotion == MOVE_LEFT)){
                            moveLeftToRight(moveSize, pastTextView, moveTextView);
                        }
                    }else if(moveSize<0){//向左
                        if(pastTextView!=null && nextTextView==null){
                            moveLeftToLeft(moveSize,pastTextView,moveTextView);
                        }else if(pastTextView==null && nextTextView!=null){
                            moveRightToLeft(moveSize,nextTextView,moveTextView);
                        }else if(nextTextView != null && pastTextView!=null){
                            if(defaultMotion == MOVE_LEFT){//左View向右移动
                                moveLeftToLeft(moveSize,pastTextView,moveTextView);
                            }else{//右View向右移动
                                //当前move增加宽度，右边move改变定位
                                moveRightToLeft(moveSize,nextTextView,moveTextView);
                            }
                        }
                    }
                }
                clickDownX = event.getX();
                clickDownY = event.getY();
                break;
            default:
                Log.i(TAG,"getAction:default");
                break;
        }
        return true;
    }

    /**
     * 检测move应该的宽度的
     * @return
     */
//    int getMoveViewWidth(){
//        int width=0;
//        int size = nowList.size();
//        for(int i=0;i<size;i++){
//            if(!nowList.get(i).equals(moveText)){
//                width+=nowList.get(i).getWidth();
//            }else{
//                Log.i(TAG,"move width:"+moveText.getWidth());
//            }
//        }
//        return desiredWidth-width;
//    }

    /**排，向右移动
     * 当前view向右移动，增加move的宽度，向右（移动）neighbor的左边
     * @param moveSize 正值
     * @param neighbor 右兄弟
     * @param move
     */
    void moveRightToRight(int moveSize,TextView neighbor,TextView move){
        if(neighbor.getWidth()-moveSize > 0){
            //减少右兄弟的宽度，修改X坐标
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            neighbor.setTranslationX(moveSize);
            olp.width = olp.width - moveSize;
            neighbor.setLayoutParams(olp);
            Log.d(TAG,"neighbor :"+neighbor.getX()+" space width:"+olp.width+" real width:"+neighbor.getWidth()+" moveSize:"+moveSize);
        }else{//移除
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            moveSize = neighbor.getWidth();//等于当前宽度
            Log.d(TAG,"reset :"+" moveSize:"+moveSize+" space:"+olp.width);
            removeView(neighbor);
            nowList.remove(neighbor);
            resetLeftRight();
        }
        LayoutParams movelp = (LayoutParams) move.getLayoutParams();
        movelp.width = movelp.width+moveSize;//如此这般发现宽度没有变化
        move.setLayoutParams(movelp);
        Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.width+" real width:"+move.getWidth()+" moveSize:"+moveSize);
    }

    /**排，向右移动
     * 点击view的左边当前view向右移动，增加兄弟的宽度，向右改变（移动）自己的左边
     * @param moveSize 正直
     * @param neighbor 左兄弟
     * @param move
     */
    void moveLeftToRight(int moveSize,TextView neighbor,TextView move){
        if(move.getWidth()-moveSize > 0){//可移动
            LayoutParams movelp = (LayoutParams) move.getLayoutParams();
            movelp.width = movelp.width - moveSize;
            move.setLayoutParams(movelp);
            move.setTranslationX(moveSize);
            Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.width+" real width:"+move.getWidth()+" moveSize:"+moveSize);
        }else{//移除
            moveSize = move.getWidth();//等于当前宽度
            removeView(move);
            nowList.remove(move);
            resetLeftRight();
        }
        LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
        //原本使用 olp.width = Math.round(moveText.getX()-neighbor.getX());
        olp.width = olp.width + moveSize;//如此这般发现宽度没有变化
        neighbor.setLayoutParams(olp);
    }

    /**排，向左移动
     * 当前view向左移动，向左改变（移动）自己的左边，减少左兄弟的宽度
     * @param moveSize 为正值
     * @param neighbor 左兄弟
     * @param move
     */
    void moveLeftToLeft(int moveSize,TextView neighbor,TextView move){
        moveSize = Math.abs(moveSize);//重置
        if(neighbor.getWidth() - moveSize > 0){
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            olp.width = olp.width - moveSize;
            neighbor.setLayoutParams(olp);
            Log.d(TAG,"neighbor :"+neighbor.getX()+" space width:"+olp.width+" real width:"+neighbor.getWidth()+" moveSize:"+moveSize);
        }else{//左兄弟移除
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            moveSize = neighbor.getWidth();//等于当前宽度
            Log.d(TAG,"reset :"+" moveSize:"+moveSize+" space:"+olp.width);
            removeView(neighbor);
            nowList.remove(neighbor);
            resetLeftRight();
        }
        move.setTranslationX(-moveSize);
        LayoutParams movelp = (LayoutParams) move.getLayoutParams();
        movelp.width = movelp.width+moveSize;//如此这般发现宽度没有变化
        move.setLayoutParams(movelp);
        Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.width+" real width:"+move.getWidth()+" moveSize:"+moveSize);
    }

    /**排，向左移动
     * 点击view的右边当前view向左移动，减少move的宽度，向左改变（移动）neighbor的左边
     * @param moveSize 为负值
     * @param neighbor 右兄弟
     * @param move
     */
    void moveRightToLeft(int moveSize,TextView neighbor,TextView move){
        moveSize = Math.abs(moveSize);//重置
        if(move.getWidth() - moveSize > 0){//可移动
            LayoutParams movelp = (LayoutParams) move.getLayoutParams();
            movelp.width = movelp.width - moveSize;
            move.setLayoutParams(movelp);
            Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.width+" real width:"+move.getWidth()+" moveSize:"+moveSize);
        }else{//移除
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            moveSize = move.getWidth();//等于当前宽度
            removeView(move);
            nowList.remove(move);
            resetLeftRight();
        }
        //移动
        neighbor.setTranslationX(-moveSize);
        //
        LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
        //原本使用 olp.width = Math.round(moveText.getX()-neighbor.getX());计算但出现move的x不变化的问题
        olp.width = olp.width + moveSize;//如此这般发现宽度没有变化
        neighbor.setLayoutParams(olp);
    }

    /**列，向下移动
     * 当前view向下移动，向下改变（移动）兄弟的上边，增加自己的高度
     * @param moveSize 为正值
     * @param neighbor 下面兄弟
     * @param move
     */
    void moveBottomToBottom(int moveSize,TextView neighbor,TextView move){
        if(neighbor.getHeight() - moveSize > 0){//是否可移动
            //减少兄弟高度
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            olp.height = olp.height - moveSize;
            neighbor.setLayoutParams(olp);
            Log.d(TAG,"neighbor :"+neighbor.getY()+" space width:"+olp.height+" real width:"+neighbor.getHeight()+" moveSize:"+moveSize);
        }else{//兄弟移除
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            moveSize = neighbor.getHeight();//等于当前剩余高度
            Log.d(TAG,"reset :"+" moveSize:"+moveSize+" space:"+olp.height);
            removeView(neighbor);
            nowList.remove(neighbor);
            resetLeftRight();
        }
        //移动兄弟Y坐标
        neighbor.setTranslationY(moveSize);
        //增加当前高度
        LayoutParams movelp = (LayoutParams) move.getLayoutParams();
        movelp.height = movelp.height+moveSize;//如此这般发现宽度没有变化
        move.setLayoutParams(movelp);
        Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.width+" real width:"+move.getWidth()+" moveSize:"+moveSize);
    }

    /**列，向下移动
     * 当前view向下移动，向下改变（移动）自己的上边，增加上面兄弟的高度
     * @param moveSize 负值
     * @param neighbor
     * @param move
     */
    void moveTopToBottom(int moveSize,TextView neighbor,TextView move){
        LayoutParams movelp = (LayoutParams) move.getLayoutParams();
        if(move.getHeight() - moveSize > 0){//是否可移动
            // 减少自己高度
            movelp.height = movelp.height-moveSize;//如此这般发现宽度没有变化
            move.setLayoutParams(movelp);
            Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.height+" real width:"+move.getWidth()+" moveSize:"+moveSize);
        }else{//自己移除
            moveSize = move.getHeight();//等于当前剩余高度
            Log.d(TAG,"reset :"+" moveSize:"+moveSize+" space:"+movelp.height);
            removeView(move);
            nowList.remove(move);
            resetLeftRight();
        }
        //移动自己的Y坐标
        move.setTranslationY(moveSize);
        //增加兄弟高度
        LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
        olp.height = olp.height + moveSize;
        neighbor.setLayoutParams(olp);
        Log.d(TAG,"neighbor :"+neighbor.getY()+" space width:"+olp.height+" real width:"+neighbor.getHeight()+" moveSize:"+moveSize);
    }

    /**列，向上移动
     * 当前view向上移动，向上改变（移动）自己的上边，减少兄弟的高度
     * @param moveSize 为负值
     * @param neighbor 上面兄弟
     * @param move
     */
    void moveTopToTop(int moveSize,TextView neighbor,TextView move){
        moveSize = Math.abs(moveSize);
        if(neighbor.getHeight() - moveSize > 0){//是否可移动
            //减少兄弟高度
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            olp.height = olp.height - moveSize;
            neighbor.setLayoutParams(olp);
            Log.d(TAG,"neighbor :"+neighbor.getY()+" space width:"+olp.height+" real width:"+neighbor.getHeight()+" moveSize:"+moveSize);
        }else{//兄弟移除
            LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
            moveSize = neighbor.getHeight();//等于当前剩余高度
            Log.d(TAG,"reset :"+" moveSize:"+moveSize+" space:"+olp.height);
            removeView(neighbor);
            nowList.remove(neighbor);
            resetLeftRight();
        }
        //增加当前高度
        LayoutParams movelp = (LayoutParams) move.getLayoutParams();
        movelp.height = movelp.height+moveSize;//如此这般发现宽度没有变化
        move.setLayoutParams(movelp);
        move.setTranslationY(-moveSize);
        Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.width+" real width:"+move.getWidth()+" moveSize:"+moveSize);
    }

    /**列，向上移动
     * 当前view向上移动，向上改变（移动）兄弟的上边，减少自己的高度
     * @param moveSize 为负值
     * @param neighbor 下面兄弟
     * @param move
     */
    void moveBottomToTop(int moveSize,TextView neighbor,TextView move){
        moveSize = Math.abs(moveSize);
        LayoutParams movelp = (LayoutParams) move.getLayoutParams();
        if(move.getHeight() - moveSize > 0){//是否可移动
            // 减少自己高度
            movelp.height = movelp.height-moveSize;//如此这般发现宽度没有变化
            move.setLayoutParams(movelp);
            Log.d(TAG,"move :"+move.getX()+" space width:"+movelp.height+" real width:"+move.getWidth()+" moveSize:"+moveSize);
        }else{//自己移除
            moveSize = move.getHeight();//等于当前剩余高度
            Log.d(TAG,"reset :"+" moveSize:"+moveSize+" space:"+movelp.height);
            removeView(move);
            nowList.remove(move);
            resetLeftRight();
        }
        //增加兄弟高度
        LayoutParams olp = (LayoutParams) neighbor.getLayoutParams();
        olp.height = olp.height + moveSize;
        neighbor.setLayoutParams(olp);
        neighbor.setTranslationY(-moveSize);
        Log.d(TAG,"neighbor :"+neighbor.getY()+" space width:"+olp.height+" real width:"+neighbor.getHeight()+" moveSize:"+moveSize);
    }

    /**
     * 重置moveView前后两个View的值
     */
    void resetLeftRight(){
        int size = nowList.size();
        if(moveTextView == null)
            return;
        for(int i=0;i<size;i++){
            if(nowList.get(i).equals(moveTextView)){
                pastTextView = i==0? null:nowList.get(i-1);
                nextTextView = i==size-1? null:nowList.get(i+1);
            }
        }
    }
    void removeView(TextView view){
        //子View数目
        LinearLayout childLinear = (LinearLayout) getChildAt(pointLayout);
        childLinear.removeView(view);
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