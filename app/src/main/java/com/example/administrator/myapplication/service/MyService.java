package com.example.administrator.myapplication.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.administrator.myapplication.utils.RomUtil;
import com.example.administrator.myapplication.utils.ShowFloatView;
import com.example.administrator.myapplication.utils.ShowFloatView2;
import com.example.administrator.myapplication.utils.ShowFloatView3;

/**
 * Created by wangtao on 2017/9/5.
 */

public class MyService extends AccessibilityService {
    private final String first_open = "first_open";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        currintPage = 0;
        System.out.println("onServiceConnected===========");
        AccessibilityServiceInfo info = getServiceInfo();
        isOpenPhoto=false;
    }

    private boolean isEnter1 = false, isEnter2 = false, isEnter3 = false;
    private boolean isOpenPhoto=false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        CharSequence className = event.getClassName();

        //com.tencent.mm.plugin.remittance.ui.RemittanceAdapterUI
        //com.tencent.mm.plugin.remittance.ui.RemittanceUI
        if (className == null) {
            System.out.println("=======nullllllll==" + event.toString());
            return;
        }
        System.out.println("=========" + className);
        if (className.toString().contains("android.widget")) {
            return;
        }
        if(className.toString().contains("com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI")){
            isOpenPhoto=true;
        }
        if (!isOpenPhoto&&className.toString().contains("com.tencent.mm.plugin.scanner.ui.BaseScanUI")) {

            if (!SPUtils.getInstance().getBoolean("isOpeanScanle", false)) {
                //是第一次进来的啊
                SPUtils.getInstance().put("isOpeanScanle", true);
                LogUtils.i("===是第一次进来扫码界面");
            } else {
                delayHandlerShow.sendEmptyMessageDelayed(-1, 0);
                LogUtils.i("===是第22次进来扫码界面");
            }
        }

        if (isPage1(className)) {
            isOpenPhoto=false;
            isEnter1 = true;
            if (!SPUtils.getInstance().getBoolean(first_open, false)) {
                LogUtils.i("====这是第一次进来111！");
                return;
            }
            //
            if (showFloatView == null) {
                showFloatView = new ShowFloatView(this);
                showFloatView.showFloatview();
                currintPage = 1;
            }

            delayHandler.sendEmptyMessageDelayed(2, 280);
            delayHandler.sendEmptyMessageDelayed(3, 480);
        }


        if (isPage2(className)) {
            isEnter2 = true;
            if (!SPUtils.getInstance().getBoolean(first_open, false)) {
                LogUtils.i("======这是第一次进来222！");
                return;
            }
            if (showFloatView2 == null) {
                delayHandlerShow.sendEmptyMessageDelayed(2, 200);
                delayHandler.sendEmptyMessageDelayed(1, 350);
                delayHandler.sendEmptyMessageDelayed(3, 480);
            }
        }
        if (isPage3(className)) {
            isEnter3 = true;
            if (!SPUtils.getInstance().getBoolean(first_open, false)) {
                LogUtils.i("====这是第一次进来333！");
                return;
            }
            if (showFloatView3 == null) {
                showFloatView3 = new ShowFloatView3(this);
                showFloatView3.showFloatview();
                currintPage = 3;
                delayHandler.sendEmptyMessageDelayed(2, 280);
                delayHandler.sendEmptyMessageDelayed(1, 480);
            }
        }


        if (showFloatView == null && showFloatView2 == null) {
            return;
        }

        for (String page : hidePageArray) {
            if (className.toString().contains(page)) {
                System.out.println("======remove");
                //退出微信支付界面了
                if (showFloatView != null && currintPage == 1) {
                    delayHandler.sendEmptyMessageDelayed(1, 200);
                }
                if (showFloatView2 != null && currintPage == 2) {
                    delayHandler.sendEmptyMessageDelayed(2, 500);
                }
                if (currintPage == 3) {
                    delayHandler.sendEmptyMessageDelayed(3, 200);
                }
                currintPage = 0;
                break;
            }
        }


    }


    private boolean isPage3(CharSequence className) {
//        if(RomUtil.isOppo()&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            return className.toString().contains("com.tencent.mm.plugin.wallet_core.ui.m");
//        }
        return className.toString().contains("com.tencent.mm.plugin.remittance.ui.RemittanceResultNewUI");
    }

    private boolean isPage1(CharSequence className) {
        return className.toString().contains("com.tencent.mm.plugin.remittance.ui.RemittanceUI");
    }

    private boolean isPage2(CharSequence className) {
        if (RomUtil.isOppo() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI
            return className.toString().contains("com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI");
        }
        return className.toString().contains("com.tencent.mm.plugin.wallet_core.ui.l");
    }

    private int currintPage = 0;


    private String[] hidePageArray = {"com.tencent.mm.plugin.scanner.ui.BaseScanUI", "com.android.systemui", "launcher.Launcher", ".Launcher"};
    private String[] finishPage = {"com.tencent.mm.ui.LauncherUI", "com.tencent.mm.plugin.scanner.ui.BaseScanUI"};

    private ShowFloatView showFloatView;
    private ShowFloatView2 showFloatView2;
    private ShowFloatView3 showFloatView3;

    private Handler delayHandlerShow = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case -1:
                    SPUtils.getInstance().put(first_open, true);
                    break;
                case 2:
                    if (showFloatView2 == null) {
                        showFloatView2 = new ShowFloatView2(MyService.this);
                        showFloatView2.showFloatview();
                        System.out.print("===show 22222");
                    }
                    currintPage = 2;
                    break;
                case 3:
                    break;
                default:

            }
        }
    };


    private Handler delayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("======remove:" + msg.what);
            switch (msg.what) {
                case 1:
                    if (showFloatView == null) {
                        return;
                    }
                    showFloatView.removeView();
                    showFloatView = null;

                    break;
                case 2:
                    if (showFloatView2 == null) {
                        System.out.println("====22222===null");
                        return;
                    }
                    showFloatView2.removeView();
                    showFloatView2 = null;
                    break;
                case 3:
                    if (showFloatView3 == null) {
                        return;
                    }
                    showFloatView3.removeView();
                    showFloatView3 = null;
                    break;
                default:

            }
        }
    };


    @Override
    public void onInterrupt() {

    }

    private void inputHello(String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        //找到当前获取焦点的view
        AccessibilityNodeInfo target = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (target == null) {
            LogUtils.i("input: null====");
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            target.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }

    public void findText() {

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int key = event.getKeyCode();
        System.out.println("========onclick-key:" + key);
        switch (key) {
            case KeyEvent.KEYCODE_HOME:
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                if (showFloatView != null) {
                    showFloatView.removeView();
                }
                break;

        }
        return super.onKeyEvent(event);
    }


}
