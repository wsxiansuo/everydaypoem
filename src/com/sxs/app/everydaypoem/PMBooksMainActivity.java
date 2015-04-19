package com.sxs.app.everydaypoem;

import java.util.List;
import java.util.Map;

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
import com.sxs.app.common.BaseActivity;
import com.sxs.app.common.MyApplication;
import com.sxs.app.data.UserDataModel;

public class PMBooksMainActivity extends BaseActivity {

	@ViewInject(R.id.refresh_listview)
	private ListView listView;

	@ViewInject(R.id.ab_pm_good_activity)
	private BaseActionBar bar;

	private String type;// 1:古典2：好诗词3：诗人
	private String pid;
	private String title;
	
	private boolean isFirst = true; //第一次作数据请求

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pm_book_main);
		ViewUtils.inject(this);
	}

	private List<Map<String, String>> getData() {
		List<Map<String, String>> listData = null;
		if (type.equals("1")) {
			listData = MyApplication.getInstance().getDbManager()
					.queryBooksList();
		} else if (type.equals("2")) {
			listData = MyApplication.getInstance().getDbManager()
					.queryPoemList(pid);
		} else if (type.equals("3")) {
			listData = MyApplication.getInstance().getDbManager()
					.queryAuthorsList();
		}
		return listData;
	}

	public void initConment() {
		bar.setTitle(title);
		List<Map<String, String>> data = getData();
		if (data == null)
			return;
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.pm_good_list_item, new String[] { "num", "title",
						"author" }, new int[] { R.id.pm_good_list_num_btn,
						R.id.pm_good_list_text, R.id.pm_good_list_count });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (type.equals("2")) {
					UserDataModel.instance().poem = (Map<String, String>) arg0
							.getItemAtPosition(arg2);
					openActivity(PMGoodPoemDetailActivity.class);
				} else {
					UserDataModel.instance().book = (Map<String, String>) arg0
							.getItemAtPosition(arg2);
					openActivity(PMBooksDetailActivity.class);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isFirst){
			isFirst = false;
			Intent intent = getIntent();
			type = intent.getStringExtra("type");
			pid = intent.getStringExtra("pid");
			title = intent.getStringExtra("title");
			title = title == null ? "" : title;
			if (type != null) {
			} else {
				type = "1";
			}
			initConment();
		}
	}
}