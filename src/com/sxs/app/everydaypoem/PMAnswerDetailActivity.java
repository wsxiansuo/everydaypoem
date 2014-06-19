package com.sxs.app.everydaypoem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.common.ClearEditText;
import com.sxs.app.data.DBManager;
import com.sxs.app.data.MapStringUtil;

public class PMAnswerDetailActivity extends Activity implements  View.OnTouchListener , android.view.GestureDetector.OnGestureListener{

	@ViewInject(R.id.ab_pm_answer_detail_activity) 		private BaseActionBar titleBar; 
	@ViewInject(R.id.tv_answer_text) 		private TextView questionText; 
	@ViewInject(R.id.tv_answer_text1) 		private TextView messageText; 
	
	@ViewInject(R.id.et_answer_text1) 		private ClearEditText et1; 
	@ViewInject(R.id.et_answer_text2) 		private ClearEditText et2; 
	@ViewInject(R.id.et_answer_text3) 		private ClearEditText et3; 
	@ViewInject(R.id.rl_pm_answer_view) 	private ScrollView swipeView; 
	
	
	private DBManager mgr;  
	private String type = "1";
	List<Map<String, String>> listData;
	private String level = "0";
	private int currentIndex = 0;
	private Boolean isQuestion = false;//是否是考试
	private Boolean isQuestionMode = true;//是否是答题模式
	private Map<String, String> map;
	private GestureDetector detector;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_answer_detail);
		ViewUtils.inject(this);
		detector= new GestureDetector(this);  
		questionText.setOnTouchListener((OnTouchListener) this);  
		swipeView.setOnTouchListener((OnTouchListener) this);   
        //initConment();
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(Math.abs(e1.getY() - e2.getY()) > 150)return true;
		if(e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 50 )  
	    {  
			currentIndex++;
 			nextQuestion();
	    }  
	    else if(e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 50 )  
	    {  
	        currentIndex--;
	 		nextQuestion(); 
	    }    
		return true;
	} 
	
	@OnClick(R.id.bt_answer_btn)
	public void onServiceLinkClick(View v)
	{
		InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if(map != null)
		{
			String answer =  MapStringUtil.getStr(map.get("answer"));
			String[] list = answer.trim().split("\\|");
			Boolean isError = false;
			if(list.length == 1){
				if( et1.getText().toString().trim().equals(list[0].trim())){
				}
				else
				{
					isError = true;
				}
				
			}else if(list.length == 2){
				if( et1.getText().toString().trim().equals(list[0].trim()) && 
					et2.getText().toString().trim().equals(list[1].trim())){
				}
				else
				{
					isError = true;
				}
				
			}else if(list.length == 3){
				if( et1.getText().toString().trim().equals(list[0].trim()) && 
					et2.getText().toString().trim().equals(list[1].trim()) &&
					et3.getText().toString().trim().equals(list[2].trim())){
				}
				else
				{
					isError = true;
				}
			}
			if(isError){
				setMessageShow("回答错误！ 正确：" + answer.replace("|", " ; "),true);
			}
			else
			{
				setMessageShow("回答正确！",false);
			}
		}
	}
	
	private void setMessageShow(String message , Boolean isError){
		messageText.setTextColor(getResources().getColor(isError?R.color.color_red:R.color.color_green));
		messageText.setText(message);
	}
	
	public void initConment() {  
		if(isQuestion)
		{
			
		}
		else
		{
			listData = mgr.queryQuestionList(level);
			currentIndex = 0;
			nextQuestion();
		}
		
    }  
	private void nextQuestion(){
		if(listData == null || listData.size() == 0 || currentIndex < 0)return;
		if(currentIndex < listData.size()){
			map = listData.get(currentIndex);
			titleBar.setTitle(MapStringUtil.getStr(map.get("num")) + "/" + listData.size());
			setMessageShow("",false);
			if(isQuestionMode)
			{
				String question = MapStringUtil.getStr(map.get("content"));
				int count = MapStringUtil.stringFind(question, "（）");
				Log.i("count", count+"");
				if(count == 1){
					et1.setText("");
					et1.setVisibility(View.VISIBLE);
					et2.setVisibility(View.GONE);
					et3.setVisibility(View.GONE);
				}
				else if(count == 2){
					et1.setText("");
					et2.setText("");
					et1.setVisibility(View.VISIBLE);
					et2.setVisibility(View.VISIBLE);
					et3.setVisibility(View.GONE);
				}else if(count == 3){
					et1.setText("");
					et2.setText("");
					et3.setText("");
					et1.setVisibility(View.VISIBLE);
					et2.setVisibility(View.VISIBLE);
					et3.setVisibility(View.VISIBLE);
				}
				question = question.replace("（）", "（          ）");
				questionText.setText(question);
			}
			else
			{
				
			}
		}
	}
	
	
	
	@Override       
    protected void onResume() {         
        super.onResume();        
        Intent intent=getIntent();
        type = intent.getStringExtra("type"); 
        if(type != null) {      
        }   
        else
        {
        	type = "1";
        }
        level = "0";
		if(type.equals("1") || type.equals("4"))	
		{
			level = "0";
		}
		else if(type.equals("2") || type.equals("5"))
		{
			level = "3";
		}
		else if(type.equals("3") || type.equals("6"))
		{
			level = "4";
		}
		isQuestion = !(type.equals("1") || type.equals("2") || type.equals("3"));
		//初始化DBManager  
        mgr = new DBManager(this);  
        initConment();
    } 

	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //应用的最后一个Activity关闭时应释放DB  
	        mgr.closeDB();
	    }

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub 
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub  
	        return true; 
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			return this.detector.onTouchEvent(event);
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return this.detector.onTouchEvent(event);
		}
		
}

