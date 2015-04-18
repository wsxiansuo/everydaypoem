package com.sxs.app.everydaypoem;

import com.sxs.app.everydaypoem.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
public class SplashActivity extends Activity {   


    private final int SPLASH_DISPLAY_LENGHT = 1500; //�ӳ�����

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
         @Override
         public void run() {
             Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
             SplashActivity.this.startActivity(mainIntent);
             SplashActivity.this.finish();
         }

        }, SPLASH_DISPLAY_LENGHT);
    }
}
