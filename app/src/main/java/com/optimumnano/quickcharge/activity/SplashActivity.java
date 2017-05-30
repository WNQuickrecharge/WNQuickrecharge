package com.optimumnano.quickcharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.login.LoginActivity;
import com.optimumnano.quickcharge.base.BaseActivity;

/**
 * Created by mfwn on 2017/5/28.
 */

public class SplashActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (mHelper.isLogined()){
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }, 1000);
        }else {
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}
