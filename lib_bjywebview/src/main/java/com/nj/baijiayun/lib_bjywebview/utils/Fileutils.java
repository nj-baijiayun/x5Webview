package com.nj.baijiayun.lib_bjywebview.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author changpeng QQ:1171345871
 * @project android_lib_webview
 * @class name：  Fileutils
 * @time 2019-09-23 14:16
 * @describe 简要说明或者详细说明
 */
public class Fileutils {
    /**
     * 获取文件类型
     * @param paramString
     * @return
     */
    public static String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d("print", "paramString---->null");
            return str;
        }
        Log.d("print", "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("print", "i <= -1");
            return str;
        }

        str = paramString.substring(i + 1);
        Log.d("print", "paramString.substring(i + 1)------>" + str);
        return str;
    }
}
