package com.nj.baijiayun.lib_bjywebview.utils;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * @author changpeng QQ:1171345871
 * @project android_lib_webview
 * @class name：  BJYReadViewUtils
 * @time 2019-09-23 14:17
 * @describe 简要说明或者详细说明
 */
public class BJYReadViewUtils {

    public static void displayFile(String filePath, String fileName, TbsReaderView tbsReaderView) {
        String   tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";;

        //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
//        String bsReaderTemp = tbsReaderTemp;
        File bsReaderTempFile =new File(tbsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            Log.d("print","准备创建/TbsReaderTemp！！");
            boolean mkdir = bsReaderTempFile.mkdir();
            if(!mkdir){
                Log.d("print","创建/TbsReaderTemp失败！！！！！");
            }
        }
        Bundle bundle = new Bundle();

        //可能是路径错误
        Log.e("print","filePath"+filePath);
        Log.d("print","tempPath"+ tbsReaderTemp);
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", tbsReaderTemp);
        boolean result =false;
        if (tbsReaderView != null){
             result = tbsReaderView.preOpen(Fileutils.getFileType(fileName), false);
        }

        Log.d("print","查看文档--->"+result);
        if (result) {
            tbsReaderView.openFile(bundle);
        }else{
            Log.d("print","文件不存在 ！");
        }
    }

}
