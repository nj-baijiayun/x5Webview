package com.nj.baijiayun.bjywebview;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.nj.baijiayun.lib_bjywebview.activity.BJYReadViewActivity;
import com.nj.baijiayun.lib_bjywebview.activity.BJYWebViewActivity;


public class MainActivity extends AppCompatActivity {


//    String docUrl = "http://zyweike.cdn.bcebos.com/zyweike/ep1/2018/04/02/周末加班统计.xlsx";
//    String docUrl = "https://lexuemiao.oss-cn-beijing.aliyuncs.com/uploads/file/2019zxnm18in1565685862.pdf";
//    String docUrl = "https://lexuemiao.oss-cn-beijing.aliyuncs.com/uploads/file/2019jgu3vyl71565072952.docx";
    String download = Environment.getExternalStorageDirectory() + "/download/test/document/";
    String url = "https://test.lexuemiao.com/api/app/appDetailPage?type=course&id=382";
//    url = "https://test.lexuemiao.com/api/app/appDetailPage?type=course&id=382"
    String docUrl = "http://laravel.dsg520.top/1.pdf";
    String save_path = Environment.getExternalStorageDirectory() + "/download/test/document/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bjy_x5web_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BJYWebViewActivity.openActivity(MainActivity.this,url);

            }
        });

        findViewById(R.id.bjy_web_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WebViewActivity.class));
            }
        });

        findViewById(R.id.bjy_read_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BJYReadViewActivity.openActivity(MainActivity.this,docUrl,save_path);
            }
        });
    }

}
