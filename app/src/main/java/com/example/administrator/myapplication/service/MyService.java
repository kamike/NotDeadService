package com.example.administrator.myapplication.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.example.administrator.myapplication.utils.ShowFloatView;
import com.example.administrator.myapplication.utils.ShowFloatView2;

import java.util.LinkedHashSet;

/**
 * Created by wangtao on 2017/9/5.
 */

public class MyService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        System.out.println("onServiceConnected===========");
        AccessibilityServiceInfo info = getServiceInfo();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        System.out.println("=========" + className);
        //com.tencent.mm.plugin.remittance.ui.RemittanceAdapterUI
        //com.tencent.mm.plugin.remittance.ui.RemittanceUI
        if (className == null) {
            return;
        }

        if (className.toString().contains("android.widget")) {
            return;
        }

        if (className.toString().contains("com.tencent.mm.plugin.remittance.ui.RemittanceUI")) {
            //
            if (showFloatView == null) {
                showFloatView = new ShowFloatView(this);
                showFloatView.showFloatview();
            }
            if (showFloatView2 != null) {
                showFloatView2.removeView();
                showFloatView2 = null;
            }
        }
        if (className.toString().contains("com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI")) {
            if (showFloatView2 == null) {
                showFloatView2 = new ShowFloatView2(this);
                showFloatView2.showFloatview();
                if (showFloatView != null) {
                    showFloatView.removeView();
                    showFloatView = null;
                }
            }
        }

        if (className.toString().contains("launcher.Launcher")) {
            System.out.println("====有launcher");
        }

        if (showFloatView != null) {
            for (String page : hidePageArray) {
                if (className.toString().contains(page)) {
                    showFloatView.removeView();
                    showFloatView = null;
                    break;
                }
            }
        }

        if (showFloatView2 != null) {
            for (String page : hidePageArray) {
                if (className.toString().contains(page)) {
                    showFloatView2.removeView();
                    showFloatView2 = null;
                    break;
                }
            }
        }

    }


    private String[] hidePageArray = {"com.tencent.mm.plugin.scanner.ui.BaseScanUI", "com.android.systemui", "launcher.Launcher", ".Launcher"};

    private ShowFloatView showFloatView;
    private ShowFloatView2 showFloatView2;


    private void AddAllToListSource(LinkedHashSet<AccessibilityNodeInfo> lsit, AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        for (int index = 0; index < node.getChildCount(); index++) {
            AccessibilityNodeInfo nodeChild = node.getChild(index);
//            if (isCheckNode(nodeChild)) {

            lsit.add(nodeChild);
//            }
            AddAllToListSource(lsit, nodeChild);
        }
    }


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
