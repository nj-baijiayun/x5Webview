/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

package com.nj.baijiayun.lib_bjywebview.sonic;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceResponse;

import com.tencent.sonic.sdk.BuildConfig;
import com.tencent.sonic.sdk.SonicRuntime;
import com.tencent.sonic.sdk.SonicSessionClient;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * the sonic host application must implement SonicRuntime to do right things.
 * 创建一个类继承SonicRuntime
 * SonicRuntime类主要提供sonic运行时环境，包括Context、用户UA、ID(用户唯一标识，存放数据时唯一标识对应用户)等等信息。
 * 以下代码展示了SonicRuntime的几个方法。
 */

public class SonicRuntimeImpl extends SonicRuntime {

    public SonicRuntimeImpl(Context context) {
        super(context);
    }

    /**
     * 获取用户UA信息
     * @return
     */
    @Override
    public String getUserAgent() {
        return "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Mobile Safari/537.36";
    }

    /**
     * 获取用户ID信息
     * @return
     */
    @Override
    public String getCurrentUserAccount() {
        return "sonic-demo-master";
    }

    @Override
    public String getCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(url);
    }

    @Override
    public void log(String tag, int level, String message) {
        switch (level) {
            case Log.ERROR:
                Log.e(tag, message);
                break;
            case Log.INFO:
                Log.i(tag, message);
                break;
            default:
                Log.d(tag, message);
        }
    }

    /**
     * 创建web资源请求
     * @param mimeType
     * @param encoding
     * @param data
     * @param headers
     * @return
     */
    @Override
    public Object createWebResourceResponse(String mimeType, String encoding, InputStream data, Map<String, String> headers) {
        WebResourceResponse resourceResponse =  new WebResourceResponse(mimeType, encoding, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse.setResponseHeaders(headers);
        }
        return resourceResponse;
    }

    @Override
    public void showToast(CharSequence text, int duration) {

    }

    @Override
    public void notifyError(SonicSessionClient client, String url, int errorCode) {

    }

    /**
     * 使用Sonic加速
     * @param url
     * @return
     */
    @Override
    public boolean isSonicUrl(String url) {
        return true;
    }

    @Override
    public boolean setCookie(String url, List<String> cookies) {
        if (!TextUtils.isEmpty(url) && cookies != null && cookies.size() > 0) {
            CookieManager cookieManager = CookieManager.getInstance();
            for (String cookie : cookies) {
                cookieManager.setCookie(url, cookie);
            }
            return true;
        }
        return false;
    }

    /**
     * 网络是否允许
     * @return
     */
    @Override
    public boolean isNetworkValid() {
        return true;
    }

    @Override
    public void postTaskToThread(Runnable task, long delayMillis) {
        Thread thread = new Thread(task, "SonicThread");
        thread.start();
    }


    /**
     * 设置缓存
     * @return
     */
    @Override
    public File getSonicCacheDir() {
        if (BuildConfig.DEBUG) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sonic/";
            File file = new File(path.trim());
            if(!file.exists()){
                file.mkdir();
            }
            return file;
        }
       return super.getSonicCacheDir();
    }

    @Override
    public String getHostDirectAddress(String url) {
        return null;
    }
}
