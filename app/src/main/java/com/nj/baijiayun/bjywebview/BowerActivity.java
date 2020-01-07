package com.nj.baijiayun.bjywebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nj.baijiayun.lib_bjywebview.BJYX5WebView;

public class BowerActivity extends AppCompatActivity {


    private BJYX5WebView bjyWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bower);

        bjyWebView = findViewById(R.id.bjy_web_view);

        String url = "https://www.youku.com";

        bjyWebView.initSettings();

        bjyWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (bjyWebView.canGoBack()){
            bjyWebView.goBack();
        }else {

            super.onBackPressed();
        }
    }
}
