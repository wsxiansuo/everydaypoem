package com.sxs.app.everydaypoem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.common.ClearEditText;
import com.sxs.app.data.DBManager;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.localdata.ScoreMessageDao;

public class PMChengyuDetailActivity extends Activity implements  View.OnTouchListener , android.view.GestureDetector.OnGestureListener{

	@ViewInject(R.id.ab_pm_answer_detail_activity) 		private BaseActionBar titleBar; 
	@ViewInject(R.id.tv_answer_text) 		private TextView questionText; 
	@ViewInject(R.id.tv_answer_text1) 		private TextView messageText; 
	@ViewInject(R.id.et_answer_text1) 		private ClearEditText et1; 
	@ViewInject(R.id.rl_pm_answer_view) 	private ScrollView swipeView; 
	@ViewInject(R.id.bt_answer_btn) 		private Button submitBtn; 
	
	private ScoreMessageDao scoreDao;
	private DBManager mgr;  
	private String type = "1";
	List<Map<String, String>> listData;
	private int currentIndex = 0;
	private Map<String, String> map;
	private GestureDetector detector;
	private int score = 0;
	private TimeCount time;
	private long timepoint;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_answer_detail);
		ViewUtils.inject(this);
		detector= new GestureDetector(this);  
		questionText.setOnTouchListener((OnTouchListener) this);  
		swipeView.setOnTouchListener((OnTouchListener) this);   
		titleBar.setReturnBtnOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showBackDialog();
			}
		});
		scoreDao = new ScoreMessageDao(this);
        //initConment();
	}
	private void showBackDialog(){
		 AlertDialog.Builder builder = new AlertDialog.Builder(
				PMChengyuDetailActivity.this);
         builder.setTitle("提示");
         builder.setMessage("您当前正在考试中，还未交卷，确定要放弃吗！");
         builder.setIcon(R.drawable.ic_launcher);
         builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

             @Override
            public void onClick(DialogInterface dialog, int which) {
                 // TODO Auto-generated method stub
            	 dialog.dismiss();
                 finish();
           }
         });
         builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

             @Override
            public void onClick(DialogInterface dialog, int which) {
                 // TODO Auto-generated method stub
                dialog.dismiss();
           }
         });
         
         builder.show();
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
		if(getCurrentFocus()!=null)  
        {  
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))  
            .hideSoftInputFromWindow(getCurrentFocus()  
                    .getWindowToken(),  
                    InputMethodManager.HIDE_NOT_ALWAYS);   
        }  
		if(map != null)
		{
			String answer =  MapStringUtil.getStr(map.get("answer"));
			Boolean isError = false;
			if( et1.getText().toString().trim().equals(answer.trim())){
			}
			else
			{
				isError = true;
			}
			if(isError){
				setMessageShow("回答错误！ ",true);
			}
			else
			{
				setMessageShow("回答正确！",false);
				score++;
				new Handler().postDelayed(new Runnable(){
	                @Override
	                public void run(){
	                	currentIndex++;
		        		nextQuestion();
		                }
		        }, 600);
			}
			if(map != null){
				map.put("isOver", "over");
				submitBtn.setEnabled(false);
			}
		}
	}
	
	private void setMessageShow(String message , Boolean isError){
		messageText.setTextColor(getResources().getColor(isError?R.color.color_red:R.color.color_green));
		messageText.setText(message);
	}
	private void getList(int count){
		if(listData != null)
		{
			if(listData.size() < count)
			{
				return;
			}
			else
			{
				int len = listData.size() - 1;
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for(int i =0;i<count ;i++)
				{
					int index = (int)Math.round(Math.random()*len); 
					Map<String, String> map = listData.get(index);
					map.put("num", (i+1)+"");		
					list.add(map);
					listData.remove(index);
					len--;
				}	
				listData = list;
			}
		}
	}
	public void initConment() {  
		listData = mgr.queryQuestionList("5");
		currentIndex = 0;
		score = 0;
		if(time == null){
			time = new TimeCount(60000 * 30, 1000);//构造CountDownTimer对象
			time.start();
			titleBar.setTitle(MapStringUtil.getTimeStr(60000 * 30));
		}
		getList(20);
		nextQuestion();
    }  
	private void nextQuestion(){
		if(currentIndex < 0)return;
		if(listData == null || listData.size() == 0){
			submitBtn.setVisibility(View.GONE);
			et1.setVisibility(View.GONE);
			showMyDialog("对不起！","当前题库暂无数据，您可以先到其它题库逛逛！");
			return;
		}
		if(currentIndex < listData.size()){
			map = listData.get(currentIndex);
			setMessageShow("",false);
			String question = MapStringUtil.getStr(map.get("content"));
			question = question.replace("()", "（）");
			int count = MapStringUtil.stringFind(question, "（）");
			et1.setText("");	
			question = question.replace("（）", "（          ）");
			if(map != null){
				questionText.setText("["+MapStringUtil.getStr(map.get("num")) + "-" + listData.size() + "]. " + question);
				if(map.get("isOver") != null) {
					submitBtn.setEnabled(false);
					setMessageShow("此题已经回答完毕，不能重复答题！",false);
				}
				else{
					submitBtn.setEnabled(true);
				}
			}
			else
			{
				questionText.setText(question);//questionText.setText(Html.fromHtml(textStr));
			}
		}
	}
	
	
	
	@Override       
    protected void onResume() {         
        super.onResume();  
        if(time != null)
        {
        	time = new TimeCount(timepoint, 1000);//构造CountDownTimer对象
			time.start();
			titleBar.setTitle(MapStringUtil.getTimeStr(timepoint));
        	return;
        }
        Intent intent=getIntent();
        type = intent.getStringExtra("type"); 
        if(type != null) {      
        }   
        else
        {
        	type = "7";
        }
		//初始化DBManager  
        mgr = new DBManager(this);  
        initConment();
    } 
	 @Override
     protected void onStop() {
             // TODO Auto-generated method stub
             super.onStop();
             if (!isAppOnForeground()) {
            	 if(time != null)
            	 {
            		 time.cancel();
            	 }
             }
     }
	 /**
     * 程序是否在前台运行
     * @return
     */
    public boolean isAppOnForeground() { 
        // Returns a list of application processes that are running on the device 
    	ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    	String packageName = getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses(); 
        if (appProcesses == null) return false; 
        
        for (RunningAppProcessInfo appProcess : appProcesses) { 
            // The name of the process that this object is associated with. 
            if (appProcess.processName.equals(packageName) 
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) { 
                return true; 
            } 
        } 
        
        return false; 
    } 
    private void showMyDialog(String title , String message){
    	AlertDialog.Builder builder = new AlertDialog.Builder(
                PMChengyuDetailActivity.this);
		 builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("知道了！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
       });
       builder.create().show();
    }
	    @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)  {  
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
	        	showBackDialog();
	        }  
	
	        return super.onKeyDown(keyCode, event);  
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
		
		class TimeCount extends CountDownTimer {
			public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
			}
			@Override
			public void onFinish() {//计时完毕时触发
				long allTime = 	60000 * 30 - timepoint;
				int scores = 0;
				String stype = "趣味成语";
				scores = score * 5;
				if(scores > 0){
					scoreDao.insertScore(scores+"", stype, MapStringUtil.getTimeStr(allTime));
				}
				showMyDialog(scores >= 90 ? "恭喜您！" : "很遗憾！","本次考试得分：" + scores + "分，"+ (scores >= 90 ? "很赞哦，希望您能继续保持下去！"
							:"不给力啊，还是先回去好好修炼吧！"));
			}
			@Override
			public void onTick(long millisUntilFinished){//计时过程显示
				timepoint = millisUntilFinished;
				titleBar.setTitle(MapStringUtil.getTimeStr(millisUntilFinished));
			}
		}
}


