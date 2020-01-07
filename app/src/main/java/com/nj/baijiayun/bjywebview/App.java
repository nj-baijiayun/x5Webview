package com.nj.baijiayun.bjywebview;

import android.app.Application;
import android.util.Log;
import com.nj.baijiayun.lib_bjywebview.BJYWebViewUtils;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("initX5 = ","time = " +System.currentTimeMillis());
        BJYWebViewUtils.initX5Core(getApplicationContext());

    }
}
