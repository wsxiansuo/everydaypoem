package com.sxs.app.everydaypoem;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.data.UserDataModel;


public class PMGoodPoemDetailActivity extends Activity {

	@ViewInject(R.id.ab_pm_good_detail_activity) 		private BaseActionBar bar; 
	@ViewInject(R.id.tv_good_poem_text)					private TextView text;
	@ViewInject(R.id.tv_good_poem_web)				private WebView web;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_good_detail);
		ViewUtils.inject(this);
		
		web.setWebViewClient(new WebViewClient(){
			   @Override
			   public boolean shouldOverrideUrlLoading(WebView view,String openUrl) {
			       return true;
			   }
			}); 
		WebSettings setting = web.getSettings();
		setting.setJavaScriptEnabled(false); 
		setting.setSupportZoom(false); 
		setting.setTextSize(TextSize.LARGER);
		
		if( UserDataModel.instance().poem != null){

	        bar.setTitle(MapStringUtil.getStr(UserDataModel.instance().poem.get("title")));
	        text.setText("×÷Õß£º" + MapStringUtil.getStr(UserDataModel.instance().poem.get("author")));
	        String str = "<p>"+MapStringUtil.getStr(UserDataModel.instance().poem.get("content"))+"</p>";
	        String translate = MapStringUtil.getStr(UserDataModel.instance().poem.get("translate"));
	        if(translate.length() > 0)
	        {
	        	str += translate;
	        }
	        web.loadData(str.replace("<p>", "<p style=\"line-height:180%; color: #4a4a4a\">") , "text/html; charset=UTF-8", null);
		}
	}
	
 
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}