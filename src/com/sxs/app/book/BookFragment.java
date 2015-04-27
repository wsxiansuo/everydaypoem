package com.sxs.app.book;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sxs.app.common.BaseFragment;
import com.sxs.app.component.IconTextView;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.everydaypoem.PMBooksMainActivity;
import com.sxs.app.everydaypoem.R;

/**
 * 古典文集页面
 * @author xiansuo
 */
public class BookFragment extends BaseFragment {
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getInjectedView(inflater, R.layout.fragment_book);
		return view;
	}
	@OnClick(R.id.user_icon_text_book)
	public void bookClick(View v){
	    gotoJump("1", "古典藏经阁");
	}
	@OnClick(R.id.user_icon_text_shiren)
	public void shirenClick(View v){
	    gotoJump("3", "诗词名人录");
	}
	
	private void gotoJump(String type, String title){
	    Bundle bundle = new Bundle();
        bundle.putString("type",type);
        bundle.putString("title", title);
        getBaseActivity().openActivity(PMBooksMainActivity.class, bundle);
	}
}
