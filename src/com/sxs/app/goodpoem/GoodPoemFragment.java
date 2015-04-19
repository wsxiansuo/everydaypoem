package com.sxs.app.goodpoem;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BaseFragment;
import com.sxs.app.common.MyApplication;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.everydaypoem.PMBooksMainActivity;
import com.sxs.app.everydaypoem.R;

/**
 * 好诗词主页面
 * @author xiansuo
 */
public class GoodPoemFragment extends BaseFragment {
	@ViewInject(R.id.refresh_listview)				ListView  listLV;
	@ViewInject(R.id.tv_no_data)					TextView  noDataTV;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getInjectedView(inflater, R.layout.fragment_goodpoem);
		initConment();
		return view;
	}
	public void initConment() {  		
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),   
				MyApplication.getInstance().getDbManager().queryPoemTypes("999"),   
                R.layout.pm_good_list_item,   
                new String[]{"num","title","count"},   
                new int[]{R.id.pm_good_list_num_btn,R.id.pm_good_list_text,R.id.pm_good_list_count});  
		listLV.setAdapter(adapter);
		listLV.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString("type","2");
				bundle.putString("pid", ((Map)arg0.getItemAtPosition(arg2)).get("id").toString());
				bundle.putString("title", (MapStringUtil.getStr(((Map)arg0.getItemAtPosition(arg2)).get("title"))));
				getBaseActivity().openActivity(PMBooksMainActivity.class, bundle);
			}
        });
    }  
}
