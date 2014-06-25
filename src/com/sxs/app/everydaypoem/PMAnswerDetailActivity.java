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
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.data.DBManager;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.localdata.ScoreMessageDao;

public class PMAnswerDetailActivity extends Activity implements  View.OnTouchListener , OnClickListener , android.view.GestureDetector.OnGestureListener{

	@ViewInject(R.id.ab_pm_answer_detail_activity) 		private BaseActionBar titleBar; 
	@ViewInject(R.id.tv_answer_text) 		private TextView questionText; 
	@ViewInject(R.id.tv_answer_text1) 		private TextView messageText; 
	
	@ViewInject(R.id.et_answer_text1) 		private LinearLayout et1; 
	@ViewInject(R.id.et_answer_text2) 		private LinearLayout et2; 
	@ViewInject(R.id.et_answer_text3) 		private LinearLayout et3; 
	@ViewInject(R.id.et_answer_text4) 		private LinearLayout et4; 
	@ViewInject(R.id.et_answer_text5) 		private LinearLayout et5; 
	
	@ViewInject(R.id.rl_pm_answer_view) 	private ScrollView swipeView; 
	
	private static String keywords = "����һ��ɽ�޷����������кλ���ʱ������������Ϊˮ��֪�Ҵ��ӵ�����ļ���������ʫ����������֮ȥ�������°���ǧ�����ɿո���ʮ���������ϼ����������ǰ���������Ŷ�������ζ�������ͷ��ѩͬ���ƿ��ؿ���ƽ�������ܴ��ʹ�������ʯ�����Ȼ��С�ݰ�����������˼��·����Ī���ظ�Ӧ����ɫ���������������Ǳ���";
	
	private ScoreMessageDao scoreDao;
	private DBManager mgr;  
	private String type = "1";
	List<Map<String, String>> listData;
	private String level = "0";
	private int currentIndex = 0;
	private Boolean isQuestion = false;//�Ƿ��ǿ���
	private Boolean isQuestionMode = true;//�Ƿ��Ǵ���ģʽ
	private Map<String, String> map;
	private GestureDetector detector;
	private int score = 0;
	private TimeCount time;
	private long timepoint;
	private DisplayMetrics dm;//��Ļ�ֱ�������
	private int btnWidth;
	
