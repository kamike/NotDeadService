package com.example.administrator.myapplication.utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/1/27.
 */

public class VivoUtils {
    private static final String TAG = "MeizuUtils";


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int)method.invoke(manager, 24, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
              LogUtils.i("vivo-====="+Log.getStackTraceString(e));
            }
        } else {
            LogUtils.i("vivo-=====Below API 19 cannot invoke!");
        }
        return false;
    }
}
