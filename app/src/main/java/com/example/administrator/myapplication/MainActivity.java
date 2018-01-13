package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(getApplication());
        setContentView(R.layout.activity_main);
        LogUtils.i("onCreate=======");
        ServiceUtils.startService(FloatWindowService.class);
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);

        chcckPermiss();
    }

    private void chcckPermiss() {
//        boolean isOpen = FloatWindowManager.getInstance().checkPermission(this);
        boolean isOpen = SettingsCompat.canDrawOverlays(this);

        if (!isOpen) {
            ToastUtils.showLong("没有打开悬浮框权限，跳转设置" + RomUtil.getVersion() + "\n" + RomUtil.getName() + "");
            SettingsCompat.manageDrawOverlays(this);
        } else {
            ToastUtils.showLong("打开了悬浮框权限！");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("onDestroy=======");
        Intent main = new Intent(this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);
    }
};