	private String[] list;
	private String[] answerList;
	private int idIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_answer_detail);
		ViewUtils.inject(this);
		detector= new GestureDetector(this);  
		questionText.setOnTouchListener((OnTouchListener) this);  
		swipeView.setOnTouchListener((OnTouchListener) this);   
		titleBar.setRightBtnOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isQuestion){
					if(time != null)time.onFinish();
					return;
				}
				titleBar.setRightTitle(isQuestionMode?"����ģʽ":"����ģʽ");
				if(getCurrentFocus()!=null)  
	            {  
	                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))  
	                .hideSoftInputFromWindow(getCurrentFocus()  
	                        .getWindowToken(),  
	                        InputMethodManager.HIDE_NOT_ALWAYS);   
	            }  
				if(isQuestionMode)
				{
					et1.setVisibility(View.GONE);
					et2.setVisibility(View.GONE);
					et3.setVisibility(View.GONE);
					et4.setVisibility(View.GONE);
					et5.setVisibility(View.GONE);
				}
				else
				{
					et1.setVisibility(View.VISIBLE);
					et2.setVisibility(View.VISIBLE);
					et3.setVisibility(View.VISIBLE);
					et4.setVisibility(View.VISIBLE);
					et5.setVisibility(View.VISIBLE);
				}
				isQuestionMode = !isQuestionMode;
				nextQuestion();
			}
		});
		titleBar.setReturnBtnOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showBackDialog();
			}
		});
		scoreDao = new ScoreMessageDao(this);


		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		btnWidth = (dm.widthPixels - 200)/8;
        //initConment();
	}
	private void showBackDialog(){
		if(isQuestion)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PMAnswerDetailActivity.this);
             builder.setTitle("��ʾ");
             builder.setMessage("����ǰ���ڿ����У���δ����ȷ��Ҫ������");
             builder.setIcon(R.drawable.ic_launcher);
             builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

                 @Override
                public void onClick(DialogInterface dialog, int which) {
                     // TODO Auto-generated method stub
                	 dialog.dismiss();
                     finish();
               }
             });
             builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

                 @Override
                public void onClick(DialogInterface dialog, int which) {
                     // TODO Auto-generated method stub
                    dialog.dismiss();
               }
             });
             
             builder.show();
		}
		else
		{
			finish();
		}
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(e1 == null)return true;
		if(Math.abs(e1.getY() - e2.getY()) > 150)return true;
		if(e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 50 )  
	    {  
			if(isQuestion && map != null){
				map.put("isOver", "over");
			}
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
		listData = mgr.queryQuestionList(level);
		titleBar.setRightTitle(isQuestion ? "����":"����ģʽ");
		currentIndex = 0;
		if(isQuestion)
		{
			score = 0;
			if(time == null){
				time = new TimeCount(60000 * 30, 1000);//����CountDownTimer����
				time.start();
				titleBar.setTitle(MapStringUtil.getTimeStr(60000 * 30));
			}
			if(level.equals("0"))
			{
				getList(20);
			}else if(level.equals("3")){
				getList(25);
			}else if(level.equals("4")){
				getList(50);
			}
			nextQuestion();
		}
		else
		{
			nextQuestion();
		}
		
    }  
	private void nextQuestion(){
		if(currentIndex < 0)return;
		if(listData == null || listData.size() == 0){
			showMyDialog("�Բ���","��ǰ����������ݣ������ֻ��¼�����г��ֵĴ����������ȵ���������䣡");
			return;
		}
		if(currentIndex < listData.size()){
			map = listData.get(currentIndex);
			if(!isQuestion)
				titleBar.setTitle(MapStringUtil.getStr(map.get("num")) + "/" + listData.size());
			setMessageShow("",false);
			String answer =  MapStringUtil.getStr(map.get("answer"));
			list = answer.trim().split("\\|");
			String question = MapStringUtil.getStr(map.get("content"));
			question = question.replace("()", "����");
			int count = MapStringUtil.stringFind(question, "����");
			if(isQuestionMode)
			{
				idIndex = 0;
				et1.removeAllViews();
				et2.removeAllViews();
				et3.removeAllViews();
				et4.removeAllViews();
				et5.removeAllViews();
				if(count == 1){
					et1.setVisibility(View.VISIBLE);
					et2.setVisibility(View.GONE);
					et3.setVisibility(View.GONE);
					addButtonGroups(list[0],et1,"1",false);
				}
				else if(count == 2){
					et1.setVisibility(View.VISIBLE);
					et2.setVisibility(View.VISIBLE);
					et3.setVisibility(View.GONE);
					addButtonGroups(list[0],et1,"1",false);
					addButtonGroups(list[1],et2,"2",false);
				}else if(count == 3){
					et1.setVisibility(View.VISIBLE);
					et2.setVisibility(View.VISIBLE);
					et3.setVisibility(View.VISIBLE);
					addButtonGroups(list[0],et1,"1",false);
					addButtonGroups(list[1],et2,"2",false);
					addButtonGroups(list[2],et3,"3",false);
				}
				String listStr = answer.trim().replace("|", "");
				answerList = new String[listStr.length()];
				for(int k=0 ;k<listStr.length();k++){
					answerList[k] = "";
				}
				if(listStr.length() < 16){
					int len = 16 - listStr.length();
					String keys = keywords;
					for(int i =0;i<len;i++){
						int pos = (int)Math.round((keys.length()-1) * Math.random());
						String ch = keys.substring(pos, pos+1);
						if(listStr.indexOf(ch)>=0){
							keys = keys.replace(ch, "");
							i--;
							continue;
						}
						listStr += ch;
						keys = keys.replace(ch, "");
					}
				}
				String roundStr = "";
				for(int j=0;j<16;j++){
					int pos = (int)Math.round((listStr.length()-1) * Math.random());
					String ch = listStr.substring(pos, pos+1);
					roundStr += ch;
					listStr = listStr.replace(ch, "");
				}
				addButtonGroups(roundStr.substring(0, 8),et4,"4",true);
				addButtonGroups(roundStr.substring(8),et5,"5",true);
				question = question.replace("����", "��          ��");
				if(isQuestion && map != null){
					questionText.setText("["+MapStringUtil.getStr(map.get("num")) + "-" + listData.size() + "]. " + question);
					if(map.get("isOver") != null) {
						setMessageShow("�����Ѿ��ش���ϣ��ظ�������Ч��",false);
					}
				}
				else
				{
					questionText.setText(question);
				}
			}
			else
			{
				String[] qlist = question.trim().split("��");
				String textStr = "";
				if(list != null && qlist != null && qlist.length > 0 && qlist.length - 1 == list.length)
				{
					for(int i = 0; i< list.length ;i++)
					{
						textStr += qlist[i].replace("��", "��<font color=\"#ff1111\">"+list[i]+"</font>��");
					}
					textStr += qlist[qlist.length - 1];
				}else{
					textStr = question.replace("����", "��          ��");
				}
				questionText.setText(Html.fromHtml(textStr));
			}
		}
	}
	
	private void addButtonGroups(String asStr,LinearLayout view, String key, Boolean isText){
		for(int i = 0;i<asStr.length();i++){
			Button btn = (Button)LayoutInflater.from(this).inflate(isText?R.layout.layout_btn:R.layout.layout_answer_btn, null);
			btn.setId(idIndex);
			btn.setOnClickListener(this);  
			idIndex++;
			btn.setTag(key+"-"+i);
			if(isText){
				btn.setText(asStr.substring(i, i+1));
			}else{
				btn.setTextColor(Color.YELLOW);
			}
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(  
					btnWidth, btnWidth);  
			layout.setMargins(10, 0, 0, 0);  
			btn.setLayoutParams(layout);
			view.addView(btn);
		}
		if(!isText){
			TextView txt = new TextView(this);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(  
					btnWidth, btnWidth);  
			layout.setMargins(10, 0, 0, 0);  
			txt.setLayoutParams(layout);
			txt.setVisibility(View.GONE);
			txt.setId(Integer.parseInt("100" + key));
			view.addView(txt);
		}
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int btnId = arg0.getId();
		String key = arg0.getTag().toString();
		String charStr = key.substring(0, 1);
		if(charStr.equals("5") || charStr.equals("4")){
			for(int i=0;i<answerList.length;i++){
				if(answerList[i].equals("")){
					answerList[i] = ((Button)arg0).getText().toString();
					((Button)findViewById(i)).setText(answerList[i]);
					break;
				}
			}
			handlerBtnGroup();
		}else{
			answerList[btnId] = "";
			((Button)arg0).setText("");
		}
	}
	private void handlerBtnGroup(){
		int index = 0;
		int success = 0;
		for(int i=0;i<list.length;i++){
			String res = list[i];
			String ans = "";
			if(res != null && res.length() > 0){
				for(int j =0;j<res.length();j++){
					ans += answerList[index + j];
				}
			}
			int txtId = Integer.parseInt("100" + (i+1));
			TextView txt = (TextView) findViewById(txtId);
			if(ans.equals(res)){
				txt.setText(" �� ");
				txt.setTextColor(Color.GREEN);
				txt.setVisibility(View.VISIBLE);
				success++;
				if(isQuestion && map != null){
					map.put("isOver", "start");
				}
			}else if(ans.length() == res.length())
			{
				txt.setText(" X ");
				txt.setTextColor(Color.RED);
				txt.setVisibility(View.VISIBLE);
				if(isQuestion && map != null){
					map.put("isOver", "start");
				}
			}else{
				txt.setVisibility(View.GONE);
			}
			index += res.length();
		}
		if(success == list.length){
			if(isQuestion)
			{
				if(!map.get("isOver").equals("over")) {
					score++;
				}
				new Handler().postDelayed(new Runnable(){
	                @Override
	                public void run(){
	                	currentIndex++;
		        		nextQuestion();
		                }
		        }, 600);
			}
		}
	}
	
	@Override       
    protected void onResume() {         
        super.onResume();  
        if(time != null)
        {
        	time = new TimeCount(timepoint, 1000);//����CountDownTimer����
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
		isQuestion = (type.equals("4") || type.equals("5") || type.equals("6"));
		//��ʼ��DBManager  
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
     * �����Ƿ���ǰ̨����
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
                PMAnswerDetailActivity.this);
		 builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("֪���ˣ�", new DialogInterface.OnClickListener() {
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
	        //Ӧ�õ����һ��Activity�ر�ʱӦ�ͷ�DB  
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
			this.detector.onTouchEvent(event);
			return false;
		}
		
		class TimeCount extends CountDownTimer {
			public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
			}
			@Override
			public void onFinish() {//��ʱ���ʱ����
				long allTime = 	60000 * 30 - timepoint;
				int scores = 0;
				String stype = "";
				if(level.equals("0"))
				{
					scores = score * 5;
					stype = "Сѧ����";
				}else if(level.equals("3")){
					scores = score * 4;
					stype = "���п���";
				}else if(level.equals("4")){
					scores = score * 2;
					stype = "���п���";
				}
				if(scores > 0){
					scoreDao.insertScore(scores+"", stype, MapStringUtil.getTimeStr(allTime));
				}
				showMyDialog(scores >= 90 ? "��ϲ����" : "���ź���","���ο��Ե÷֣�" + scores + "�֣�"+ (scores >= 90 ? "����Ŷ��ϣ�����ܼ���������ȥ��"
							:"���������������Ȼ�ȥ�ú������ɣ�"));
			}
			@Override
			public void onTick(long millisUntilFinished){//��ʱ������ʾ
				timepoint = millisUntilFinished;
				titleBar.setTitle(MapStringUtil.getTimeStr(millisUntilFinished));
			}
		}
	
}


