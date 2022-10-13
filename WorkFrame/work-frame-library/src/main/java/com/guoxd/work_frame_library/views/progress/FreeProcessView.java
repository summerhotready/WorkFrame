package com.guoxd.work_frame_library.views.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.DrawableUtils;
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
	String TAG = getClass().getName();
	Context mContext;
	float density=0f;

	Picture tagPicture;
	Picture limitPicture;
	Picture bgPicture;

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
	int progressStart;
	int paddingLeft,textPadding;
	int top,bottom,left,right;
	//	文字宽高
	float textLen=0;
	int textHeight=0;
//
	int progressWidth=0;
	float progressTextSize=0.0f;

	@ColorInt
	private int defaultBGColor=Color.WHITE;
	@ColorInt
	private int defaultBaseTextColor=Color.DKGRAY;
	@ColorInt
	private int defaultProgressTextColor=Color.WHITE;
	@ColorInt
	private int defaultProgressBGColor=Color.LTGRAY;
	@ColorInt
	private int defaultLimitColor=Color.RED;

	/** * 绘制弧线的画笔 */
	private Paint progressPaint;
	/** * 绘制文字的画笔 */
	private Paint textPaint;
//初始化onDraw判断
	private boolean isFirstDraw = true;


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

//******************	属性设置
//	进度条tag
	public void setProgressWidth(int size){
		int toSize = getSize(size);
		if(progressWidth != toSize){
			progressWidth = toSize;
			if(!isFirstDraw){
				updateBgPicture();
				updateLimitPicture();
				invalidate();
			}
		}
	}
	public void setProgressTextColor(@ColorInt int color){
		if(defaultProgressTextColor != color) {
			defaultProgressTextColor = color;
			if(!isFirstDraw) {
				invalidate();
			}
		}
	}
	public void setProgressBGColor(@ColorInt int color){
		if(defaultProgressBGColor != color) {
			defaultProgressBGColor = color;
			if(!isFirstDraw){
				updateBgPicture();
				invalidate();
			}
		}
	}
//	背景相关
	public void setMaxMinLimit(int min,int max){
		P_MIN = min;
		P_MAX = max;
		initLimitSize();
		if(!isFirstDraw){
			updateBgPicture();
			invalidate();
		}
	}
	public void setBaseTextColor(@ColorInt int color){
		if(defaultBaseTextColor != color) {
			defaultBaseTextColor = color;
			if(!isFirstDraw){
				updateBgPicture();
				invalidate();
			}
		}
	}
	public void setTextSize(int size){
		float toSize =(float) getSize(size);
		if(toSize != progressTextSize){
			progressTextSize =  toSize;
			if(textPaint==null){
				textPaint = new Paint();
				textPaint.setAntiAlias(true);
			}
			textPaint.setTextSize(progressTextSize);
			if(!isFirstDraw){
				updateBgPicture();
				updateLimitPicture();
				invalidate();
			}
		}

	}
//限制相关
	public void setLimitColor(@ColorInt int color){
		if(defaultLimitColor != color) {
			defaultLimitColor = color;
			if(!isFirstDraw){
				updateLimitPicture();
				invalidate();
			}
		}
	}
//******************
	private void init() {
		Log.d(TAG,"init start");
		density = mContext.getResources().getDisplayMetrics().density;

		sp_4 = getSize(4);
		sp_2 = getSize(2);
	}
	private void initLimitSize() {
		int size = P_MAX-P_MIN+1;
		if(size==0)
			return;
//		要确保每一个等份是相等的，修正底部区间比调整份数更好
		if(orientation == LinearLayout.HORIZONTAL){
			int add = (width) % size;
			if(add>0){
				width = width-add;
			}
			limitSize = width / size;
		}else {
			int add = (bottom-top) % size;
			if(add>0)
			bottom -= add;
			limitSize = (bottom-top) / size;
		}
		textPadding = getSize(10);
	}

	private void updateTagPicture(@DrawableRes int tagRes){
		//注意：关闭硬件加速
		setLayerType(LAYER_TYPE_SOFTWARE,null);
//		缩放致高度为30
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig= Bitmap.Config.RGB_565;
		//inJustDecodeBounds为true时BitmapFactory只会解析图片的原始宽高信息，并不会真正的加载图片，获取后的尺寸存在options里。
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(mContext.getResources(),  tagRes,options);
		if(options.outWidth<=0){
			tagRes = 	 android.R.mipmap.sym_def_app_icon;
			BitmapFactory.decodeResource(mContext.getResources(), tagRes,options);
		}

		//inSampleSize采样率:inSampleSize的取值为1或2的整数倍，否则会被向下取整得到一个最接近2的整数倍。当inSampleSize=2时，采样后的图片的宽高均为原始图片宽高的1/2
		options.inSampleSize = calculateInSampleSize(options, getSize(16), getSize(30));
		// 修改为false后真实加载
		options.inJustDecodeBounds = false;
		Bitmap tagBitmap;
		Drawable d = AppCompatResources.getDrawable(mContext,tagRes);
		Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

		if(options.inSampleSize==1){
			Matrix matrix = new Matrix();
			float fy = getSize(16)/((float)bitmap.getHeight());
			matrix.postScale(fy,fy); //长和宽放大缩小的比例
			tagBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
			Log.i(TAG, "matrix:" + fy);
		}else{
			tagBitmap = bitmap;
		}
//
		tagPicture = new Picture();
//录制内容
		Canvas canvas = tagPicture.beginRecording(tagBitmap.getWidth(), tagBitmap.getHeight());
		canvas.drawBitmap(tagBitmap, 0, 0, null);
		tagPicture.endRecording();

		canvas=null;
		tagBitmap.recycle();
		tagBitmap = null;
		bitmap.recycle();
		bitmap = null;
	}
