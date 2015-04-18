package com.sxs.app.goodpoem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sxs.app.common.BaseFragment;
import com.sxs.app.everydaypoem.R;

/**
 * 好诗词主页面
 * @author xiansuo
 */
public class GoodPoemFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getInjectedView(inflater, R.layout.fragment_goodpoem);
		return view;
	}
}
