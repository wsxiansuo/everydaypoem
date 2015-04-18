package com.sxs.app.everydaypoem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.component.ListTypeView;
import com.sxs.app.data.DBManager;
import com.sxs.app.data.UserDataModel;


public class PMPoemSearchActivity extends Activity  implements SearchView.OnQueryTextListener {

	@ViewInject(R.id.lv_pm_search_list) 		private ListView listView; 
	@ViewInject(R.id.lt_pm_search_bar) 			private ListTypeView keyView; 
	@ViewInject(R.id.search_view) 			private SearchView searchView; 
	@ViewInject(R.id.tv_no_data) 			private TextView textView; 
	
	private DBManager mgr;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_search_main);
		ViewUtils.inject(this);
		mgr = new DBManager(this);
		keyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initConment(keyView.getKeyValue(),true);
				searchView.clearFocus();
			}
		});
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(false);
        initConment("A",true);
	}

	
	public void initConment(String key , Boolean isKey) {
		List<Map<String, String>> data = mgr.queryKeyPoemList(key, isKey);
		if(data == null || data.size() == 0)
		{
			textView.setVisibility(View.VISIBLE);//显示无数据
			listView.setVisibility(View.GONE);
			return;
		}
		else{
			textView.setVisibility(View.GONE);//隐藏无数据
			listView.setVisibility(View.VISIBLE);
		}
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
				UserDataModel.instance().poem = (Map)arg0.getItemAtPosition(arg2); 
				Intent intent = new Intent(PMPoemSearchActivity.this, PMGoodPoemDetailActivity.class);
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


	 @Override
	 public boolean onQueryTextChange(String newText) {
		  if (TextUtils.isEmpty(newText)) {
		   // Clear the text filter.
			  initConment("A",true);
		  } else {
		   // Sets the initial value for the text filter.
			  initConment(newText,false);
		  }
		  return false;
	 }

	 @Override
	 public boolean onQueryTextSubmit(String query) {
	  // TODO Auto-generated method stub
		 Log.i("A -- ",query);
	  return false;
	 }
	
}