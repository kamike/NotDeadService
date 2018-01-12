package com.example.administrator.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.Utils;

/**
 * Created by wangtao on 2018/1/4.
 */

public class FloatWindowService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(getApplication());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        // 设置flag
//        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        int flags = FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCH_MODAL;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到B-ack键的事件
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 不设置这个弹出框的透明遮罩显示为黑色
//        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = 200;
        params.height = 150;

        params.gravity = Gravity.CENTER;
        Button view = new Button(this);
        view.setText("100px");
        view.setBackgroundColor(Color.RED);
        view.setOnClickListener(onclickView());
        windowManager.addView(view, params);
    }

    private View.OnClickListener onclickView() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("onclickView====");
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.mipmap.ic_launcher).setContentText("service");
        nb.setOngoing(true);
        startForeground(0x111, nb.build());
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceUtils.startService(FloatWindowService.class);

    }


}
