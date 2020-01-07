package com.nj.baijiayun.lib_bjywebview.download;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.nj.baijiayun.lib_bjywebview.utils.Utils;

/**
 * @author changpeng QQ:1171345871
 * @project android_lib_webview
 * @class name：  DownLoadManager
 * @time 2019-09-23 09:39
 * @describe 简要说明或者详细说明
 */
public class DownLoadManager {

    public void startDownload(String url,String dirPath,String fileName){
        int downloadId = PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                    }

                    @Override
                    public void onError(Error error) {

                    }

                });


    }

}
