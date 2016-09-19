package com.syhd.myweather.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 创建一个简单的异步的任务 去执行异步的操作.
 *
 * @author Seny
 */
public abstract class SimpleAsyncTask {
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            onPostExecute();
        }
    };

    /**
     * 耗时任务执行之前调用的方法，可选重写
     */
    public void onPreExecute() {

    }

    /**
     * 耗时任务执行后调用的方法. 可选重写
     */
    public void onPostExecute() {

    }

    /**
     * 在后台运行的一个耗时的任务.
     */
    public abstract void doInBackground();

    /**
     * 开启一个异步的任务.
     */
    public void execute() {
        onPreExecute();
        new Thread() {
            public void run() {
                doInBackground();
                handler.sendEmptyMessage(0);
            }

        }.start();

    }
}
