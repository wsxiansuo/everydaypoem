package com.sxs.app.everydaypoem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class PMQuestionMainActivity extends Activity {

	@ViewInject(R.id.lv_pm_question_list) 		private ListView listView; 
	private String[] menus = {"Сѧ��ϰ��","������ϰ��","������ϰ��","Сѧ����","���п���","���п���","���Գɼ���"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_question_main);
		ViewUtils.inject(this);
        initConment();
	}
	
	public void initConment() {  		
		SimpleAdapter adapter = new SimpleAdapter(this,   
				getMainMenuList(),   
                R.layout.pm_good_list_item,   
                new String[]{"id","title"},   
                new int[]{R.id.pm_good_list_num_btn,R.id.pm_good_list_text,});  
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PMQuestionMainActivity.this, PMAnswerDetailActivity.class);
				intent.putExtra("type",((Map)arg0.getItemAtPosition(arg2)).get("id").toString() );
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
        });
    }  
	
	/**
	 * ��ȡ�����б�
	 * @return
	 */
	private List<Map<String, String>> getMainMenuList()
	{
		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();   
		for(int i=0;i<menus.length;i++)
		{
			Map<String, String> map = new HashMap<String, String>();  
            map.put("id", (i+1)+""); 
            map.put("title", menus[i]); 
            listData.add(map);
		}
		return listData;
	}
	

	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //Ӧ�õ����һ��Activity�ر�ʱӦ�ͷ�DB  
	    }  
}