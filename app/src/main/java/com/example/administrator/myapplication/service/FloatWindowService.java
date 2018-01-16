package com.example.administrator.myapplication.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.ActiityTaskUtils;
import com.example.administrator.myapplication.utils.AdbUtils;

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


        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenWidth();


        params.width = screenWidth - subWidth * 2;
        params.height = SizeUtils.dp2px(subHeight);
        params.y = startY;
        params.x = 0;

        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

//        setShowView(windowManager, params);
        thread.start();
    }

    private void setShowView(WindowManager windowManager, WindowManager.LayoutParams params) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_wechat_pay1, null);
        view.setOnClickListener(onclickView());
        TextView tvValue = (TextView) view.findViewById(R.id.tv_value_pay1);

        windowManager.addView(view, params);
    }

    private static final int startY = 550, subWidth = 100, subHeight = 90;


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

    private Thread thread = new Thread() {
        @Override
        public void run() {
            while (true) {
//                Activity activity = ActivityUtils.getTopActivity();
////                LogUtils.i("===getTopActivity:"+activity.getComponentName());
//                LogUtils.i("===getTopActivityPackageName:" + getTopActivityPackageName(context));
//                try {
//                    ActiityTaskUtils.getTaskList(context);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                LogUtils.i("====exe adb:"+ AdbUtils.exeAdb("adb shell dumpsys activity | findstr mFocusedActivity"));

                //3333333
                //判断是否有use 查看使用情况的权限
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    boolean useGranted = ActiityTaskUtils.isUseGranted(context);
                    LogUtils.i("====TopAppService", "use 权限 是否允许授权=" + useGranted);
                    if (useGranted) {
                        String topApp = ActiityTaskUtils.getHigherPackageName(context);
                        LogUtils.i("====TopAppService", "顶层app=" + topApp);
                    } else {
                        //开启应用授权界面
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    String topApp = ActiityTaskUtils.getLowerVersionPackageName(context);
                    LogUtils.i("====TopAppService", "顶层app=" + topApp);
                }


                try {
                    sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
