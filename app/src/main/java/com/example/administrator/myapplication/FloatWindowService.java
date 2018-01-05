package com.example.administrator.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

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
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
//        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        // 设置flag
//        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        int flags = FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCH_MODAL;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到B-ack键的事件
        params.flags = FLAG_NOT_FOCUSABLE;
        // 不设置这个弹出框的透明遮罩显示为黑色
//        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = 100;
        params.height = 100;

        params.gravity = Gravity.CENTER;
        Button view = new Button(this);
        view.setText("100px");
        view.setBackgroundColor(Color.RED);
        windowManager.addView(view, params);
        _thread.start();
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

    Socket socket = null;
    private Thread _thread = new Thread() {
        PrintWriter outs = null;

        public void run() {
          /*  try {
                //获取控件里填写的IP地址和端口号，连接服务器
                if ((socket == null) || (socket.isConnected() == false)) {
                    socket = new Socket("49.4.140.67",9990);
                }
                //获得输入输出流
                outs = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            while (true) {
                if ((socket == null) || (socket.isConnected() == false)) {
                    try {
                        socket = new Socket("49.4.140.67", 9000);
                        // 获得输入输出流
                        outs = new PrintWriter(socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if ((socket != null) && (socket.isConnected() == true)) {
                    outs.println("ok");
                    outs.flush();
                }
                LogUtils.i("while=======");
            }

        }
    };
}
