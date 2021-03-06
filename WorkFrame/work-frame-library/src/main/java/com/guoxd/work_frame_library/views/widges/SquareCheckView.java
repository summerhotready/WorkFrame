package com.guoxd.work_frame_library.views.widges;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.guoxd.work_frame_library.R;
import com.guoxd.work_frame_library.info.ViewOnClickListener;
import com.guoxd.work_frame_library.utils.PaintViewUtils;

/**
 * Created by guoxd on 2018/5/25.
 * checkStart's base
 * 点击变化的checkStart
 */
//square
@TargetApi(16)
public class SquareCheckView extends AppCompatCheckedTextView {
    boolean hasText=false;
    Context mContext;
    Drawable checkedDrawable,uncheckDrawable;
    int checkedColor,uncheckColor;
    public SquareCheckView(Context mContext, int mStartSize){
        super(mContext);
        this.mContext = mContext;
        initView(mStartSize);
    }

    private void initView(int mStartSize) {
        setWidth(mStartSize);
        setHeight(mStartSize);
    }

    public void setTag(String tag){
        setTag(tag);
    }

    public void setText(String text){
        if(text==null)
            return;
        setText(text,BufferType.NORMAL);
        hasText = !text.trim().equals("");
    }

    public void setDrawable(Drawable... draws){
        if(draws ==null)
            return;
        if(draws instanceof Drawable[]){
            if(draws.length>1){
                uncheckDrawable = draws[0];
            }
            if(draws.length>=2){
                checkedDrawable = draws[1];
            }
        }
    }


        /**
     *
     * @param b
     */
    public void startChecked(boolean b){
        setChecked(b);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if(uncheckDrawable !=null && checkedDrawable !=null){
            setBackground(checked? checkedDrawable:uncheckDrawable);
        }else {
            setBackgroundResource(checked ? R.drawable.start_chedked : android.R.color.transparent);
        }
        if(hasText){
            if(checkedColor !=0 && uncheckColor!=0){
                setTextColor(checked? PaintViewUtils.getColor(mContext,checkedColor)
                        :PaintViewUtils.getColor(mContext,uncheckColor));
            }else{
                setTextColor(checked ? PaintViewUtils.getColor(mContext,android.R.color.white)
                        : PaintViewUtils.getColor(mContext,android.R.color.black));
            }
        }
    }

    public void setStartViewClickListener(final ViewOnClickListener listener){
        setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               startChecked(isChecked());
               listener.onClick(isChecked(),getTag().toString());
           }
       });
    }

}
