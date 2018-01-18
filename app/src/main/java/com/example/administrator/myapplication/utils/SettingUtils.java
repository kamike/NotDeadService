package com.example.administrator.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by wangtao on 2018/1/18.
 */

public class SettingUtils {
    public static void startPage(Context c) {
        String settingAccessName = getSettingName(c);
        Intent intent = new Intent(settingAccessName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent);

    }

    private static String getSettingName(Context c) {
        String name = "com.android.settings.SubSettings";

//        return name;
        return Settings.ACTION_ACCESSIBILITY_SETTINGS;
    }
}
