package com.sxs.app.localdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static String DATABASE_NAME = "every_day_poem.db";
	public static String SCORE_TABLE_NAME = "score_table";
	public static String MESSAGE_TABLE_NAME = "message_table";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}
	//数据库第一次创建时调用的方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + SCORE_TABLE_NAME + "(id integer primary key autoincrement, score varchar(20), type varchar(20), date varchar(20));";
		db.execSQL(sql);
		String sql1 = "CREATE TABLE " + MESSAGE_TABLE_NAME + "(id integer primary key autoincrement, content varchar(20), date  varchar(20));";
		db.execSQL(sql1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
