package com.nj.baijiayun.lib_bjywebview.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nj.baijiayun.lib_bjywebview.BJYX5WebView;
import com.nj.baijiayun.lib_bjywebview.R;

public class BJYWebViewActivity extends AppCompatActivity {

    private BJYX5WebView bjyWebView;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bjyweb_view);
        bjyWebView = findViewById(R.id.bjy_web_view);

        webUrl = getIntent().getStringExtra("url");

        bjyWebView.initSettings();
        if (webUrl != null && !"".equals(webUrl)){
            bjyWebView.loadUrl(webUrl);
        }


    }


    @Override
    public void onBackPressed() {
        if (bjyWebView.canGoBack()){
            bjyWebView.goBack();
        }else {

            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (bjyWebView!= null){
            bjyWebView.destory();
        }
        super.onDestroy();
    }

    public static void openActivity(Activity activity, String url){

        Intent intent = new Intent(activity,BJYWebViewActivity.class);
        intent.putExtra("url",url);

        activity.startActivity(intent);


    }
}
