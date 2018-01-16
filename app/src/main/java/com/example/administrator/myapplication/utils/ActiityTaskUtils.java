package com.example.administrator.myapplication.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.blankj.utilcode.util.LogUtils;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.jaredrummler.android.processes.models.Statm;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by wangtao on 2018/1/14.
 */

public class ActiityTaskUtils {

    public static void getTaskList(Context context) throws Exception {
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();

        for (AndroidAppProcess process : processes) {
            // Get some information about the process
            String processName = process.name;

            Stat stat = process.stat();
            int pid = stat.getPid();
            int parentProcessId = stat.ppid();
            long startTime = stat.stime();
            int policy = stat.policy();
            char state = stat.state();

            Statm statm = process.statm();
            long totalSizeOfProcess = statm.getSize();
            long residentSetSize = statm.getResidentSetSize();

            PackageInfo packageInfo = process.getPackageInfo(context, 0);
            String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();

            LogUtils.i("===processName:" + processName + ",appName:" + appName + ",state:" + state);
        }
    }

    public static String getTopActivityPackageName(Context context) {
        String topActivityPackage = null;
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager
                .getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityPackage = f.getClassName();
        }
        return topActivityPackage;
    }


    /**
     * 判断  用户查看使用情况的权利是否给予app
     *
     * @return
     */
    public static boolean isUseGranted(Context context) {

        AppOpsManager appOps = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = -1;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mode = appOps.checkOpNoThrow("android:get_usage_stats",
                    android.os.Process.myUid(), context.getPackageName());
        }
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }

    /**
     * 高版本：获取顶层的activity的包名
     *
     * @return
     */
    public static String getHigherPackageName(Context context) {
        String topPackageName = "null";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            //time - 1000 * 1000, time 开始时间和结束时间的设置，在这个时间范围内 获取栈顶Activity 有效
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    LogUtils.i("===TopPackage Name:", topPackageName);
                    for(long t:mySortedMap.keySet()){
                        LogUtils.i("===all==TopPackage Name"+mySortedMap.get(t).getPackageName());
                    }
                }
            }
        } else {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
            topPackageName = topActivity.getPackageName();
        }
        return topPackageName;
    }

    /**
     * 低版本：获取栈顶app的包名
     *
     * @return
     */
    public static String getLowerVersionPackageName(Context context) {
        String topPackageName;//低版本  直接获取getRunningTasks
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
        topPackageName = topActivity.getPackageName();
        return topPackageName;
    }

}
