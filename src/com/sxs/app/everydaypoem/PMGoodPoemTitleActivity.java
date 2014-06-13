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
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.data.DBManager;
import com.sxs.app.data.MapStringUtil;


public class PMGoodPoemTitleActivity extends Activity {

	@ViewInject(R.id.lv_pm_book_list) 		private ListView listView; 
	@ViewInject(R.id.ab_pm_good_activity) 		private BaseActionBar bar;
	
	private DBManager mgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_book_main);
		ViewUtils.inject(this);

		//初始化DBManager  
        mgr = new DBManager(this);
        bar.setTitle("中华好诗词");
        initConment();
        
	}
	
	public void initConment() {  		
		SimpleAdapter adapter = new SimpleAdapter(this,   
				mgr.queryPoemTypes("999"),   
                R.layout.pm_good_list_item,   
                new String[]{"num","title","count"},   
                new int[]{R.id.pm_good_list_num_btn,R.id.pm_good_list_text,R.id.pm_good_list_count});  
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PMGoodPoemTitleActivity.this, PMBooksMainActivity.class);
				intent.putExtra("type","2");
				intent.putExtra("pid", ((Map)arg0.getItemAtPosition(arg2)).get("id").toString() );
				intent.putExtra("title", (MapStringUtil.getStr(((Map)arg0.getItemAtPosition(arg2)).get("title"))));
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
        });
    }  
	
	
	

	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //应用的最后一个Activity关闭时应释放DB  
	        mgr.closeDB();  
	    }  
}