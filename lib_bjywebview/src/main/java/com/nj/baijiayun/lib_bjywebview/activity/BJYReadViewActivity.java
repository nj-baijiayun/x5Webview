package com.nj.baijiayun.lib_bjywebview.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.downloader.Status;
import com.nj.baijiayun.lib_bjywebview.BJYReaderView;
import com.nj.baijiayun.lib_bjywebview.R;

public class BJYReadViewActivity extends AppCompatActivity {

    private static final String DOWN_LOAD_URL = "doc_url";
    private static final String SAVE_PATH = "save_path";
    private String docUrl;
    private String savePath;
    private RelativeLayout mRelativeLayout;
    private BJYReaderView bjyReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bjy_read_view);

        docUrl = getIntent().getStringExtra(DOWN_LOAD_URL);
        savePath = getIntent().getStringExtra(SAVE_PATH);
        initView();

    }

    public void initView(){
        mRelativeLayout = findViewById(R.id.tbsView);
        bjyReaderView = new BJYReaderView.Builder(this)
                .addViewGroup(mRelativeLayout)
                .setDocUrl(docUrl)
                .setSavePath(savePath)
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

    public static void openActivity(Activity activity,String docUrl, String savePath){

        Intent intent = new Intent();
        intent.putExtra(DOWN_LOAD_URL,docUrl);
        intent.putExtra(SAVE_PATH,savePath);
        intent.setClass(activity,BJYReadViewActivity.class);
        activity.startActivity(intent);


    }
}
