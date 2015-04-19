package com.sxs.app.common;

import java.util.ArrayDeque;
import java.util.Deque;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.sxs.app.data.DBManager;

/**
 * 说明： 
 * 作者： xssong
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static Deque<Activity> activityStack;
    private static MyApplication singleton;
    
    private int dbVersion = 1; //更新处理数据库
    private DBManager dbManager; //数据库管理类
    
    
    @Override
    public void onCreate(){
        super.onCreate();
        singleton=this;
    }
    // Returns the application instance
    public static MyApplication getInstance() {
        return singleton;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity){
        if (activityStack == null){
            activityStack =new ArrayDeque<Activity>();
        }
        
        String name = "";
        if (!activityStack.isEmpty()){
            name = activityStack.getLast().getComponentName().getClassName();
        }
        if (name.equals(activity.getComponentName().getClassName())){
            return;
        }
        activityStack.add(activity);
    }
    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.getLast();
        return activity;
    }
    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.getLast();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity mactivity) {
        Activity activity = mactivity;
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.toArray()[i]) {
                ((Activity) activityStack.toArray()[i]).finish();
            }
        }
        activityStack.clear();
    }
    /**
     * 退出应用程序
     */
    public void appExit(){
        try {
        	getDbManager().closeDB(); //关闭数据库
        	setDbManager(null);
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e){
            Log.i(TAG, e.getMessage(), e);
        }
    }
    /**
     * 获取数据库操作类
     * @return
     */
	public DBManager getDbManager() {
		if (dbManager == null){
			dbManager = new DBManager(getApplicationContext());
	    	if(dbManager.getDbVersion() < dbVersion){
	    		dbManager.upDatabase();  
	    		Log.i("update", "执行了数据库更新---------------");
	    	}
		}
		return dbManager;
	}
	
	public void setDbManager(DBManager value){
		this.dbManager = value;
	}
}
