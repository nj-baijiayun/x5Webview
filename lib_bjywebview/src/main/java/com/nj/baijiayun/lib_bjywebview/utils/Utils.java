package com.nj.baijiayun.lib_bjywebview.utils;

import android.content.Context;



/**
 * @author changpeng QQ:1171345871
 * @project android_lib_webview
 * @class name：  Utils
 * @time 2019-09-23 09:43
 * @describe 简要说明或者详细说明
 */
public class Utils {


    private static Context mContext;


    public static void init(Context context){

        mContext = context.getApplicationContext();
        
    }

    public static Context getContext(){

        if (mContext != null){
            return mContext;

        }  return null;
    }













}
