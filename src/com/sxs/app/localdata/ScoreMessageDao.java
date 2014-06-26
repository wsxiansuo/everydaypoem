package com.sxs.app.localdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScoreMessageDao {
	private Context context;
	private DatabaseHelper helper;
	/**
	 * �ڹ��췽��������봫�ݽ���һ�������Ķ���
	 * 
	 * @param context
	 */
	public ScoreMessageDao(Context context) {
		this.context = context;
		// ���췽��ʵ�������ݿ�򿪵İ�����
		helper = new DatabaseHelper(context);
	}

	/**
	 * �����ݿ����һ����¼
	 */
	public void insertScore(String score,String type, String date) {
//		Log.i(date,weight);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into "+DatabaseHelper.SCORE_TABLE_NAME+"(score,type,date) values (?,?,?)",
				new Object[] { score,type, date });
			// ��Լ��Դ���ر����ݿ�
		db.close();
	}
	
	public List<Map<String, String>> findAllScores(){
		List<Map<String, String>> persons = null;  
        SQLiteDatabase db = helper.getReadableDatabase();  
        Cursor cursor = db.query(DatabaseHelper.SCORE_TABLE_NAME, null, null, null, null, null, "score desc");  
        if(cursor != null){  
            persons = new ArrayList<Map<String, String>>();  
            int i = 0;
            while(cursor.moveToNext()){  
            	Map<String, String> map = new HashMap<String, String>();  
            	map.put("id", cursor.getString(cursor.getColumnIndex("id")));  
            	map.put("score", cursor.getString(cursor.getColumnIndex("score")) + "("+cursor.getString(cursor.getColumnIndex("type"))+")"); 
            	map.put("date", cursor.getString(cursor.getColumnIndex("date"))); 
            	i++;
                map.put("num", i+""); 
                persons.add(map);  
            }  
        }  
        return persons;  
	}
	/**
	 * ɾ����������
	 */
	public void delete()
	{
		SQLiteDatabase db = helper.getReadableDatabase();  
		db.delete(DatabaseHelper.SCORE_TABLE_NAME, null, null);
	}
}
