package com.syhd.myweather.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author fada
 */
public class AppInfoUtil {
    public static final String SDCARD_SOFT_STORE = Environment.getExternalStorageDirectory() + "/htjxsdk/";
    public static final File SDCARD = Environment.getExternalStorageDirectory();
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static ProgressDialog mProgress = null;
    private static DisplayMetrics displayMetrics = new DisplayMetrics();
    private static int id;

    public static String toHexString(byte[] b) {
        // String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 生成Md5
     *
     * @param s 字符串
     * @return 字符串的MD5值
     */
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Json字符串转化成JSONObject
     *
     * @param str
     * @return JSONObject对象
     * @throws Exception
     */
    public static JSONObject stringToJSONObject(String str) throws Exception {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 方法描述：安装一个Apk
     *
     * @return 是否成功
     */
    public static boolean isAppInstall(Context context, String packname) {
        PackageManager manager = context.getPackageManager();
        @SuppressWarnings("rawtypes")
        List pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = (PackageInfo) pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase(packname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法描述：将资源目录下文件写入本地
     *
     * @param fileName :文件名
     * @param path     :路径名
     * @return 是否成功
     */
    public static boolean retrieveApkFromAssets(Context context,
                                                String fileName, String path) {
        boolean bRet = false;
        try {
            InputStream is = context.getAssets().open(fileName);

            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }

            fos.close();
            is.close();

            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }

    /**
     * 获取一个apk的包信息
     *
     * @param context         上下文
     * @param archiveFilePath apk本地路径
     * @return apk的包信息
     */
    public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_META_DATA);
        return apkInfo;
    }

    /**
     * 方法描述：获取imsi码
     *
     * @return imsi码
     */
    public static String getIMSI(Context context) {
        TelephonyManager phone = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = phone.getSubscriberId();
        return imsi == null ? "" : imsi;
    }

    /**
     * imei和mac地址的md5值
     *
     * @param context
     * @return imei和mac地址的md5值
     */
    public static String getXid(Context context) {
        String xid = "";
        xid = md5(getIMEI(context) + getMacAddress(context));
        return xid;

    }


    /**
     * 获取版本号
     *
     * @param context
     * @return 获取版本号
     */
    public static int getVersionCode(Context context) {

        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;


    }

    /**
     * 获取版本名
     *
     * @param context
     * @return 获取版本名
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取Sim卡状态
     *
     * @param context 上下文
     * @return 获取Sim卡状态码
     */
    public static int getSimState(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimState();
    }

    /**
     * 获取电话号码
     *
     * @param context 上下文
     * @return 获取电话号码
     * @throws Exception
     */
    public static String getPhoneNumber(Context context) throws Exception {
        String phoneNumber = "";
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            phoneNumber = tm.getLine1Number();
        } catch (Exception e) {
            throw new RuntimeException(
                    "android.permission.READ_PHONE_STATE should be add to AndroidManifest.xml!");
        }
        return phoneNumber == null ? "" : phoneNumber;
    }

    /**
     * 方法描述：获取MAC地址
     *
     * @param context 上下文
     * @return 获取MAC地址
     */
    public static String getMacAddress(Context context) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        String macAddr = sp.getString("mac", "");
        if (macAddr == "") {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            try {
                WifiInfo wifiInfo = wifi.getConnectionInfo();
                macAddr = wifiInfo.getMacAddress();
                if (macAddr != null && macAddr != "") {
                    Editor edit = sp.edit();
                    edit.putString("mac", macAddr);
                    edit.commit();
                } else {
                    return "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                ALog.d("android.permission.ACCESS_WIFI_STATE should be add to AndroidManifest.xml!");
            }
        }

        return macAddr;
    }

    /**
     * String转JsonArray数组
     *
     * @param str
     * @return JsonArray数组
     * @throws Exception
     */
    public static JSONArray stringToJSONArray(String str) throws Exception {
        try {
            return new JSONArray(str);
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage() + ":" + str, e);
        }

    }

    /**
     * 弹出进度条
     *
     * @param context
     * @param title
     * @param message
     * @param indeterminate
     * @param cancelable
     * @return 进度对话框
     */
    public static ProgressDialog showProgress(Context context,
                                              CharSequence title, CharSequence message, boolean indeterminate,
                                              boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(new OnCancelListener((Activity) context));
        dialog.show();
        mProgress = dialog;
        return dialog;
    }

