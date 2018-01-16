package com.example.administrator.myapplication.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.example.administrator.myapplication.utils.ShowFloatView;

import java.util.LinkedHashSet;

/**
 * Created by wangtao on 2017/9/5.
 */

public class MyService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        System.out.println("onServiceConnected===========");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        System.out.println("=========" + className);
        //com.tencent.mm.plugin.remittance.ui.RemittanceAdapterUI
        //com.tencent.mm.plugin.remittance.ui.RemittanceUI

        if (TextUtils.equals(className, "com.tencent.mm.plugin.remittance.ui.RemittanceUI")) {
            //
            if (showFloatView == null) {
                showFloatView = new ShowFloatView(this);
                showFloatView.showFloatview();
            }
        } else {
            if (showFloatView != null) {
//                showFloatView.removeView();
            }
        }

    }

    private ShowFloatView showFloatView;


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


}
