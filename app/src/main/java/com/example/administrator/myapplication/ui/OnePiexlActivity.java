package com.example.administrator.myapplication.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class OnePiexlActivity extends AppCompatActivity {
    private BroadcastReceiver endReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_one_piexl);
        //设置1像素
        Log.i("msg","start one piex");
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 10;
        params.width = 10;
        window.setAttributes(params);
        _Logthread.start();
        //结束该页面的广播
       /* endReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(endReceiver, new IntentFilter("finish"));
        //检查屏幕状态
        checkScreen();*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkScreen();
    }

    /**
     * 检查屏幕状态  isScreenOn为true  屏幕“亮”结束该Activity
     */
    private void checkScreen() {
        PowerManager pm = (PowerManager) getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            finish();
        }
    }


    private Thread _Logthread = new Thread(){
        public void run(){
            while(true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Log.i("msg","one piex...");
            }

        }
    };

}
