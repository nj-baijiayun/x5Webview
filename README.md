#更新内容
 1.添加预览功能

 2.添加X5WebView默认的BJYWebViewActivity

#----------------------------------------------------------------

# 1.快速集成
allprojects {

    repositories {

    google()
    jcenter()
    maven {
        //release仓库地址
        url = 'http://172.20.2.114:8081/repository/maven-releases/'
    }
    maven {
        //snapshot仓库地址
        url = 'http://172.20.2.114:8081/repository/maven-snapshots/'
    }
}
# 2.添加依赖
    dependencies {

     //当前最新版本为1.0.2
     implementation 'com.nj.baijiayun:bjywebview:1.0.2'
     }
## 3. BJYWebView使用介绍
### 1.BJYWebView介绍
     原生webview+VasSonic（腾讯系）提升首屏加载速度。有关VasSonic使用介绍可以参考 https://www.jianshu.com/p/250e3d16daea
### 2.BJYWebView使用步骤
    1.在xml中添加BJYWebView

	<?xml version="1.0" encoding="utf-8"?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
          android:layout_width="match_parent"
         android:layout_height="match_parent"
       tools:context=".WebViewActivity">

       <com.nj.baijiayun.lib_bjywebview.BJYWebView
        android:id="@+id/bjy_web_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

    </LinearLayout>

	2.Activity/Fragment中使用(Activity为例)
	
	   @Override
       protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_web_view);
         bjyWebView = findViewById(R.id.bjy_web_view);
         String url = "https://www.youku.com";
         webview(url);
       }


		 private void webview(String url) {
       		 // 1. 首先创建SonicImpl
        	sonic = SonicImpl.getInstance(url, this);
        	// 2. 调用 onCreateSession
        	sonic.onCreateSession();
        	// 3. 注入js
        	bjyWebView.addJavascriptInterface(new SonicJavaScriptInterface
                (sonic.getSonicSessionClient()), "android");
        	//4. 最后绑定bjyWebView
        	sonic.bindBjyWebView(bjyWebView);

			  //处理回调
        	bjyWebView.setOnReceivedError(new OnReceivedError() {
           		 @Override
           		 public void onReceivedError() {
                
						//处理错误回调
           	 		}
        		});
    		 }



		 	/**
     		* 必须重写
     		*/
    		@Override
   			protected void onDestroy() {
        	bjyWebView.destory();
        	//此方法必须调用，否则第二次发开webview会出现白屏
        	sonic.destrory();
        	super.onDestroy();
    		}

## 4. BJYX5WebView使用介绍（项目中建议使用此种类型加载webview）
### 1.BJYX5WebView介绍
	基于腾讯X5内核架构封装 详细请看 https://x5.tencent.com/tbs/guide/sdkInit.html
### 2.使用步骤

#### 在Application中初始化X5内核
    public class App extends Application {
        @Override
        public void onCreate() {
             super.onCreate();
             Log.d("initX5 = ","time = " +System.currentTimeMillis());
             BJYWebViewUtils.initX5Core(getApplicationContext());

            }
        }
	1.在布局文件中
	<LinearLayout

    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".BowerActivity">

    <com.nj.baijiayun.lib_bjywebview.BJYX5WebView
        android:id="@+id/bjy_web_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
	</LinearLayout>

	2.在Activity中

 		@Override
   		protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bower);

        bjyWebView = findViewById(R.id.bjy_web_view);

        String url = "https://www.youku.com";

        bjyWebView.initSettings();

        bjyWebView.loadUrl(url);
    	}
    	
    	
    3。懒人用法 

		没有loading效果

        BJYWebViewActivity.openActivity(MainActivity.this,url);

    	
    	
    	
## BJYReadView 预览文件

### 使用介绍
#### 1.在Activity 中

