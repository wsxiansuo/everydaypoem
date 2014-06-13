package com.sxs.app.everydaypoem;

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
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.data.DBManager;
import com.sxs.app.data.UserDataModel;


public class PMBooksMainActivity extends Activity {

	@ViewInject(R.id.lv_pm_book_list) 		private ListView listView; 
	@ViewInject(R.id.ab_pm_good_activity) 		private BaseActionBar bar;
	
	private String type;//1:古典2：好诗词3：诗人
	private String pid;
	private String title;
	private DBManager mgr;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_book_main);
		ViewUtils.inject(this);
	}
	
	private List<Map<String, String>> getData()
	{
		List<Map<String, String>> listData = null;
		if(type.equals("1"))
		{
			listData = mgr.queryBooksList();
		}
		else if(type.equals("2"))
		{
			listData = mgr.queryPoemList(pid);
		}
		else if(type.equals("3"))
		{
			listData = mgr.queryAuthorsList();
		}
		return listData;	
	}
	
	public void initConment() { 
		
		bar.setTitle(title);
		List<Map<String, String>> data = getData();
		if(data == null)return;
		SimpleAdapter adapter = new SimpleAdapter(this,   
				data, 
                R.layout.pm_good_list_item,   
                new String[]{"num","title","author"},   
                new int[]{R.id.pm_good_list_num_btn,R.id.pm_good_list_text,R.id.pm_good_list_count});  
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = null;
				if( type.equals("2") )
				{
					UserDataModel.instance().poem = (Map)arg0.getItemAtPosition(arg2); 
					intent = new Intent(PMBooksMainActivity.this, PMGoodPoemDetailActivity.class);
				}
				else
				{
					UserDataModel.instance().book = (Map)arg0.getItemAtPosition(arg2); 
					intent = new Intent(PMBooksMainActivity.this, PMBooksDetailActivity.class);
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
        });
    }  
	
	@Override       
    protected void onResume() {         
        super.onResume();        
        Intent intent=getIntent();
        type = intent.getStringExtra("type"); 
        pid = intent.getStringExtra("pid"); 
        title = intent.getStringExtra("title"); 
        title = title==null ? "" : title;
        if(type != null) {      
        }   
        else
        {
        	type = "1";
        }
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
}