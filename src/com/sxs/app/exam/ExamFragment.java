package com.sxs.app.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sxs.app.common.BaseFragment;
import com.sxs.app.everydaypoem.R;

/**
 * 考试页面
 * @author xiansuo
 */
public class ExamFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getInjectedView(inflater, R.layout.fragment_exam);
		return view;
	}
}