```

		<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
   		 xmlns:app="http://schemas.android.com/apk/res-auto"
   		 xmlns:tools="http://schemas.android.com/tools"
    		android:layout_width="match_parent"
    		android:layout_height="match_parent"
    		tools:context=".ReadViewActivity">
    		<RelativeLayout
        		android:id="@+id/tbsView"
       			android:layout_width="match_parent"
        		android:layout_height="match_parent"
        		android:orientation="vertical"/>

		</LinearLayout>



```


```


      String docUrl = "http://laravel.dsg520.top/1.pdf";//文件下载地址

        //文件下载后保存的路径
      String downloadpath = Environment.getExternalStorageDirectory() + "/download/test/document/";
   
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_read_view);
          mRelativeLayout = findViewById(R.id.tbsView);
          bjyReaderView = new BJYReaderView.Builder(this)
                         .addViewGroup(mRelativeLayout)
                         .setDocUrl(docUrl)
                         .setSavePath(savePath)
                         .openFile()
                         .build();
    }

```

```


    @Override
    public void onBackPressed() {

        // 退出当前页面暂停下载  必须重写 否则崩溃
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

       懒人用法

        没有loading效果
        BJYReadViewActivity.openActivity(MainActivity.this,docUrl,save_path);

```

##--------------------------------------------------------------------------	
## 混淆

```

#------tbs腾讯x5混淆规则-------

#-optimizationpasses 7
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontoptimize
-dontusemixedcaseclassnames
-verbose
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**
#-overloadaggressively

# ------------------ Keep LineNumbers and properties ---------------- #
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
# --------------------------------------------------------------------------

# Addidional for x5.sdk classes for apps

-keep class com.tencent.smtt.export.external.**{
    *;
}

-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
    *;
}

-keep class com.tencent.smtt.sdk.CacheManager {
    public *;
}

-keep class com.tencent.smtt.sdk.CookieManager {
    public *;
}

-keep class com.tencent.smtt.sdk.WebHistoryItem {
    public *;
}

-keep class com.tencent.smtt.sdk.WebViewDatabase {
    public *;
}

-keep class com.tencent.smtt.sdk.WebBackForwardList {
    public *;
}

-keep public class com.tencent.smtt.sdk.WebView {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
    public static final <fields>;
    public java.lang.String getExtra();
    public int getType();
}

-keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$PictureListener {
    public <fields>;
    public <methods>;
}


-keepattributes InnerClasses

-keep public enum com.tencent.smtt.sdk.WebSettings$** {
    *;
}

-keep public enum com.tencent.smtt.sdk.QbSdk$** {
    *;
}

-keep public class com.tencent.smtt.sdk.WebSettings {
    public *;
}


-keepattributes Signature
-keep public class com.tencent.smtt.sdk.ValueCallback {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebViewClient {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
    public <fields>;
    public <methods>;
}

-keep class com.tencent.smtt.sdk.SystemWebChromeClient{
    public *;
}
# 1. extension interfaces should be apparent
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {
    public protected *;
}

# 2. interfaces should be apparent
-keep public class com.tencent.smtt.export.external.interfaces.* {
    public protected *;
}

-keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
    public protected *;
}

-keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebIconDatabase {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebStorage {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
    public <fields>;
    public <methods>;
}
-keep public class com.tencent.smtt.sdk.CookieSyncManager {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.Tbs* {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.utils.LogFileUtils {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLog {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLogClient {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.CookieSyncManager {
    public <fields>;
    public <methods>;
}

# Added for game demos
-keep public class com.tencent.smtt.sdk.TBSGamePlayer {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.utils.Apn {
    public <fields>;
    public <methods>;
}
-keep class com.tencent.smtt.** {
    *;
}
# end


-keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
    public <fields>;
    public <methods>;
}

-keep class MTT.ThirdAppInfoNew {
    *;
}

-keep class com.tencent.mtt.MttTraceEvent {
    *;
}

# Game related
-keep public class com.tencent.smtt.gamesdk.* {
    public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBooter {
        public <fields>;
        public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
    public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
    public protected *;
}

-keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
    public *;
}

```
    	
 