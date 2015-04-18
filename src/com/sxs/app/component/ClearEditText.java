package com.sxs.app.component;



import com.sxs.app.everydaypoem.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class ClearEditText extends EditText implements  OnFocusChangeListener{ 
	/**
	 * ɾ����ť������
	 */
	private Drawable mClearDrawable; 
	/**
	 * �ؼ��Ƿ��н���
	 */
	private boolean hasFoucs;
	/**
	 * �Ƿ���6222 2316 1256��138 8888 8888
	 * ���п��ź͵绰������ʾ��ʽ
	 * Ĭ����ʾ���п�����ʾ��ʽ
	 */
	public boolean showType;
	/**
	 * �����绰������ʾ��ʽ
	 */
	public boolean showMobileType;

	//�Ƿ�����
	private boolean isRun = false;
	//���������
	private String inputStr = "";

	public ClearEditText(Context context) { 
		this(context, null); 
	} 

	public ClearEditText(Context context, AttributeSet attrs) { 
		//���ﹹ�췽��Ҳ����Ҫ����������ܶ����Բ�����XML���涨��
		this(context, attrs, android.R.attr.editTextStyle); 
	} 

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	private void init() { 
		//��ȡEditText��DrawableRight,����û���������Ǿ�ʹ��Ĭ�ϵ�ͼƬ
		mClearDrawable = getCompoundDrawables()[2]; 
		if (mClearDrawable == null) { 
			mClearDrawable = getResources().getDrawable(R.drawable.sl_delete); 
		} 
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight()); 
		//Ĭ����������ͼ��
		setClearIconVisible(false); 
		//���ý���ı�ļ���
		setOnFocusChangeListener(this); 
		//����������������ݷ����ı�ļ���
		addTextChangedListener(new TextWatcher() {
			/**
			 * ��������������ݷ����仯��ʱ��ص��ķ���
			 */
			@Override 
			public void onTextChanged(CharSequence s, int start, int count, 
					int after) { 
				if(hasFoucs){
					setClearIconVisible(s.length() > 0);
				}
				show(s);
			} 
			@Override 
			public void beforeTextChanged(CharSequence s, int start, int count, 
					int after) { 
			} 
			@Override 
			public void afterTextChanged(Editable s) { 
			}
		}); 
	} 


	/**
	 * ��Ϊ���ǲ���ֱ�Ӹ�EditText���õ���¼������������ü�ס���ǰ��µ�λ����ģ�����¼�
	 * �����ǰ��µ�λ�� ��  EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ�� - ͼ��Ŀ��  ��
	 * EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ��֮�����Ǿ�������ͼ�꣬��ֱ�����û�п���
	 */
	@Override 
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {
				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
						&& (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					this.setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * ��ClearEditText���㷢���仯��ʱ���ж������ַ��������������ͼ�����ʾ������
	 */
	@Override 
	public void onFocusChange(View v, boolean hasFocus) { 
		this.hasFoucs = hasFocus;
		if (hasFocus) { 
			setClearIconVisible(getText().length() > 0); 
		} else { 
			setClearIconVisible(false); 
		} 
	} 


	/**
	 * �������ͼ�����ʾ�����أ�����setCompoundDrawablesΪEditText������ȥ
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) { 
		Drawable right = visible ? mClearDrawable : null; 
		setCompoundDrawables(getCompoundDrawables()[0], 
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]); 
	} 
	
	/**
	 * �������п��ź͵绰������ʾ��ʽ
	 */
	private void show(CharSequence s){
		if(showType){//���������п��š��绰������ʾ��ʽ
			if(isRun){
				isRun = false;
				return;
			}
			isRun = true;
			inputStr = "";
			String newStr = s.toString();
			newStr = newStr.replace(" ", "");
			int index = 0;
			if(showMobileType){//�绰������ʾ��ʽ
				if((index + 3)< newStr.length()){
					inputStr += (newStr.substring(index, index + 3)+ " ");
					index += 3;
				}
			}
			while((index + 4)< newStr.length()){
				inputStr += (newStr.substring(index, index + 4)+ " ");
				index += 4;
			}
			inputStr += (newStr.substring(index, newStr.length()));
			this.setText(inputStr);
			this.setSelection(inputStr.length());
		}
	}
}