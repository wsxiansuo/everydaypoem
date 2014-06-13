package com.sxs.app.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ListTypeView extends View {

	private float mIndexbarWidth;
	private float mIndexbarMargin;
	private float mPreviewPadding;
	private float mDensity;
	private float mScaledDensity;
	private float mAlphaRate;
	private int myWidth;
	private int myHeight;
	private int mCurrentSection = -1;
	private boolean mIsIndexing = false;
	private String mSectionsStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String[] mSections = null;
	private RectF mIndexbarRect;
	
	private String lastKey;
	
	public String getKeyValue()
	{
		return lastKey;
	}
	public ListTypeView(Context context)
	{
		super(context);
	}
	public ListTypeView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		initComponent(context,attrs);
		
	}
	public String[] getSections() {
		String[] sections = new String[mSectionsStr.length()];
		for (int i = 0; i < mSectionsStr.length(); i++)
			sections[i] = String.valueOf(mSectionsStr.charAt(i));
		return sections;
	}
	private void initComponent(Context context,AttributeSet attrs)
	{
		mSections = getSections();
		mDensity = context.getResources().getDisplayMetrics().density;
		mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		mIndexbarWidth = 20 * mDensity;
		mIndexbarMargin = 10 * mDensity;
		mPreviewPadding = 5 * mDensity;
	}
	

	@Override
	public void draw(Canvas canvas) {
		mAlphaRate = 1;
		// mAlphaRate determines the rate of opacity
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.BLACK);
		indexbarPaint.setAlpha((int) (64 * mAlphaRate));
		indexbarPaint.setAntiAlias(true);
		canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);
		
		if (mSections != null && mSections.length > 0) {
			// Preview is shown when mCurrentSection is set
			if (mCurrentSection >= 0) {
				Paint previewPaint = new Paint();
				previewPaint.setColor(Color.BLACK);
				previewPaint.setAlpha(96);
				previewPaint.setAntiAlias(true);
				previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));
				
				Paint previewTextPaint = new Paint();
				previewTextPaint.setColor(Color.BLUE);
				previewTextPaint.setAntiAlias(true);
				previewTextPaint.setTextSize(30 * mScaledDensity);
				
				//float previewTextWidth = previewTextPaint.measureText(mSections[mCurrentSection]);
				float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
				RectF previewRect = new RectF(myWidth - mIndexbarMargin - mIndexbarWidth
						, (myHeight - previewSize) / 2
						, myWidth - mIndexbarMargin
						, (myHeight - previewSize) / 2 + previewSize);
		
				canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
				canvas.drawText(mSections[mCurrentSection], previewRect.left
						, previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1, previewTextPaint);
				
			}
			else
			{
				lastKey = "A";
			}
			
			Paint indexPaint = new Paint();
			indexPaint.setColor(Color.WHITE);
			indexPaint.setAlpha((int) (255 * mAlphaRate));
			indexPaint.setAntiAlias(true);
			indexPaint.setTextSize(12 * mScaledDensity);
			
			float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
			float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
			for (int i = 0; i < mSections.length; i++) {
				float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
				canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft
						, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// If down event occurs inside index bar region, start indexing
			if (contains(ev.getX(), ev.getY())) {
				// It demonstrates that the motion event started from index bar
				mIsIndexing = true;
				// Determine which section the point is in, and move the list to that section
				mCurrentSection = getSectionByPoint(ev.getY());
				this.invalidate();
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsIndexing) {
				// If this event moves inside index bar
				if (contains(ev.getX(), ev.getY())) {
					// Determine which section the point is in, and move the list to that section
					mCurrentSection = getSectionByPoint(ev.getY());
					this.invalidate();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsIndexing) {
				mIsIndexing = false;
				if(mCurrentSection >= 0)
				{
					lastKey = mSections[mCurrentSection];
					performClick();
				}
				else
				{
					lastKey = "A";
				}
				mCurrentSection = -1;
				this.invalidate();
			}
			break;
		}
		return false;
	}
	private int getSectionByPoint(float y) {
		if (mSections == null || mSections.length == 0)
			return 0;
		if (y < mIndexbarRect.top + mIndexbarMargin)
			return 0;
		if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
			return mSections.length - 1;
		return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
	}
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		myWidth = w;
		myHeight = h;
		mIndexbarRect = new RectF(myWidth - mIndexbarMargin - mIndexbarWidth
				, mIndexbarMargin
				, w - mIndexbarMargin
				, h - mIndexbarMargin);
	}
	
	private boolean contains(float x, float y) {
		// Determine if the point is in index bar region, which includes the right margin of the bar
		return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
	}
}
