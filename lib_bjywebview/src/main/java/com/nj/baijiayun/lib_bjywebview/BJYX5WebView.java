package com.nj.baijiayun.lib_bjywebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.nj.baijiayun.lib_bjywebview.inteface.OnReceivedError;
import com.nj.baijiayun.lib_bjywebview.inteface.OnReceivedHttpError;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.util.ArrayList;
import java.util.List;

public class BJYX5WebView extends LinearLayout {

    private List<ProgressBar> progressBarList = new ArrayList<>();
    public boolean isLoadComplete;
    private int timeout = 4;
    public String url;
    private WebView webView;
    private ProgressBar progressBar;

    private OnReceivedError onReceivedError;
    private OnReceivedHttpError onReceivedHttpError;

    @SuppressLint("HandlerLeak")
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (timeout > 0) {
                    timeout--;
                    handler.sendEmptyMessageDelayed(1, 500);
                } else {
                    if (!isLoadComplete) {
                        hideProgressBar();
                    }
                }

            }
        }
    };


    public BJYX5WebView(Context context) {
        this(context, null);
    }

    public BJYX5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BJYX5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.x5webview, this);
        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressbar);
        webView= findViewById(R.id.bjy_x5web_view);
        setProgressBar(progressBar);
        initSettings();
        loadUrl(url);
    }


    public void initSettings() {
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
         webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
         webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//         webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
         webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                showProgressBar();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                hideProgressBar();
                if (onReceivedHttpError != null){
                    onReceivedHttpError.onReceivedHttpError();
                }

            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                hideProgressBar();
                if (onReceivedError != null){

                    onReceivedError.onReceivedError();
                }
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                hideProgressBar();
                if (onReceivedError != null){

                    onReceivedError.onReceivedError();
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
            }

            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                super.onProgressChanged(webView, newProgress);
                if (newProgress == 100) {
                    //加载完网页进度条消失
                    hideProgressBar();

                } else {
                    for (int i = 0; i < progressBarList.size(); i++) {
                        progressBarList.get(i).setProgress(newProgress);
                    }
                }
                webView.setVisibility(VISIBLE);
            }
        });
    }


    public void setProgressBar(ProgressBar progressBar) {
        if (progressBarList == null) {
            progressBarList = new ArrayList<>();
        }
        if (!progressBarList.contains(progressBar)) {
            progressBarList.add(progressBar);
        }
    }

    void hideProgressBar() {
        for (int i = 0; i < progressBarList.size(); i++) {
            progressBarList.get(i).setVisibility(View.GONE);
        }
    }

    void showProgressBar() {

        for (int i = 0; i < progressBarList.size(); i++) {
            progressBarList.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void onLoadError() {
        hideProgressBar();
    }


    public void onLoadComplete() {
    }


    public void loadUrl(String url) {
        if (url != null) {
            this.url = url;
            webView.loadUrl(url);
            timeout = 3;
            handler.sendEmptyMessageDelayed(1, 1000);

        }
    }


    /**
     * 设置进度条颜色
     * @param d
     */
    public void setProgressDrawable(Drawable d){
        if (progressBar != null){
            progressBar.setProgressDrawable(d);
        }
    }


    /**
     * 进度条是否显示
     * @param visibility
     */
    public void setProgressVisibility(int visibility){
        if (progressBarList != null){
            progressBarList.clear();
        }
        if (progressBar != null){

            progressBar.setVisibility(visibility);
        }

    }


    public void destory(){
        if (webView !=null){
            webView.destroy();
            webView = null;
        }

    }


    public void addJavascriptInterface(Object o,String action){
        if (webView !=null){
            webView.addJavascriptInterface(o,action);
        }
    }

    public void goBack(){

            webView.goBack();

    }

    public boolean canGoBack(){
        return webView.canGoBack();
    }



    public void setOnReceivedError(OnReceivedError onReceivedError){
        this.onReceivedError = onReceivedError;

    }

    public void setOnReceivedHttpError(OnReceivedHttpError onReceivedHttpError){
        this.onReceivedHttpError = onReceivedHttpError;

    }

    public void evaluateJavascript(String s, ValueCallback<String> callBack){
        webView.evaluateJavascript(s,callBack);
    }

    public WebView getWebView(){
        return webView;
    }

}
