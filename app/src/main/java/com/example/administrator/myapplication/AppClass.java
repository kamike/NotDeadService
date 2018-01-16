package com.example.administrator.myapplication;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by wangtao on 2018/1/16.
 */

public class AppClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
