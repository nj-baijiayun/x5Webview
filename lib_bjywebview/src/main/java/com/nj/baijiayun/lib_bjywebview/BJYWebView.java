package com.nj.baijiayun.lib_bjywebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nj.baijiayun.lib_bjywebview.inteface.OnReceivedError;
import com.nj.baijiayun.lib_bjywebview.inteface.OnReceivedHttpError;
import com.nj.baijiayun.lib_bjywebview.sonic.SonicJavaScriptInterface;

import java.util.ArrayList;
import java.util.List;

public class BJYWebView extends LinearLayout {
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
                    handler.sendEmptyMessageDelayed(1, 1000);
                } else {
                    if (!isLoadComplete) {
                        onLoadError();
                    }
                }

            }
        }
    };


    /**
     * 初始化
     * @param context
     */
    public BJYWebView(Context context) {
        this(context,null);
    }

    public BJYWebView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BJYWebView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.webview, this);
        initView();

    }

    private void initView(){
        progressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.web_view);
        setProgressBar(progressBar);
        initWebViewSettings(webView);
        loadUrl(url);

    }


    /**
     * 注入js
     * @param sonicJavaScriptInterface
     * @param name
     */
    public void addJavascriptInterface(SonicJavaScriptInterface sonicJavaScriptInterface, String name){
        webView.addJavascriptInterface(sonicJavaScriptInterface,name);
    }


    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object object, String name){
        webView.addJavascriptInterface(object,name);
    }

    /**
     * 设置进度条颜色
     * @param d
     */
    private void setProgressDrawable(Drawable d){
        if (progressBar != null){
            progressBar.setProgressDrawable(d);
        }
    }

    /**
     * 初始化设置
     * @param webView
     */
    private void initWebViewSettings(final WebView webView){
        WebSettings webSettings = webView.getSettings();
        webSettings.setTextZoom(100);
        webSettings.setJavaScriptEnabled(true);
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置Web视图
        webSettings.setBlockNetworkImage(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressBar();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                isLoadComplete = true;
                webView.getSettings().setBlockNetworkImage(false);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
                onLoadComplete();
                super.onPageFinished(view, url);
                if (view.getVisibility() == View.GONE) {
                    view.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onLoadResource(WebView webView, String s) {
                super.onLoadResource(webView, s);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideProgressBar();
                onReceivedError.onReceivedError();

            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                hideProgressBar();
                onReceivedHttpError.onReceivedHttpError();

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    //加载完网页进度条消失
                    hideProgressBar();

                } else {
                    for (int i = 0; i < progressBarList.size(); i++) {
                        progressBarList.get(i).setProgress(newProgress);
                    }
                }

            }
        });

    }



    public void loadDataWithBaseUrl(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }


    private List<ProgressBar> progressBarList = new ArrayList<>();

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

    public boolean canGoBack(){


        return webView.canGoBack();
    }

    public void goBack(){
        webView.goBack();
    }

    public void destory(){
        if (webView !=null){
            webView.destroy();
            webView = null;
        }


    }


    public void setOnReceivedError(OnReceivedError onReceivedError){
        this.onReceivedError = onReceivedError;

    }

    public void setOnReceivedHttpError(OnReceivedHttpError onReceivedHttpError){
        this.onReceivedHttpError = onReceivedHttpError;

    }

}

