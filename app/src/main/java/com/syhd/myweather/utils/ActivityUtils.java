package com.syhd.myweather.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

import java.io.Serializable;

/**
 * @author 韩宝坤
 * @date 2016/8/18 14:21
 * @email kunyu1342@163.com
 * @description activity工具类
 */
public class ActivityUtils  {
	/**
	 * 启动一个Activity并关闭当前Activity
	 * @param activity 当前Activity
	 * @param cls 要启动的Activity
	 */
	public static void startActivityAndFinish(Activity activity, Class<?> cls){
		Intent intent = new Intent(activity,cls);
		activity.startActivity(intent);
		
		activity.finish();
	}
	
	/**
	 * 启动Activity
	 * @param activity 当前Activity
	 * @param cls 要启动的Activity Class
	 */
	public static void startActivity(Activity activity, Class<?> cls){
		Intent intent = new Intent(activity,cls);
		activity.startActivity(intent);
	}
	/**
	 * 启动Activity并传int数据 key:"data"
	 * @param activity 当前Activity
	 * @param cls 要启动的Activity Class
	 * @param data int型数据
	 */
	public static void startActivityForIntData(Activity activity, Class<?> cls, int data){
		Intent intent = new Intent(activity,cls);
		intent.putExtra("data", data);
		activity.startActivity(intent);
	}
	/**
	 * 启动Activity并传String数据 key:"data"
	 * @param activity 当前Activity
	 * @param cls 要启动的Activity Class
	 * @param data String型数据
	 */
	public static void startActivityForData(Activity activity, Class<?> cls, String data){
		Intent intent = new Intent(activity,cls);
		intent.putExtra("data", data);
		activity.startActivity(intent);
	}
	/**
	 * 启动Activity传String数据并接收返回结果 key:"data"
	 * @param activity 当前Activity
	 * @param cls 要启动的Activity Class
	 * @param data String型数据
	 * @param flag int标记
	 */
	public static void startActivityForResult(Activity activity, Class<?> cls, String data, int flag){
		Intent intent = new Intent(activity,cls);
		intent.putExtra("data", data);
		intent.setFlags(flag);
		activity.startActivityForResult(intent, flag);
	}
	/**
	 * 启动Activity并传序列化对象数据 key:"Serializable"
	 * @param activity 当前Activity
	 * @param cls 要启动的Activity Class
	 * @param data String型数据
	 */
	public static void startActivityForSerializable(Activity activity, Class<?> cls, Serializable data){
		Intent intent = new Intent(activity,cls);
		intent.putExtra("Serializable", data);
		activity.startActivity(intent);
	}
	/**
	 * 启动Activity并传String对象数据
	 * @param activity 当前Activity
	 * @param cls 要启动的Activity Class
	 * @param name key
	 * @param data String数据
	 */
	public static void startActivityForStringData(Activity activity, Class<?> cls, String name, String data){
		Intent intent = new Intent(activity,cls);
		intent.putExtra(name, data);
		activity.startActivity(intent);
	}
	/**
	 * 启动网络设置
	 * @param activity 当前Activity
	 */
	public static void startSetNetActivity(Activity activity){
		Intent intent =  new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
		activity.startActivity(intent);
	}
	/**
	 * 启动系统设置
	 * @param activity 当前Activity
	 */
	public static void startSetActivity(Activity activity){
		Intent intent = new Intent(Settings.ACTION_SETTINGS);
		activity.startActivity(intent);
	}
}

