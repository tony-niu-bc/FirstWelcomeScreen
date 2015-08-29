package com.wzhnsc.firstwelcomescreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

public class SplashScreen extends Activity
{
    private static final int GO_HOME_ACTIVITY  = 0x01;
    private static final int GO_GUIDE_ACTIVITY = 0x02;

    // 启动画面显示时长(单位毫秒)
    private static final long SPLASH_SCREEN_DELAY = 2000;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Intent intent = null;

            switch (msg.what)
            {
            case GO_HOME_ACTIVITY:
//                intent = new Intent(SplashScreen.this, MainActivity.class);
                intent = new Intent(SplashScreen.this, GuideScreen.class);
                break;

            case GO_GUIDE_ACTIVITY:
                intent = new Intent(SplashScreen.this, GuideScreen.class);
                break;
            }

            startActivity(intent);
            finish();

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (sp.getBoolean(GuideScreen.SP_FIRST_START, true))
        {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME_ACTIVITY, SPLASH_SCREEN_DELAY);
        }
        else
        {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE_ACTIVITY, SPLASH_SCREEN_DELAY);
        }
    }
}
