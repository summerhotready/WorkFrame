package com.guoxd.workframe.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guoxd.workframe.R;


/**RecyclerView‘s Decoration
 * AUTHOR: The_Android
 * DATE: 2018/4/12
 */
@TargetApi(21)
public class MyItemDecoration extends RecyclerView.ItemDecoration {
	public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
	public static final int VERTICAL = LinearLayout.VERTICAL;

	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

	private Drawable mDivider;

	/**
	 * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
	 */
	private int mOrientation;

	private final Rect mBounds = new Rect();
	private Paint mPaint;

	/**
	 * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
	 * {@link LinearLayoutManager}.
	 *
	 * @param context     Current context, it will be used to access resources.
	 * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
	 */
	public MyItemDecoration(Context context, int orientation) {
		final TypedArray a = context.obtainStyledAttributes(ATTRS);
//		mDivider = a.getDrawable(0);
		mDivider = context.getDrawable(R.drawable.line);
//		mPaint = new Paint();
//		mPaint.setColor(Color.RED);
		a.recycle();
		setOrientation(orientation);
	}

	/**
	 * Sets the orientation for this divider. This should be called if
	 * {@link RecyclerView.LayoutManager} changes orientation.
	 *
	 * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
	 */
	public void setOrientation(int orientation) {
		if (orientation != HORIZONTAL && orientation != VERTICAL) {
			throw new IllegalArgumentException(
					"Invalid orientation. It should be either HORIZONTAL or VERTICAL");
		}
		mOrientation = orientation;
	}

	/**
	 * Sets the {@link Drawable} for this divider.
	 *
	 * @param drawable Drawable that should be used as a divider.
	 */
	public void setDrawable(@NonNull Drawable drawable) {
		if (drawable == null) {
			throw new IllegalArgumentException("Drawable cannot be null.");
		}
		mDivider = drawable;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		if (parent.getLayoutManager() == null) {
			return;
		}
		if (mOrientation == VERTICAL) {
			drawVertical(c, parent);
		} else {
			drawHorizontal(c, parent);
		}
	}

	@SuppressLint("NewApi")
	private void drawVertical(Canvas canvas, RecyclerView parent) {
		canvas.save();
		final int left;
		final int right;
		if (parent.getClipToPadding()) {
			left = parent.getPaddingLeft();
			right = parent.getWidth() - parent.getPaddingRight();
			canvas.clipRect(left, parent.getPaddingTop(), right,
					parent.getHeight() - parent.getPaddingBottom());
		} else {
			left = 0;
			right = parent.getWidth();
		}

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			parent.getDecoratedBoundsWithMargins(child, mBounds);
			final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
			final int top = bottom - mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
//			LogUtil.e("Bound", "left:" + left + "top:" + top + "right:" + right + "bottom:" + bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	@SuppressLint("NewApi")
	private void drawHorizontal(Canvas canvas, RecyclerView parent) {
		canvas.save();
		final int top;
		final int bottom;
		if (parent.getClipToPadding()) {
			top = parent.getPaddingTop();
			bottom = parent.getHeight() - parent.getPaddingBottom();
			canvas.clipRect(parent.getPaddingLeft(), top,
					parent.getWidth() - parent.getPaddingRight(), bottom);
		} else {
			top = 0;
			bottom = parent.getHeight();
		}

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
			final int right = mBounds.right + Math.round(child.getTranslationX());
			final int left = right - mDivider.getIntrinsicWidth();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
							   RecyclerView.State state) {
		if (mOrientation == VERTICAL) {
//			outRect.set(0, 0, 0, 0);
			outRect.left = 0;
			outRect.top = 0;
			outRect.right = 0;
			outRect.bottom = mDivider.getIntrinsicHeight();
		} else {
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		}
	}
}
