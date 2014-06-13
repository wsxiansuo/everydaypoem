package com.sxs.app.everydaypoem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.data.UserDataModel;


public class PMBooksDetailActivity extends Activity {

	@ViewInject(R.id.ab_pm_book_detail_activity) 		private BaseActionBar bar; 
	@ViewInject(R.id.tv_book_text)					private TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_book_detail);
		ViewUtils.inject(this);
		if( UserDataModel.instance().book != null){
	        bar.setTitle(MapStringUtil.getStr(UserDataModel.instance().book.get("title")));
	        text.setText(MapStringUtil.getStr(UserDataModel.instance().book.get("content")));
		}
	}
	
 
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}