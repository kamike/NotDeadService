package com.example.administrator.myapplication.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.example.administrator.myapplication.R;

/**
 * Created by wangtao on 2018/1/16.
 */

public class ShowFloatView {
    private final Context context;

    public ShowFloatView(Context c) {
        this.context = c;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();
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
        params.y = SizeUtils.dp2px(startY);
        params.x = 0;

        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        showFloatview();
    }

    private WindowManager.LayoutParams params;
    private static final int startY = 185, subWidth = 100, subHeight = 90;


    public void showFloatview() {
        if (view != null) {
            return;
        }

        view = LayoutInflater.from(context).inflate(R.layout.view_wechat_pay1, null);
        TextView tvValue = (TextView) view.findViewById(R.id.tv_value_pay1);
        tvValue.setText("2.00");
        windowManager.addView(view, params);
    }

    private WindowManager windowManager;
    private View view;

    public void removeView() {
        if (windowManager == null || view == null) {
            return;
        }
        windowManager.removeView(view);
        view = null;
        windowManager = null;
    }
}
