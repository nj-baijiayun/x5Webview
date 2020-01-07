package com.nj.baijiayun.lib_bjywebview;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;


public class BJYWebViewUtils {

    public static  void initX5Core(Context context) {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
                Log.d("initX5ViewInitFinished","time = " +System.currentTimeMillis());
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.d("app", " onCoreInitFinished" );
                Log.d("initX5CoreInitFinished","time = " +System.currentTimeMillis());
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context,  cb);
    }



}
