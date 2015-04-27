package com.sxs.app.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BaseFragment;
import com.sxs.app.everydaypoem.PMAnswerDetailActivity;
import com.sxs.app.everydaypoem.PMScoreListActivity;
import com.sxs.app.everydaypoem.R;

/**
 * 考试页面
 * @author xiansuo
 */
public class ExamFragment extends BaseFragment {
    
    @ViewInject(R.id.lv_pm_question_list)
    private ListView listView;
    private String[] menus = { "小学练习题", "初中练习题", "高中练习题", "小学考试", "初中考试",
            "高中考试", "考试成绩单" };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getInjectedView(inflater, R.layout.fragment_exam);
		initConment();
		return view;
	}
	public void initConment() {
        SimpleAdapter adapter = new SimpleAdapter(
                getBaseContext(),
                getMainMenuList(),
                R.layout.pm_good_list_item,
                new String[] { "id", "title" },
                new int[] { R.id.pm_good_list_num_btn, R.id.pm_good_list_text, });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                String stype = ((Map) arg0.getItemAtPosition(arg2)).get("id")
                        .toString();
                if (stype.equals("7")) {
                    getBaseActivity().openActivity(PMScoreListActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", stype);
                    getBaseActivity().openActivity(PMAnswerDetailActivity.class, bundle);
                }
            }
        });
    }

    /**
     * 获取主的列表
     * 
     * @return
     */
    private List<Map<String, String>> getMainMenuList() {
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
        for (int i = 0; i < menus.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", (i + 1) + "");
            map.put("title", menus[i]);
            listData.add(map);
        }
        return listData;
    }
}
