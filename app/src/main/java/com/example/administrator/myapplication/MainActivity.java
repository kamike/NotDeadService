package com.example.administrator.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.myapplication.service.MyService;
import com.example.administrator.myapplication.utils.AccessbilityUtils;
import com.example.administrator.myapplication.utils.RomUtil;
import com.example.administrator.myapplication.utils.SettingUtils;
import com.example.administrator.myapplication.utils.SettingsCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webview_main);
        System.out.println("onCreate=======");
//        ServiceUtils.startService(FloatWindowService.class);
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);


        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl("http://tp.t2334.com");
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
                Log.d("main", "onJsAlert:" + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("软件提示")
                                .setMessage(message)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                });
                result.confirm();//这里必须调用，否则页面会阻塞造成假死
                return true;
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

    }



    private void setPermiss() {
        String name = getPackageName() + "/" + MyService.class.getName();
        if (!AccessbilityUtils.checkAccessEnable(name, this)) {
            System.out.println("=====没有开启:" + name);
            SettingUtils.startPage(this);

        } else {
            System.out.println("=====开启了辅助功能");
            ToastUtils.showLong("开启了辅助功能");
        }
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                chcckAccessPermiss();
            }
        };
        handler.sendEmptyMessageDelayed(0, 500);
    }




    @Override
    protected void onResume() {
        super.onResume();
        setPermiss();

    }

    private void chcckAccessPermiss() {
//        boolean isOpen = FloatWindowManager.getInstance().checkPermission(this);
        boolean isOpen = SettingsCompat.canDrawOverlays(this);

        if (!(SettingsCompat.canDrawOverlays(this))) {
            ToastUtils.showLong("没有打开悬浮框权限，跳转设置" + RomUtil.getVersion() + "\n" + RomUtil.getName() + "");
            SettingsCompat.manageDrawOverlays(this);
            finish();
            return;
        }
        ToastUtils.showLong("打开了悬浮框权限！" + RomUtil.getVersion() + "\n" + RomUtil.getName());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy=======");

    }


};


