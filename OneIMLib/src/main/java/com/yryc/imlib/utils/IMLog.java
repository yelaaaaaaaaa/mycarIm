package com.yryc.imlib.utils;


import android.util.Log;

public class IMLog {

    private static final String TAG = "OneIMLog";
    private static final boolean isDebug = true;

    public static int v(String tag, String msg) {
        return isDebug ? Log.v(TAG, "[ " + tag + " ] " + msg) : 0;

    }

    public static int d(String tag, String msg) {
        return isDebug ? Log.d(TAG, "[ " + tag + " ] " + msg) : 0;
    }

    public static int i(String tag, String msg) {
        return isDebug ? Log.i(TAG, "[ " + tag + " ] " + msg) : 0;
    }

    public static int w(String tag, String msg) {
        return isDebug ? Log.w(TAG, "[ " + tag + " ] " + msg) : 0;
    }

    public static int e(String tag, String msg) {
        return isDebug ? Log.e(TAG, "[ " + tag + " ] " + msg) : 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        return isDebug ? Log.e(TAG, "[ " + tag + " ] " + msg, tr) : 0;
    }
}
