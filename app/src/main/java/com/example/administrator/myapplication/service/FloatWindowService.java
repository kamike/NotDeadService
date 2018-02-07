package com.example.administrator.myapplication.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.blankj.utilcode.util.ServiceUtils;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.AccessbilityUtils;
import com.example.administrator.myapplication.utils.SettingUtils;

/**
 * Created by wangtao on 2018/1/4.
 */

public class FloatWindowService extends Service {

    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        t.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.mipmap.ic_launcher).setContentText(getString(R.string.app_name));
        nb.setOngoing(true);
        startForeground(0x111, nb.build());
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceUtils.startService(FloatWindowService.class);

    }

    private Thread t = new Thread() {
        @Override
        public void run() {
            while (true) {

                try {
                    sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name = getPackageName() + "/" + MyService.class.getName();
                if (!AccessbilityUtils.checkAccessEnable(name, context)) {
                    SettingUtils.startPage(context);
                }
            }
        }
    };


}
