package com.sxs.app.data;

import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Map;
  
import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
import android.util.Log;
  
public class DBManager {   
	
    private SQLiteDatabase db;  
    public DBManager(Context context) {  
        db = (new DBHelper(context)).openDatabase();  
    }  
    public List<Map<String, String>> queryPoemTypes(String id) {   
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();   
        Cursor c = get_types_by_id(id);  
        int i = 0;
        while (c.moveToNext()) {
        	Map<String, String> map = new HashMap<String, String>();  
            map.put("id", c.getString(c.getColumnIndex("id"))); 
            map.put("title", c.getString(c.getColumnIndex("name"))); 
            map.put("pid", c.getString(c.getColumnIndex("pid"))); 
            map.put("count", c.getString(c.getColumnIndex("count")) + "首"); 
            i++;
            map.put("num", i+""); 
            listData.add(map);  
        }  
        c.close();  
        return listData;  
    }  
  
      
    /** 
     * query all persons, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM sxs_poem_types", null);  
        return c;  
    }  
    public Cursor get_types_by_id(String id)
    {
    	return db.query("sxs_poem_types", null, "pid=?", new String[]{id} ,null, null, null);
    }
    
    
    
    public List<Map<String, String>> queryPoemList(String type_id) {   
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();   
        Cursor c = db.query("sxs_poem_list", null, "typeid like ?", new String[]{"%*"+type_id+"*%"} ,null, null, null); 
        int i = 0;
        while (c.moveToNext()) {
        	Map<String, String> map = new HashMap<String, String>();  
            map.put("id", c.getString(c.getColumnIndex("id"))); 
            map.put("title", c.getString(c.getColumnIndex("title"))); 
            map.put("content", c.getString(c.getColumnIndex("content"))); 
            map.put("translate", c.getString(c.getColumnIndex("translate")));
            map.put("author", c.getString(c.getColumnIndex("author")));
            i++;
            map.put("num", i+""); 
            listData.add(map);  
        }  
        c.close();  
        //Log.i("count:", type_id + " -- : -- " + listData.size());
        return listData;  
    }  
    
    public List<Map<String, String>> queryBooksList() {  
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();   
        Cursor c = db.rawQuery("SELECT * FROM sxs_books", null); 
        int i = 0;
        while (c.moveToNext()) {
        	Map<String, String> map = new HashMap<String, String>();  
            map.put("id", c.getString(c.getColumnIndex("id"))); 
            map.put("title", c.getString(c.getColumnIndex("type"))); 
            map.put("content", c.getString(c.getColumnIndex("content"))); 
            i++;
            map.put("num", i+""); 
            listData.add(map);  
        }  
        c.close();  
        return listData;  
    }  
    
    public List<Map<String, String>> queryAuthorsList() {  
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();   
        Cursor c = db.rawQuery("SELECT * FROM sxs_authors", null); 
        int i = 0;
        while (c.moveToNext()) {
        	Map<String, String> map = new HashMap<String, String>();  
            map.put("id", c.getString(c.getColumnIndex("id"))); 
            map.put("title", c.getString(c.getColumnIndex("name"))); 
            map.put("content", c.getString(c.getColumnIndex("detail"))); 
            map.put("author", c.getString(c.getColumnIndex("date")));  
            i++;
            map.put("num", i+""); 
            listData.add(map);  
        }  
        c.close();  
        return listData;  
    } 
    
    /**
     * 根据首字母获取分类
     * @param key
     * @return
     */
    public List<Map<String, String>> queryKeyPoemList(String key,Boolean keySerch) {   
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
        Cursor c = null;
        if(keySerch)
        {
        	c = db.rawQuery("SELECT * FROM sxs_poem_list where key=?", new String[]{key}); 
        }
        else
        {
        	c = db.query("sxs_poem_list", null, "title like ? or content like ? or author like ?", new String[]{"%"+key+"%","%"+key+"%","%"+key+"%"} ,null, null, null);
        }
        
        int i = 0;
        while (c.moveToNext()) {
        	Map<String, String> map = new HashMap<String, String>();  
            map.put("id", c.getString(c.getColumnIndex("id"))); 
            map.put("title", c.getString(c.getColumnIndex("title"))); 
            map.put("content", c.getString(c.getColumnIndex("content"))); 
            map.put("translate", c.getString(c.getColumnIndex("translate")));
            map.put("author", c.getString(c.getColumnIndex("author")));
            i++;
            map.put("num", i+"");
            listData.add(map);  
        }  
        c.close();  
        //Log.i("count:", type_id + " -- : -- " + listData.size());
        return listData;  
    } 
    
//    private void updatePoem(String id , String key , int i)
//    {
//    	key = MapStringUtil.getZiMu(key);
//    	Log.i("key : ", i + " -- " + key);
//    	db.execSQL("update sxs_poem_list set key=? where id=?", 
//    			  new Object[]{key,id}); 
//    }
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  