package com.sxs.app.common;

/**
 * 说明： 提供返回按钮和标题的anctionbar，可以自定义标题
 * 作者： xssong
 * 创建时间：2013-7-16 下午1:52:49
 */


import com.sxs.app.everydaypoem.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActionBar extends RelativeLayout {
	
	TextView titleTv;
	Button returnBtn;
	Button rightBtn;
	Button leftBtn;
	
	private OnClickListener returnBtnClickListener = null;
	private OnClickListener rightBtnClickListener = null;
	private OnClickListener leftBtnClickListener = null;
	
	public void setLeftBtnOnClickListener(OnClickListener listener) {
		leftBtnClickListener = listener;
		if( leftBtn != null ){
			leftBtn.setOnClickListener(listener);
		}
	}
	
	public void setReturnBtnOnClickListener(OnClickListener listener) {
		returnBtnClickListener = listener;
		if( returnBtn != null ){
			returnBtn.setOnClickListener(listener);
		}
	}
	public void setRightBtnOnClickListener( OnClickListener listener ){
		rightBtnClickListener = listener;
		if( rightBtn != null ){
			rightBtn.setOnClickListener(listener);
		}
	}
	
	public void setLeftBtnVisible( int value ){
		if( leftBtn != null){
			leftBtn.setVisibility(value);
		}
	}
	
	public void setReturnBtnVisible( int value ){
		if( returnBtn != null){
			returnBtn.setVisibility(value);
		}
	}
	
	public void setRightBtnVisible( int value ){
		if( rightBtn != null){
			rightBtn.setVisibility(value);
		}
	}
	
	public void setTitle( String title ){
		if( titleTv != null){
			titleTv.setText(title);
		}
	}
	public void setRightTitle( String title ){
		if( rightBtn != null ){
			rightBtn.setText(title);
		}
	}
	
	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		for(int i = 0 ; i < getChildCount() ; i++){
            getChildAt(i).layout(l, t, r, b);
        }
	}

	public BaseActionBar(Context context)
	{
		super(context);
	}
	public BaseActionBar(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		this.initComponent(context, attrs);
	}
	private void initComponent(Context context,AttributeSet attrs)
	{
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.common_attrs);
		LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.include_actionbar, this);
	    
	    // 标题
        titleTv = (TextView)findViewById(R.id.tv_actionbar_title);
        String title = a.getString(R.styleable.common_attrs_title);
        titleTv.setText(title);
        
        // 返回按钮
        returnBtn = (Button)findViewById(R.id.btn_actionbar_return);
        returnBtn.getBackground().setAlpha(0);
        boolean showReturn = a.getBoolean(R.styleable.common_attrs_show_return_btn, true);
        returnBtn.setVisibility( showReturn ? View.VISIBLE : View.INVISIBLE );
        if( showReturn ){
        	if (returnBtnClickListener == null){
        		returnBtnClickListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						((Activity)getContext()).onBackPressed();
					}
				};
			}
        	returnBtn.setOnClickListener(returnBtnClickListener);
        }
        // 右侧按钮
        rightBtn = (Button)findViewById(R.id.btn_action_bar_right);
        Drawable rightBtnDrawable = a.getDrawable(R.styleable.common_attrs_right_btn_selector);
        String rightTitle = a.getString(R.styleable.common_attrs_right_btn_title);
        if( rightBtnDrawable != null ){
        	rightBtn.setBackgroundDrawable(rightBtnDrawable);
        }else{
        	rightBtnDrawable = this.getResources().getDrawable(R.drawable.sl_actionbar_right_btn);
        	rightBtn.setBackgroundDrawable( rightBtnDrawable );
        }
        
        rightBtn.setText(rightTitle);
        boolean showRightBtn = a.getBoolean(R.styleable.common_attrs_show_right_btn, false);
        rightBtn.setVisibility( showRightBtn ? View.VISIBLE : View.INVISIBLE );
        if( showRightBtn && rightBtnClickListener != null ){
        	rightBtn.setOnClickListener(rightBtnClickListener);
        }
        a.recycle();
        
        // 左侧按钮
        leftBtn = (Button)findViewById(R.id.btn_action_bar_left);
        Drawable leftBtnDrawable = a.getDrawable(R.styleable.common_attrs_left_btn_selector);
        String leftTitle = a.getString(R.styleable.common_attrs_left_btn_title);
        if( leftBtnDrawable == null ){
        	leftBtnDrawable = this.getResources().getDrawable(R.drawable.sl_actionbar_right_btn);
        }
        leftBtn.setBackgroundDrawable( leftBtnDrawable );
        
        leftBtn.setText(leftTitle);
        boolean showLeftBtn = a.getBoolean(R.styleable.common_attrs_show_left_btn, false);
        leftBtn.setVisibility( showLeftBtn ? View.VISIBLE : View.GONE );
        if( showLeftBtn && leftBtnClickListener != null ){
        	leftBtn.setOnClickListener(leftBtnClickListener);
        }
        a.recycle();
	}

	protected void onFinishInflate ()
	{
		super.onFinishInflate();
		if (isInEditMode()) {
            return;
		}
	}
	
	public interface OnReturnButtonClickListener{
		abstract void onClick();
	}
}
