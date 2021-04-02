package com.yryc.onecarim;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/7/2 9:51
 * @describe :
 */


public class ImApplication extends MultiDexApplication {

    String uid_one = "USER_178";
    String token_one = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBpZCI6ImJBNWw1TEJmZlB3YyIsImF1ZCI6InljIiwiZXhwIjoxODY4Nzc1ODQ1LCJpYXQiOjE1NTc3MzU4NDUsImlzcyI6InljIiwianRpIjoiMm1mMzB2c2o2MzBzcXQ4MWNnMDAxajcxIiwibmJmIjoxNTU3NzM1ODQ1LCJ1aWQiOiJVU0VSXzE3OCJ9.G0IpwtiqEVbI6V8jURt8iWTn8ELknmc9MFekzFdW81o";

    public static Set<Activity> sAllActivities;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        final ImUtil imUtil = new IM.ImBuilder(get).setAppKey("bA5l5LBffPwc")
//                .setDebug(true)
//                .setEnableFunctions(Function.ALBUM, Function.SHOOTING,
//                        Function.LANGUAGE, Function.LOCATION)
//                .setToken(token_one)
//                .setDeviceId("")
//                .setChannelName(uid_one).build();
    }


    public static void addActivity(Activity act) {
        if (sAllActivities == null) {
            sAllActivities = new HashSet<>();
        }
        sAllActivities.add(act);
    }

    public static void removeActivity(Activity act) {
        if (sAllActivities != null) {
            sAllActivities.remove(act);
        }
    }

    public static void exitApp() {
        if (sAllActivities != null) {
            synchronized (sAllActivities) {
                for (Activity act : sAllActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
