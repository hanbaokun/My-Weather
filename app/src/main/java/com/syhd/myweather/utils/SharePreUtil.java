package com.syhd.myweather.utils;

import android.content.SharedPreferences;

import com.syhd.myweather.App;


public class SharePreUtil {
    private static SharedPreferences sp = App.application.SP;
    private static SharedPreferences.Editor EDIT = App.application.EDIT;

    /**
     * 保存boolean值到sp文件中
     *
     * @param key     保存的内容名称
     * @param value   保存内容
     */
    public static void saveBooleanData(String key, boolean value) {
        EDIT.putBoolean(key, value).commit();
    }

    /**
     * 获取sp中保存的内容
     *
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBooleanData(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 保存字符串到sp文件中
     *
     * @param key
     * @param value
     */
    public static void saveStringData(String key, String value) {
        EDIT.putString(key, value).commit();
    }

    /**
     * 获取String类型值
     * @param key
     * @param defValue
     * @return
     */
    public static String getStringData(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 保存Int到sp文件中
     *
     * @param key
     * @param value
     */
    public static void saveIntData(String key, int value) {
        EDIT.putInt(key,value).commit();
    }

    /**
     * 获取Int类型值
     * @param key
     * @param defValue
     * @return
     */
    public static int getIntData(String key, int defValue) {
        return sp.getInt(key, defValue);
    }
    /**
     * 保存float到sp文件中
     *
     * @param key
     * @param value
     */
    public static void saveFloatData(String key, float value) {
        EDIT.putFloat(key,value).commit();
    }

    /**
     * 获取float类型值
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloatData(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }
}
