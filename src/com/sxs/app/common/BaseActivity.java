package com.sxs.app.common;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

/**
 * 说明： 基础activity
 * 作者： xssong
 */

public class BaseActivity extends FragmentActivity{
    private static final String TAG = "BaseActivity";
    
    @Override
     protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
          //添加Activity到堆栈
        MyApplication.getInstance().addActivity(this);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //结束Activity&从栈中移除该Activity
        MyApplication.getInstance().finishActivity(this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); 
    }
    
    
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        //重新恢复调用处理
    }
    protected void closeSoftKeyboard(){
        View view = this.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**.
     * 通过类名启动Activity
     * @param pClass class类
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }
    /**
     * 通过类名启动Activity，并且含有Bundle数据
     * 
     * @param pClass clasee
     * @param pBundle bundle
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 通过Action启动Activity
     * @param pAction 动作
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     * 
     * @param pAction 动作
     * @param pBundle 数据
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }
    public void showToast(String tip){
        Toast t = Toast.makeText(BaseActivity.this, tip, Toast.LENGTH_SHORT);
        t.show();
    }
    public void showLongToast(String tip){
        Toast t = Toast.makeText(BaseActivity.this, tip, Toast.LENGTH_LONG);
        t.show();
    }
    
    protected void logTime(String title){
        Log.i(title, new Date().getTime() + "");
    }
    
    public String getMetaDataValue(String name, String def) {
        String value = getMetaDataValue(name);
        return (value == null) ? def : value;
    }

    protected String getMetaDataValue(String name) {
        Object value = null;
        PackageManager packageManager = this.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            Log.e(TAG, "The name '" + name + "' is not defined in the manifest file's meta data.");
        }
        return value.toString();
    }
}
