package com.nj.baijiayun.bjywebview;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.downloader.OnProgressListener;
import com.downloader.Progress;
import com.downloader.Status;
import com.nj.baijiayun.lib_bjywebview.BJYReaderView;


public class ReadViewActivity extends AppCompatActivity {



    private RelativeLayout mRelativeLayout;
    private BJYReaderView bjyReaderView;

//    String docUrl = "http://zyweike.cdn.bcebos.com/zyweike/ep1/2018/04/02/周末加班统计.xlsx";
//    String docUrl = "https://lexuemiao.oss-cn-beijing.aliyuncs.com/uploads/file/2019zxnm18in1565685862.pdf";
//    String docUrl = "https://lexuemiao.oss-cn-beijing.aliyuncs.com/uploads/file/2019jgu3vyl71565072952.docx";
//    String docUrl = "http://laravel.dsg520.top/1.pptx";

    String docUrl = "http://laravel.dsg520.top/1.pdf";
//    String docUrl = "http://laravel.dsg520.top/白帽子讲WEB安全.pdf";

    String savePath = Environment.getExternalStorageDirectory() + "/download/test/document/";

    TextView loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_view);
        loadingView =  findViewById(R.id.loading_view_rl);
        mRelativeLayout = findViewById(R.id.tbsView);

        bjyReaderView = new BJYReaderView.Builder(this)
                .addViewGroup(mRelativeLayout)
                .setDocUrl(docUrl)
                .setSavePath(savePath)
                .setProgressDownloadListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        //下载中 显示loading等

                    }
                })
                .openFile()
                .build();

    }



    /**
     * 退出当前页面就暂停下载
     */
    @Override
    public void onBackPressed() {

        if (bjyReaderView.getDownloadStatus() == Status.RUNNING){
            bjyReaderView.pauseDownload();
        }
        super.onBackPressed();
    }

    /**
     * 必须重写 否则只能打开第一次
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bjyReaderView !=null){
            bjyReaderView.destroyView();
        }
    }


}
