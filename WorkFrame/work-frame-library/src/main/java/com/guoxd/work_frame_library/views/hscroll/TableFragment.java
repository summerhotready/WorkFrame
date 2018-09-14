package com.guoxd.work_frame_library.views.hscroll;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.guoxd.work_frame_library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
public class TableFragment extends Fragment implements OnScrollChangedListener {

    private String TAG="TableFragment";

    private SaleData dataMap;

    private int currentSelectItem=-1;

    private GestureDetector mGestureDetector = null;
    private HScrollView mItemRoom = null;
    private ScrollView mVertical = null;
    private HScrollView mTimeItem = null;
    private List<LinearLayout> mLinears;
    private LinearLayout mContain = null;
    private LinearLayout HLayout;
//    private TablePopDialog popTable;

    private int topLine;
    private int viewHeight;
    private int titleLine;
    private int columnLine;

    Activity activity;

    private int scrollYPoint;

    /*public TableFragment(){}
    public TableFragment(Activity activity){
        this.activity = activity;
    }
*/
    TextPaint mTextPaint;
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

//        viewHeight = activity.getResources().getDisplayMetrics().heightPixels;
        scrollYPoint=0;

        mGestureDetector = new GestureDetector(mGestureListener);
//        popTable = new TablePopDialog(getActivity(), new TablePopDialog.OnPopClickListener() {
//            @Override
//            public void onClick() {
//                if(currentSelectItem>=0){
//                    //new Intent
//                    Bundle bundle = new Bundle();
//                    for(int i=0;i<dataMap.cloumnSize();i++){
//                        bundle.putString(dataMap.getTitlePosition(i),
//                                dataMap.getCellsItem(currentSelectItem,i));
//                    }
//                    Intent intent = new Intent(activity, ShowSaleDateActivity.class);
//                    intent.putExtra("data",bundle);
//                    startActivity(intent);
//                    Log.i(TAG,"click:"+currentSelectItem);
//                }
//            }
//        });


