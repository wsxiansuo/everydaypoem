package com.sxs.app.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sxs.app.common.BaseFragment;
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
}
