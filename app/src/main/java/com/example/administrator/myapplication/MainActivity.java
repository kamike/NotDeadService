package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.floatwindowpermission.permission.FloatWindowManager;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(getApplication());
        setContentView(R.layout.activity_main);
        LogUtils.i("onCreate=======");
//        ServiceUtils.startService(FloatWindowService.class);
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);

        chcckPermiss();
    }

    private void chcckPermiss() {
        FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
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


