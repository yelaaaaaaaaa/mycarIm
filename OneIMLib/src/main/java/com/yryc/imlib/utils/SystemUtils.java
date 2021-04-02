package com.yryc.imlib.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.List;

/**
 * 系统工具类
 */
public class SystemUtils {

    /**
     * 判断进程是否在运行
     * @param context
     * @param name
     * @return
     */
    public static boolean isAppRunning(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        } else {
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
            if (infos == null) {
                return false;
            } else {
                Iterator var4 = infos.iterator();
                ActivityManager.RunningAppProcessInfo info;
                do {
                    if (!var4.hasNext()) {
                        return false;
                    }
                    info = (ActivityManager.RunningAppProcessInfo)var4.next();
                } while(!info.processName.equals(name));

                return true;
            }
        }
    }

    /**
     * 获取当前进程名称
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        if (context == null) {
            return null;
        } else {
            int pid = Process.myPid();
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
            if (infos == null) {
                return null;
            } else {
                Iterator var4 = infos.iterator();
                ActivityManager.RunningAppProcessInfo info;
                do {
                    if (!var4.hasNext()) {
                        return null;
                    }
                    info = (ActivityManager.RunningAppProcessInfo)var4.next();
                } while(info.pid != pid);

                return info.processName;
            }
        }
    }

    /**
     * 是否推到后台
     * @param context
     * @return
     */
    public static boolean isInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List runningProcesses;
        if (Build.VERSION.SDK_INT > 20) {
            runningProcesses = am.getRunningAppProcesses();
            if (runningProcesses == null) {
                return true;
            }
            Iterator var4 = runningProcesses.iterator();

            while(true) {
                ActivityManager.RunningAppProcessInfo processInfo;
                do {
                    if (!var4.hasNext()) {
                        return isInBackground;
                    }
                    processInfo = (ActivityManager.RunningAppProcessInfo)var4.next();
                } while(processInfo.importance != 100);

                String[] var6 = processInfo.pkgList;
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String activeProcess = var6[var8];
                    if (activeProcess.equals(context.getPackageName())) {
                        return false;
                    }
                }
            }
        } else {
            runningProcesses = am.getRunningTasks(1);
            ComponentName componentInfo = ((ActivityManager.RunningTaskInfo)runningProcesses.get(0)).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
