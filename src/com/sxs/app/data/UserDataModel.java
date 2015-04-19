package com.sxs.app.data;

import java.util.Map;

public class UserDataModel {

	public Map<String, String> poem;
	public Map<String, String> book;
	public PoemTypeVO typeOne;
	public PoemTypeVO typeTwo;

	private static UserDataModel _instance;

	public static UserDataModel instance() {
		if (_instance == null) {
			_instance = new UserDataModel();
		}
		return _instance;
	}
}
