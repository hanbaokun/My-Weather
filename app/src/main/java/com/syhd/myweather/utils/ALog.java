package com.syhd.myweather.utils;

import android.util.Log;

/**
 * @author 韩宝坤
 * @date 2016/8/18 14:20
 * @email kunyu1342@163.com
 * @description 日志工具类
 */
public class ALog {
    /**
     * Application TAG,use "logcat -s TAG"
     */
    private static String TAG = "ALOG";


    private static boolean IS_FULL_CLASSNAME;

    /**
     * log level
     */
    private static int LOG_LEVEL = Log.VERBOSE;

    /**
     * print full class name or not
     *
     * @param isFullClassName
     */
    public static void setFullClassName(boolean isFullClassName) {
        ALog.IS_FULL_CLASSNAME = isFullClassName;
    }

    /**
     * set log level, default Log.VERBOSE
     *
     * @param level
     */
    public static void setLogLevel(int level) {
        ALog.LOG_LEVEL = level;
    }

    /**
     * set application TAG, default "ALOG"
     *
     * @param tag
     */
    public static void setAppTAG(String tag) {
        ALog.TAG = tag;
    }


    public static void v(String msg) {
        if (LOG_LEVEL <= Log.VERBOSE) {
            Log.v(TAG, getLogTitle() + msg);
        }
    }


    public static void d(String msg) {
        if (LOG_LEVEL <= Log.DEBUG) {
            Log.d(TAG, getLogTitle() + msg);
        }
    }

    public static void i(String msg) {
        if (LOG_LEVEL <= Log.INFO) {
            Log.i(TAG, getLogTitle() + msg);
        }
    }

    public static void w(String msg) {
        if (LOG_LEVEL <= Log.WARN) {
            Log.w(TAG, getLogTitle() + msg);
        }
    }

    public static void e(String msg) {
        if (LOG_LEVEL <= Log.ERROR) {
            Log.e(TAG, getLogTitle() + msg);
        }
    }

    /**
     * make log title
     *
     * @return
     */
    private static String getLogTitle() {
        StackTraceElement elm = Thread.currentThread().getStackTrace()[4];
        String className = elm.getClassName();
        if (!IS_FULL_CLASSNAME) {
            int dot = className.lastIndexOf('.');
            if (dot != -1) {
                className = className.substring(dot + 1);
            }
        }
        return className + "." + elm.getMethodName() + "(" + elm.getLineNumber() + ")" + ": ";
    }


}