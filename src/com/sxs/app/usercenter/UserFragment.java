package com.sxs.app.usercenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sxs.app.common.BaseFragment;
import com.sxs.app.everydaypoem.R;

/**
 * 个人中心页面
 * @author xiansuo
 *
 */
public class UserFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getInjectedView(inflater, R.layout.fragment_user);
		return view;
	}
}
