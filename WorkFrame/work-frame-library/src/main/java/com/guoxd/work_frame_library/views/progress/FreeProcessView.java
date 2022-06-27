package com.guoxd.work_frame_library.views.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.guoxd.work_frame_library.R;

/**
 * Created by emma on 2021/10/15.
 */

/** * 自定义自由的直形状ProgressBar.
 * 思路，由多个View组成
 * */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FreeProcessView extends View {
	Context mContext;
	float density=0f;

	Picture bgPicture;
	int progressStart;
	int textHeight=0;
//	指示图片
	Bitmap tagBitmap;
	String TAG = getClass().getName();
//			进度值
	int P_MIN=0,P_MAX=0,LIMIT=0;
//	段数,单段长度
	int limitSize;
	/** * 组件的宽，高 */
	private int width, height;
	/** * 进度条最大值和当前进度值 */
	private int progress=0;
	private int sp_4,sp_2;
//	方向
	int orientation = LinearLayout.VERTICAL;

	int paddingLeft,textPadding,progressWidth;
	int top,bottom,left,right;
//组件背景色，必须设置
	int bgColor=0;


	/** * 绘制弧线的画笔 */
	private Paint progressPaint;
	/** * 绘制文字的画笔 */
	private Paint textPaint;

	public FreeProcessView(Context context) {
		this(context,null);
	}

	public FreeProcessView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public FreeProcessView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
	}

	private void getColors(){
		if(bgColor!=0)
			return;
		Drawable background = getBackground();
		//background包括color和Drawable,这里分开取值
		if (background instanceof ColorDrawable) {
			ColorDrawable colordDrawable = (ColorDrawable) background;
			bgColor =  colordDrawable.getColor();
		} else {
			bgColor = Color.WHITE;
		}
	}

	private void init() {
		density = mContext.getResources().getDisplayMetrics().density;
		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		textPaint = new Paint();
		textPaint.setAntiAlias(true);

		progressWidth=getSize(15);

		tagBitmap = BitmapFactory.decodeResource(mContext.getResources(),  android.R.drawable.star_big_off);
		sp_4 = getSize(4);
		sp_2 = getSize(2);
	}

	public void setLimit(int min,int max){
		P_MIN = min;
		P_MAX = max;
		initLimitSize();
		invalidate();
	}

	private void initLimitSize() {
		int size = P_MAX-P_MIN;
		if(size==0)
			return;
		if(orientation == LinearLayout.HORIZONTAL){
			limitSize = width / size;
		}else {
			limitSize = (bottom-top) / size;
		}
		textPadding = getSize(10);
	}
	private void recode(){
			//注意：关闭硬件加速
			setLayerType(LAYER_TYPE_SOFTWARE,null);
//			mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.you);
			bgPicture = new Picture();

//		text
			// min
			textPaint.setColor(Color.parseColor("#8F9FB3"));
			textPaint.setTextSize(getSize(11));
//		String text = (int) (100 * progress / max) + "";

			// 计算文字高度
			Rect textBounds = new Rect();
			textPaint.getTextBounds("8", 0, 1, textBounds);
			textHeight = (int)textBounds.height();
//		canvas.drawText(text, centerX - textLen / 2, centerY - circleRadius
//				/ 10 + h1 / 2, textPaint);
			String text = "最大:" + P_MAX;
			float textLen = textPaint.measureText(text);
//
			Canvas canvas = bgPicture.beginRecording((int) textLen, height);
			canvas.drawText("最小:" + P_MIN,  paddingLeft+left,  top+textHeight, textPaint);
			canvas.drawText(text,  paddingLeft+left, bottom - textHeight, textPaint);
//		bg
			progressPaint.setColor(Color.YELLOW);
			progressStart = (int)(paddingLeft+left+textLen+textPadding);
			canvas.drawRect(progressStart,top,progressStart+progressWidth,bottom,progressPaint);
			bgPicture.endRecording();
			Log.i(TAG,"draw create bgPicture");

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if ((width == 0 || height == 0 )&& (getMeasuredWidth()!=0 && getMeasuredHeight()!=0)) {
			width = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
			height = getMeasuredHeight()-getPaddingTop()-getPaddingBottom();
			top=getPaddingTop();
			bottom = getMeasuredHeight()-getPaddingBottom();
			paddingLeft = getSize(15);
			left = getPaddingLeft()+paddingLeft;
			right = getMeasuredWidth()-getPaddingRight();
			initLimitSize();
			Log.i(TAG,"onMeasure size");
			getColors();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i(TAG,"onDraw limitSize:"+limitSize);
//		canvas.drawPicture(bgPicture);
//		canvas.drawPicture(mPicture,new RectF(0, 0, 300, 300));
// min
		textPaint.setColor(Color.parseColor("#8F9FB3"));
		textPaint.setTextSize(getSize(11));
//		String text = (int) (100 * progress / max) + "";

		// 计算文字高度
		Rect textBounds = new Rect();
		textPaint.getTextBounds("8", 0, 1, textBounds);
		textHeight = (int)textBounds.height();
//		canvas.drawText(text, centerX - textLen / 2, centerY - circleRadius
//				/ 10 + h1 / 2, textPaint);
		String text = "最大:" + P_MAX;
		float textLen = textPaint.measureText(text);

		canvas.drawText("最小:" + P_MIN,  paddingLeft+left,  top+textHeight, textPaint);
		canvas.drawText(text,  paddingLeft+left, (int)(bottom - 0.5*textHeight), textPaint);
//		bg
		progressPaint.setStyle(Paint.Style.FILL);
		progressPaint.setColor(Color.parseColor("#f4f6fc"));//<solid android:color="#fff4f6fc" />
		progressStart = (int)(paddingLeft+left+textLen+textPadding);
		canvas.drawRect(progressStart,top,progressStart+progressWidth,bottom,progressPaint);

//		limite
		progressPaint.setColor(Color.RED);
		int limitTop = (int)(top+limitSize*(LIMIT-P_MIN));
		canvas.drawRect(progressStart,top,progressStart+progressWidth,limitTop,progressPaint);
		textPaint.setColor(Color.RED);

		canvas.drawText("限制:"+LIMIT,paddingLeft+left, top+textHeight+getSize(10), textPaint);

		progressPaint.setStyle(Paint.Style.STROKE);
		progressPaint.setStrokeWidth(sp_4);
		progressPaint.setColor(bgColor);//<solid android:color="#fff4f6fc" />
		progressStart = (int)(paddingLeft+left+textLen+textPadding);
		canvas.drawRoundRect(progressStart-sp_2,top-sp_2,progressStart+progressWidth+sp_2,bottom+sp_2,getSize(4),getSize(4),progressPaint);

//		progress
		int progressHeight = getLimitProgress();
		if(tagBitmap!=null) { // 指定图片绘制区域(左上角的四分之一)
			Rect src = new Rect(0, 0, tagBitmap.getWidth(), tagBitmap.getHeight());
			// 指定图片在屏幕上显示的区域(原图大小)
			int bitmapLeft = (int) (progressStart + 0.7 * progressWidth);
			Rect dst = new Rect(bitmapLeft, progressHeight - getSize(8),
					bitmapLeft + getSize(30), progressHeight + getSize(8));
			canvas.drawBitmap(tagBitmap, src, dst, progressPaint);

			textPaint.setColor(Color.WHITE);
			if (progress >= 100) {
				canvas.drawText("" + progress, bitmapLeft + getSize(8), (int) (progressHeight + 0.5 * textHeight), textPaint);
			} else {
				canvas.drawText("" + progress, bitmapLeft + getSize(11), (int) (progressHeight + 0.5 * textHeight), textPaint);
			}
		}
	}
	private int checkProgressLimit(int pro){
		if(pro<P_MIN)
			return P_MIN;
		if(pro>P_MAX)
			return P_MAX;
		return pro;
	}

	private int getLimitProgress() {
		return (int) (limitSize*(progress-P_MIN))+top;
	}

	ValueAnimator  limitAnimator,progressAnimtor;
	int animtorTime = 150;
	public int getLimit(){
		return LIMIT;
	}
	public void setLimit( int limitValue) {
		final int limit = checkProgressLimit(limitValue);
		if(limit == LIMIT)
			return;
		if(limitAnimator!=null && limitAnimator.isRunning()){
			limitAnimator.cancel();
		}
		limitAnimator = ValueAnimator.ofInt(LIMIT, limit);
		limitAnimator.setDuration(animtorTime);
		limitAnimator.setInterpolator(new DecelerateInterpolator());
		limitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int current = (int)animation.getAnimatedValue();
				if(LIMIT !=current){
					LIMIT = current;
					postInvalidate();
				}
			}
		});
		limitAnimator.start();
	}

	// 动画切换进度值(异步)
	public void setProgress(int proValue) {
		final int pro = checkProgressLimit(proValue);
		if(pro == progress)
			return;
		if(progressAnimtor!=null && progressAnimtor.isRunning()){
			progressAnimtor.cancel();
		}
		progressAnimtor = ValueAnimator.ofInt(progress, pro);
		progressAnimtor.setDuration(animtorTime);
		progressAnimtor.setInterpolator(new DecelerateInterpolator());
		progressAnimtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int current = (int)animation.getAnimatedValue();
				if(progress !=current){
					progress = current;
					postInvalidate();
				}
			}
		});
		progressAnimtor.start();
	}

	public int getProgress(){
		return progress;
	}

	// 直接设置进度值（同步）
	public void setProgressSync(int progress) {
		this.progress = progress;
		invalidate();
	}
	public void setTagBitmap(@DrawableRes int picRes){//R.drawable.icon_free_process
		tagBitmap = BitmapFactory.decodeResource(mContext.getResources(),  picRes);
		invalidate();
	}

	private  int getSize(int dp){
		return (int)(dp*density);
	}


	public  Bitmap getBitmapFromDrawable( @DrawableRes int drawableId) {
		Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);

			return bitmap;
		} else {
			throw new IllegalArgumentException("unsupported drawable type");
		}
	}
}