        Log.i(TAG,"onCreate"+ (dataMap== null ? true:false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView"+ (dataMap== null ? true:false));
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        mContain = (LinearLayout)view.findViewById(R.id.contain);
//        popTable.setDownDeviation(topLine,titleLine,scrollYPoint);
        makeItems();
        return view;
    }



    public SaleData getDataMap() {
        return dataMap;
    }

    public void setDataMap(SaleData dataMap,int viewHeight) {
        this.dataMap = dataMap;
        this.titleLine = dataMap.getTitleHeight();
        this.columnLine = dataMap.getCellHeight();
        this.viewHeight = viewHeight;
        topLine = titleLine;
        Log.i(TAG,"viewHeight:"+viewHeight);
    }

    public void UpdateDataMap(SaleData dataMap,int titleHeight,int viewHeight) {
        this.dataMap = dataMap;
        this.titleLine = dataMap.getTitleHeight();
        this.columnLine = dataMap.getCellHeight();
        topLine = titleHeight;
        this.viewHeight = viewHeight;
        Log.i(TAG,"viewHeight:"+viewHeight);
    }

    public void updateView(){
        if(dataMap == null){
            return;
        }
        makeItems();
    };

    @TargetApi(Build.VERSION_CODES.M)
    private void makeItems(){
        int sceanWidth=0,containWidth=0;
        int sceanHeight = titleLine;
        Log.i(TAG,"makeItems start"+ (dataMap== null ? true:false)+" sceanHeight:"+sceanHeight);
        if(dataMap == null)
            return;
        if(mGestureDetector == null){
            return;
        }
        mItemRoom = new HScrollView(activity, mGestureDetector);
        mItemRoom.setListener(this);

        if(dataMap != null) {
            for (int i = 0; i < dataMap.cloumnSize(); i++) {
                sceanWidth+=dataMap.getTitleColumnWidth(i) ;
            }
        }
        containWidth = sceanWidth;
        Log.i(TAG,"containWidth:"+containWidth);
        //Table Cantion
        LinearLayout VLayout = new LinearLayout(activity);
        VLayout.setOrientation(LinearLayout.VERTICAL);
        VLayout.setBackgroundColor(activity.getResources().getColor(android.R.color.darker_gray));
        List<TextView> mListItems;
        mLinears = new ArrayList<>();
        if(dataMap.getCells() != null){
            for(int i = 0;i < dataMap.RowSize();i++){
                mListItems = new ArrayList<>();
                HLayout = new LinearLayout(activity);
                sceanHeight+=dataMap.getCellHeight() ;
                for(int j = 0;j < dataMap.cloumnSize();j++){
                    TextView item = new TextView(activity);
                    item.setText(dataMap.getCellsItem(i,j));
                    item.setGravity(Gravity.CENTER);
                    item.setTextSize(dataMap.getCellTextSize());
                    item.setTextColor(activity.getResources().getColor(dataMap.getCellTextClolor(j)));
                    item.setBackgroundResource(android.R.drawable.arrow_up_float);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dataMap.getTitleColumnWidth(j) , columnLine);
                    HLayout.addView(item, params);
                    mListItems.add(item);
                }
                HLayout.setTag(i+"");
                HLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = Integer.valueOf(v.getTag().toString());
                        if(currentSelectItem >=0){
                            mLinears.get(currentSelectItem).setBackgroundResource(android.R.color.transparent);
                        }
                        mLinears.get(position).setBackgroundResource(android.R.drawable.arrow_up_float);
                        currentSelectItem = position;
                        Log.i(TAG,"position:"+position);
//                        popTable.showPop(mLinears.get(position),"展开吗？");
                    }
                });
                mLinears.add(HLayout);
                VLayout.addView(HLayout);
            }
            containWidth = sceanWidth;
            if(sceanHeight<viewHeight){
                HLayout = new LinearLayout(activity);
                TextView item = new TextView(activity);
                item.setBackgroundResource(android.R.drawable.arrow_up_float);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(containWidth, viewHeight - sceanHeight);
                HLayout.addView(item, params);
                HLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        popTable.dismiss();
                    }
                });
                mLinears.add(HLayout);
                VLayout.addView(HLayout);
            }
            mItemRoom.addView(VLayout);
        }else{
            sceanHeight = activity.getResources().getDisplayMetrics().heightPixels;
            HLayout = new LinearLayout(activity);
            HLayout.setBackgroundColor(activity.getResources().getColor(android.R.color.darker_gray));
            TextView item = new TextView(activity);
            item.setBackgroundResource(android.R.drawable.arrow_up_float);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(containWidth, viewHeight-titleLine);
            HLayout.addView(item, params);
            HLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popTable.dismiss();
                }
            });
            mLinears.add(HLayout);
            VLayout.addView(HLayout);
            mItemRoom.addView(VLayout);
        }




        mVertical = new ScrollView(activity);
        LinearLayout hlayout = new LinearLayout(activity);
        LinearLayout vlayout = new LinearLayout(activity);
        //left
        if(dataMap.RowSize() > 0) {
            vlayout.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < dataMap.RowSize(); i++) {
                TextView item = new TextView(activity);
                item.setBackgroundResource(android.R.drawable.arrow_up_float);
                item.setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
                item.setText((i + 1) + "");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dataMap.leftTitleWidth() , columnLine);
                vlayout.addView(item, params);
            }
            if(sceanHeight<viewHeight){
                TextView item = new TextView(activity);
                item.setBackgroundResource(android.R.drawable.arrow_up_float);
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(dataMap.leftTitleWidth(), viewHeight - sceanHeight);
                vlayout.addView(item, params);
            }
            hlayout.addView(vlayout);
        }
        hlayout.addView(mItemRoom);

        mVertical.addView(hlayout);

        LinearLayout tophlayout = new LinearLayout(activity);
        mTimeItem = new HScrollView(activity, mGestureDetector);
        mTimeItem.setListener(this);


        //title
        if(dataMap != null) {
            LinearLayout layout = new LinearLayout(activity);
            for (int i = 0; i < dataMap.cloumnSize(); i++) {
                TextView item = new TextView(activity);
                item.setText(dataMap.getTitlePosition(i));
                item.setGravity(Gravity.CENTER);
                item.setTextSize(dataMap.getTitleTextSize());
                mTextPaint = item.getPaint();
                mTextPaint.setFakeBoldText(true);
                item.setTextColor(activity.getResources().getColor(dataMap.getTitleTextClolor(i)));
                item.setBackgroundResource(android.R.drawable.arrow_up_float);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dataMap.getTitleColumnWidth(i), titleLine);
                layout.addView(item, params);
            }
            mTimeItem.addView(layout);
        }
        //title_first
        if (dataMap.RowSize() > 0) {
            TextView item = new TextView(activity);
            item.setBackgroundResource(android.R.drawable.arrow_up_float);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dataMap.leftTitleWidth(),titleLine);
            tophlayout.addView(item,params);
        }

        tophlayout.addView(mTimeItem);

        Log.i(TAG,"sceanWidth:"+sceanWidth);

        mContain.removeAllViews();
        mContain.addView(tophlayout);
        mContain.addView(mVertical);

        mVertical.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                popTable.dismiss();
                scrollYPoint = scrollY;
//                popTable.setDownDeviation(topLine,titleLine,scrollYPoint);
                Log.i(TAG,"onScrollChanged distanceX:"+scrollX+" distanceY:"+scrollY+" OldX:"+oldScrollX+" OldY:"+oldScrollY);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
//        popTable.dismiss();
    }

    //方向
    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i(TAG,"GestureDetector distanceX:"+distanceX+" distanceY:"+distanceY);
            if(Math.abs(distanceX) > Math.abs(distanceY)) {
                return true;
            }
            return false;
        }

    };
    //横向滚动更新
    @Override
    public void onScrollChanged(HScrollView scroll, int x, int y, int oldx,
                                int oldy) {
//        popTable.dismiss();
//        Log.i(TAG,"onScrollChanged distanceX:"+x+" distanceY:"+y+" OldX:"+oldx+" OldY:"+oldy);
        if(scroll == mItemRoom){
            mTimeItem.scrollTo(x, y);
        }else if(scroll == mTimeItem){
            mItemRoom.scrollTo(x, y);
        }

    }
}