//	计算要压缩的分辨率（成倍缩小图片）
	private static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	private void updateBgPicture(){
		//注意：关闭硬件加速
		setLayerType(LAYER_TYPE_SOFTWARE,null);
		bgPicture = new Picture();

//		text
		// 最大最小值
		textPaint.setColor(defaultBaseTextColor);
		// 计算文字高度
		Rect textBounds = new Rect();
		textPaint.getTextBounds("8", 0, 1, textBounds);
		textHeight = (int)textBounds.height();
		String text = "最大:" + P_MAX;
		textLen = textPaint.measureText(text);

		Canvas canvas = bgPicture.beginRecording(width,height);
		canvas.drawText("最小:" + P_MIN,  paddingLeft+left,  top+textHeight, textPaint);
		canvas.drawText(text,  paddingLeft+left, (int)(bottom - 0.5*textHeight), textPaint);
//		进度条背景绘制
		progressPaint.setStyle(Paint.Style.FILL);
		progressPaint.setColor(defaultProgressBGColor);//<solid android:color="#fff4f6fc" />
		progressStart = (int)(paddingLeft+left+textLen+textPadding);
		canvas.drawRect(progressStart,top,progressStart+progressWidth,bottom,progressPaint);
		bgPicture.endRecording();
		canvas = null;
	}

	private void updateLimitPicture(){
		progressPaint.setStyle(Paint.Style.FILL);
		progressPaint.setColor(defaultLimitColor);
		int limitTop = (int)(top+limitSize*(LIMIT-P_MIN));

		limitPicture = new Picture();
		Canvas canvas = limitPicture.beginRecording(width,height);
		canvas.drawRect(progressStart,top,progressStart+progressWidth,limitTop,progressPaint);

		textPaint.setColor(defaultLimitColor);
		canvas.drawText("限制:"+LIMIT,paddingLeft+left, top+textHeight+getSize(10), textPaint);

		progressPaint.setStyle(Paint.Style.STROKE);
		progressPaint.setStrokeWidth(sp_4);
		progressPaint.setColor(defaultBGColor);//<solid android:color="#fff4f6fc" />
		progressStart = (int)(paddingLeft+left+textLen+textPadding);
		canvas.drawRoundRect(progressStart-sp_2,top-sp_2,progressStart+progressWidth+sp_2,bottom+sp_2,getSize(4),getSize(4),progressPaint);

		limitPicture.endRecording();
		canvas=null;
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
			initDraw();
		}
	}
	private void initDraw(){
		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		if(textPaint==null) {
			textPaint = new Paint();
			textPaint.setAntiAlias(true);
		}
		if(progressWidth==0){
			progressWidth = getSize(16);
		}
		if(progressTextSize==0){
			setTextSize(10);
		}
		initLimitSize();
		if(tagPicture==null) {
			updateTagPicture(android.R.mipmap.sym_def_app_icon);
		}
		updateBgPicture();
		updateLimitPicture();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isFirstDraw){
			isFirstDraw = false;
		}
		canvas.drawPicture(bgPicture,new Rect(0,0,width,height));
		//	限制背景绘制和文字
		canvas.drawPicture(limitPicture,new Rect(0,0,width,height));

		//		进度+游标指向
		int progressHeight = getLimitProgress();
		if(tagPicture!=null) {
			// 指定图片在屏幕上显示的区域(原图大小)
			int bitmapLeft = (int) (progressStart + 0.7 * progressWidth);
			int tagPHeight_top = tagPicture.getHeight()/2;
			//			绘制位置
			Rect dst = new Rect(bitmapLeft, progressHeight - tagPHeight_top,
					bitmapLeft + tagPicture.getWidth(), progressHeight +tagPicture.getHeight()-tagPHeight_top);
			canvas.drawPicture(tagPicture,dst);
//绘制tag进度
			textPaint.setColor(defaultProgressTextColor);
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
		if(isFirstDraw){
			LIMIT = limit;
			invalidate();
			return;
		}
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
					Log.i(TAG,"postInvalidate LIMIT:"+current);
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
		if(isFirstDraw){
			progress = pro;
			invalidate();
			return;
		}
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
					Log.i(TAG,"postInvalidate progress:"+current);
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
		Log.i(TAG,"setProgressSync");
		invalidate();
	}
//	图片为png jpg
	public void setTagPicture(@DrawableRes int picRes){//R.drawable.icon_free_process
		updateTagPicture(picRes);
		Log.i(TAG,"setTagBitmap");
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