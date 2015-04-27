package com.sxs.app.everydaypoem;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.localdata.ScoreMessageDao;


public class PMScoreListActivity extends Activity {

	@ViewInject(R.id.lv_pm_score_list) 		private ListView listView; 

	private ScoreMessageDao scoreDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_score_list);
		ViewUtils.inject(this);
		scoreDao = new ScoreMessageDao(this);
        initConment();
	}
	
	public void initConment() {  		
		SimpleAdapter adapter = new SimpleAdapter(this,   
				scoreDao.findAllScores(),   
                R.layout.pm_score_list_item,   
                new String[]{"num","score","date"},   
                new int[]{R.id.pm_good_list_num_btn,R.id.pm_good_list_text,R.id.pm_good_list_count});  
        listView.setAdapter(adapter);
    }  
	
	
	

	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //Ӧ�õ����һ��Activity�ر�ʱӦ�ͷ�DB  
	    }  
}