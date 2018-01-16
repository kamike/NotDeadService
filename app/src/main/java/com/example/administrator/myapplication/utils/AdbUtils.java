package com.example.administrator.myapplication.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wangtao on 2018/1/14.
 */

public class AdbUtils {
    public static String exeAdb(String adb) {
        BufferedReader reader = null;
        try {
            //("ps -P|grep bg")执行失败，PC端adb shell ps -P|grep bg执行成功
            //Process process = Runtime.getRuntime().exec("ps -P|grep tv");
            //-P 显示程序调度状态，通常是bg或fg，获取失败返回un和er
            // Process process = Runtime.getRuntime().exec("ps -P");
            //打印进程信息，不过滤任何条件
            Process process = Runtime.getRuntime().exec(adb);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer output = new StringBuffer();
            int read;
            char[] buffer = new char[4096];
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            return output.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }


    }
}
