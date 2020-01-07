package com.nj.baijiayun.bjywebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nj.baijiayun.lib_bjywebview.BJYWebView;
import com.nj.baijiayun.lib_bjywebview.inteface.OnReceivedError;
import com.nj.baijiayun.lib_bjywebview.sonic.SonicImpl;
import com.nj.baijiayun.lib_bjywebview.sonic.SonicJavaScriptInterface;

public class WebViewActivity extends AppCompatActivity {

    private BJYWebView bjyWebView;
    private SonicImpl sonic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        bjyWebView = findViewById(R.id.bjy_web_view);
        String url = "https://www.youku.com";
        webview(url);
    }

    private void webview(String url) {
        // 1. 首先创建SonicImpl
        sonic = SonicImpl.getInstance(url, this);
        // 2. 调用 onCreateSession
        sonic.onCreateSession();
        // 3. 注入js
        bjyWebView.addJavascriptInterface(new SonicJavaScriptInterface
                (sonic.getSonicSessionClient()), "android");
        //4. 最后绑定bjyWebView
        sonic.bindBjyWebView(bjyWebView);

        //处理回调
        bjyWebView.setOnReceivedError(new OnReceivedError() {
            @Override
            public void onReceivedError() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * 必须重写
     */
    @Override
    protected void onDestroy() {
        bjyWebView.destory();
        //此方法必须调用，否则第二次发开webview会出现白屏
        sonic.destrory();
        super.onDestroy();
    }
}
