package com.nj.baijiayun.lib_bjywebview.sonic;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.nj.baijiayun.lib_bjywebview.BJYWebView;
import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SonicImpl {
    private SonicSession sonicSession;
    private Context mContext;
    private String url;
    private SonicSessionClientImpl sonicSessionClient;

    public static SonicImpl instance;


    private SonicImpl(String url , Context context){
        this.url=url;
        this.mContext=context;

    }

    public static SonicImpl getInstance(String url , Context context){
        if (instance == null){
            instance = new SonicImpl(url,context);
        }
        return instance;
    }

    /**
     */
    public void onCreateSession() {

        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(mContext.getApplicationContext()), new SonicConfig.Builder().build());
        }


        // if it's offline pkg mode, we need to intercept the session connection
            sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
                @Override
                public String getCacheData(SonicSession session) {
                    return null; // offline pkg does not need cache
                }
            });

            sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
                @Override
                public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                    return new OfflinePkgSessionConnection(mContext, session, intent);
                }
            });
//        SonicEngine.createInstance(new SonicRuntimeImpl(mContext.getApplicationContext()), new SonicConfig.Builder().build());
        // create sonic session and run sonic flow
        sonicSession = SonicEngine.getInstance().createSession(url, sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            // throw new UnknownError("create session fail!");
            Toast.makeText(mContext, "create sonic session fail!", Toast.LENGTH_LONG).show();
        }
    }

    public SonicSessionClientImpl getSonicSessionClient(){
        return this.sonicSessionClient;
    }

    /**
     * 暂时用不到
     */
    public WebViewClient createSonicClient(){
        return new SonicWebViewClient(sonicSession);
    }

    public void BJYWebView(BJYWebView webView){
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(webView);
            sonicSessionClient.clientReady();
        } else { // default mode
            webView.loadUrl(url);
        }
    }

    public void bindBjyWebView(BJYWebView webView){
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(webView);
            sonicSessionClient.clientReady();
        } else { // default mode
            webView.loadUrl(url);
        }
    }

    public void destrory(){
        if(sonicSession!=null){
            sonicSession.destroy();
        }
    }

    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        private final WeakReference<Context> context;

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
            this.context = new WeakReference<Context>(context);
        }

        @Override
        protected int internalConnect() {
            Context ctx = context.get();
            if (null != ctx) {
                try {
                    InputStream offlineHtmlInputStream = ctx.getAssets().open("sonic-demo-index.html");
                    responseStream = new BufferedInputStream(offlineHtmlInputStream);
                    return SonicConstants.ERROR_CODE_SUCCESS;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        protected String internalGetCustomHeadFieldEtag() {
            return SonicSessionConnection.CUSTOM_HEAD_FILED_ETAG;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
        }
    }

}

