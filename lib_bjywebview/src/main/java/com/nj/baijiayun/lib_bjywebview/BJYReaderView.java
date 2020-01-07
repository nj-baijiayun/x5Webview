package com.nj.baijiayun.lib_bjywebview;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.Status;
import com.nj.baijiayun.lib_bjywebview.inteface.OnDownLoadError;
import com.nj.baijiayun.lib_bjywebview.inteface.OnDownloadComplete;
import com.nj.baijiayun.lib_bjywebview.inteface.OnPauseDownLoad;
import com.nj.baijiayun.lib_bjywebview.inteface.OnStopDownLoad;
import com.nj.baijiayun.lib_bjywebview.utils.BJYReadViewUtils;
import com.nj.baijiayun.lib_bjywebview.utils.StringUtils;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;


/**
 * 文件预览类（office、pdf等）
 */
public class BJYReaderView {

    private Context mContext;
    private int mDownloadId;
    private  TbsReaderView tbsReaderView;
    private ViewGroup mViewGroup;


    public BJYReaderView(Builder builder) {
        this.mContext = builder.context;
        this.mDownloadId = builder.mDownloadId;
        this.mViewGroup = builder.viewGroup;
        this.tbsReaderView = builder.tbsReaderView;
    }

    /**
     * 获取下载id
     * @return
     */
    public int getDownloadId(){
        return mDownloadId;
    }

    /**
     * 取消下载 需要重新下载
     */
    public void cancelDownload(){
        PRDownloader.cancel(getDownloadId());
    }

    /**
     * 继上次下载的position下载
     */
    public void resumeDownload(){

        PRDownloader.resume(getDownloadId());

    }

    /**
     * 暂停下载
     */
    public void pauseDownload(){

        PRDownloader.pause(getDownloadId());

    }

    /**
     * 获取下载状态
     * @return
     */
    public Status getDownloadStatus(){

        return PRDownloader.getStatus(getDownloadId());
    }

    /**
     * 销毁TbsReaderView
     */
    public void destroyView(){

        if ( mViewGroup != null){
            mViewGroup.removeAllViews();
//          必须调用 否则只能打开第一次
            tbsReaderView.onStop();
        }
    }


    public static class Builder{
        Context context;
        ViewGroup viewGroup;
        String docUrl;
        String savePath;
        TbsReaderView tbsReaderView;
        int mDownloadId;
        private OnPauseDownLoad onPauseDownLoadListener;
        private OnStopDownLoad onStopDownLoadListener;
        private OnProgressListener onProgressDownLoadListener;
        private OnDownloadComplete onDownloadComplete;
        private OnDownLoadError onDownLoadErrorListener;

        public Builder(Context context){
            this.context = context;
            initConfig();
        }

        /**
         * 初始化下载器
         */
        private void initConfig(){
            PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                    .setDatabaseEnabled(true)
                    .build();
            PRDownloader.initialize(context, config);
        }

        public BJYReaderView.Builder addViewGroup(ViewGroup viewGroup){
            this.viewGroup = viewGroup;

            tbsReaderView = new TbsReaderView(context, new TbsReaderView.ReaderCallback() {
                @Override
                public void onCallBackAction(Integer integer, Object o, Object o1) {

                }
            });
            if (viewGroup!=null && tbsReaderView != null){
                viewGroup.removeAllViews();
                viewGroup.addView(tbsReaderView,new RelativeLayout.LayoutParams(-1,-1));
            }

            return this;

        }


        public BJYReaderView.Builder setDocUrl(String docUrl){
            this.docUrl = docUrl;
            return this;
        }

        public BJYReaderView.Builder setSavePath(String savePath){
            this.savePath = savePath;
            if (StringUtils.empty(this.savePath)){
                this.savePath = Environment.getExternalStorageDirectory()
                        + "/download/test/document/";
            }
            return this;
        }




        public  BJYReaderView.Builder setStopDownloadListener(OnStopDownLoad onStopDownLoadListener){

            this.onStopDownLoadListener = onStopDownLoadListener;
            return this;

        }

        public  BJYReaderView.Builder setProgressDownloadListener(OnProgressListener onProgressDownLoadListener){

            this.onProgressDownLoadListener = onProgressDownLoadListener;
            return this;

        }

        public  BJYReaderView.Builder setDownloadCompleteListener(OnDownloadComplete onDownloadComplete){

            this.onDownloadComplete = onDownloadComplete;
            return this;

        }

        public  BJYReaderView.Builder setOnDownLoadErrorListener(OnDownLoadError onDownLoadErrorListener){
            this.onDownLoadErrorListener = onDownLoadErrorListener;
            return this;
        }

        public BJYReaderView build(){
            return new BJYReaderView(this);
        }


        public BJYReaderView.Builder openFile(){
            int i = docUrl.lastIndexOf("/");
            final String docName = docUrl.substring(i, docUrl.length());
            Log.d("print", "---substring---" + docName);
            String[] split = docUrl.split("\\/");
            String s = split[split.length - 4] + split[split.length - 3] + split[split.length - 2] + split[split.length - 1];
            Log.d("print", "截取带时间---" + s);
            //判断是否在本地/[下载/直接打开]
            final File docFile = new File(savePath, docName);
            if (docFile.exists()) {
                //存在本地;
                Log.d("print", "本地存在");
                if (tbsReaderView!= null ){
                    BJYReadViewUtils.displayFile(docFile.toString(), docName,tbsReaderView);
                }

            }else {
                //下载完成后再打开文件
                startDownload(docName);

            }
            return this;

        }



        /**
         *第一次进来 下载并打开文件
         * @param docName
         */
        private void startDownload(final String docName){
            mDownloadId =   PRDownloader.download(docUrl, savePath, docName)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {
                            if (onPauseDownLoadListener != null){
                                onPauseDownLoadListener.pauseDownLoad();
                            }
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            if (onStopDownLoadListener != null) {
                                onStopDownLoadListener.stopDownLoad();
                            }
                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            Log.d("print", "总大小："+progress.totalBytes+"当前大小： "+progress.currentBytes);
                            //在主线程
                            if (onProgressDownLoadListener != null){
                                onProgressDownLoadListener.onProgress(progress);
                            }
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            if (onDownloadComplete != null){
                                onDownloadComplete.onDownloadComplete();
                            }

                            //在主线程
                            Log.d("print", "文件路径："+savePath);
                            if (tbsReaderView != null ){
                                BJYReadViewUtils.displayFile(savePath+docName, docName,tbsReaderView);
                            }

                        }
                        @Override
                        public void onError(Error error) {
                            if (onDownLoadErrorListener != null){
                                onDownLoadErrorListener.onDownLoadError(error);
                            }

                        }
                    });

        }



    }


}