    /**
     * 关闭进度条对话框
     */
    public static void closeProgress() {
        try {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出对话框
     *
     * @param context
     * @param strTitle
     * @param strText
     * @param icon
     * @param text
     */
    public static void showDialog(Activity context, String strTitle,
                                  String strText, int icon, String text) {
        Builder tDialog = new Builder(context);
        tDialog.setIcon(icon);
        tDialog.setTitle(strTitle);
        tDialog.setMessage(strText);
        tDialog.setPositiveButton(text, null);
        tDialog.show();
    }

    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法描述：将一个流转换成字符串
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 方法描述：给文件加时间戳
     */
    public static void updateFileTime(String dir, String fileName) {
        File file = new File(dir, fileName);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 方法描述：获取文件时间戳
     */
    public static long getFileTime(String dir, String fileName) {
        File file = new File(dir, fileName);
        return file.lastModified();
    }

    /**
     * 方法描述：检查SD卡是否存在
     */
    public static boolean isExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 返回屏幕宽(px)
     */
    public static int getScreenWidth(Activity activity) {
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 返回屏幕高(px)
     */
    public static int getScreenHeight(Activity activity) {
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 方法描述：获取IMEI
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String imei = tm.getDeviceId();
        return imei == null ? "" : imei;
    }

    /**
     * 方法描述：获取手机型号
     */
    public static String getPhoneModle(Context context) {
        return android.os.Build.MODEL;
    }

    /**
     * 启动一个通知栏
     *
     * @param context      上下文
     * @param contentTitle 标题
     * @param contentText  内容
     * @param icon         图标id  R.drawable.XXX
     * @param intent
     */
    /*public static void startNotification(Context context, String contentTitle,
                                         String contentText, int icon, Intent intent) {
        // 1.获取通知管理器
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // 2.创建通知对象
        long when = System.currentTimeMillis(); // 时间
        Notification notification = new Notification(icon, contentTitle, when);
        // 3.设置通知
        // 意图指定Activity
        PendingIntent pedningIntent = PendingIntent.getActivity(context, 100,
                intent, PendingIntent.FLAG_ONE_SHOT); // 定义待定意图
        notification.setLatestEventInfo(context, contentTitle, contentText,
                pedningIntent); // 设置通知的具体信息
        notification.flags = Notification.FLAG_AUTO_CANCEL; // 设置自动清除
        // notification.sound = Uri.parse("file:///mnt/sdcard/download.mp3"); //
        // 设置通知的声音
        // 4.发送通知
        manager.notify(id++, notification);
    }*/

    /**
     * 将long值大小格式化为有单位的KB MB GB
     *
     * @param context
     * @param size
     * @return 格式化后的KB MB GB
     */
    public static String formatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 获取手机自带可用内存rom.
     */
    public static long getAvailRom(Context context) {
        File path = Environment.getDataDirectory();
        // StatFs用于获取一个
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        //return Formatter.formatFileSize(context, blockSize * availableBlocks);
        return blockSize * availableBlocks;
    }

    /**
     * 获取手机总内存rom.
     */
    public static long getTotalRom(Context context) {
        File path = Environment.getDataDirectory();
        // StatFs用于获取一个
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();// 区别
        return blockSize * availableBlocks;
        //return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获取总运存大小 RAM
     *
     * @param context
     * @return 获取总运存大小 RAM
     */
    public static long getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            ALog.d(e.getMessage());
        }
        return initial_memory;
    }

    /**
     * 获取可用运存大小RAM
     *
     * @param context
     * @return 可用运存大小RAM
     */
    public static long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        System.out.println("可用内存---->>>" + mi.availMem / (1024 * 1024));
        return mi.availMem;
    }

    /**
     * Sdcard的可用空间
     *
     * @param context
     * @return Sdcard的可用空间
     */
    public static long getAvailSD(Context context) {
        // 判断是否有插入存储卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();// 获取sd路径
            // StatFs:检索有关整体上的一个文件系统的空间信息
            StatFs stat = new StatFs(path.getPath());
            // 一个文件系统的块大小，以字节为单位。
            long blockSize = stat.getBlockSize();
            // SD卡中可用的块数
            long availableBlocks = stat.getAvailableBlocks();
            //return Formatter.formatFileSize(context, blockSize* availableBlocks);
            return blockSize * availableBlocks;
        } else {
            return 0;
        }
    }

    /**
     * Sdcard的总空间
     *
     * @return Sdcard的总空间
     */
    public static long getALLAvailSdSize(Context context) {
        // 判断是否有插入存储卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            // StatFs:检索有关整体上的一个文件系统的空间信息
            StatFs stat = new StatFs(path.getPath());
            // 一个文件系统的块大小，以字节为单位。
            long blockSize = stat.getBlockSize();
            // SD卡中可用的块数
            long availableBlocks = stat.getBlockCount();
            //return Formatter.formatFileSize(context, blockSize* availableBlocks);
            return blockSize * availableBlocks;
        } else {
            return 0;
        }
    }

    public void token(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);

            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            byte[] signbyteArray = sign.toByteArray();
            CertificateFactory instance = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) instance
                    .generateCertificate(new ByteArrayInputStream(signbyteArray));
            byte[] encoded = cert.getEncoded();
            String string = new String(encoded, "gb2312");
            ALog.d("X509Certificate=" + cert.toString() + "string="
                    + string);
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
                ALog.d("" + e.toString());
            }
        }
    }

    static class OnCancelListener implements DialogInterface.OnCancelListener {
        Activity mcontext;

        OnCancelListener(Activity context) {
            this.mcontext = context;
        }

        public void onCancel(DialogInterface dialog) {
            this.mcontext.onKeyDown(4, null);
        }
    }

}
