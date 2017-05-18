package com.optimumnano.quickcharge.http;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class CommonUtils {

    private static final String SCHEMA_HTTP = "http://%s";
    private static final String SCHEMA_HTTPS = "https://%s";

    public static void silentClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                //
            }
        }
    }

    public static String fixRequestUrl(String host, String action, Boolean... https) {
        if (isStringInvalid(action)) {
            return null;
        }
        if (action.contains("http")) {
            return action;
        }
        if (!action.startsWith("/")) {
            action = "/" + action;
        }
        String schema = null;
        if (https == null || https.length < 1) {
            schema = SCHEMA_HTTP;
        } else {
            boolean flag = https[0];
            schema = flag ? SCHEMA_HTTPS : SCHEMA_HTTP;
        }
        return new StringBuilder().append(String.format(schema, host)).append(action).toString();
    }

    /* 格式化android sdk版本，整型转字符串 */
    public static String formatSdkLevel() {
        return String.valueOf(android.os.Build.VERSION.SDK_INT);
    }

    /* 获取android应用名称 */
    public static String getAppName(Context context) {
        PackageInfo pInfo = null;
        try {
            PackageManager pm = context.getPackageManager();
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return pm.getApplicationLabel(pInfo.applicationInfo).toString();
        } catch (Exception e) {
            return "";
        }
    }

    /* 获取android应用版本名称 */
    public static String getAppVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /* 获取android应用版本号 */
    public static int getAppCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    /* 转换整数为字符串 */
    public static String i2s(int i) {
        try {
            return String.valueOf(i);
        } catch (Exception e) {
            return "";
        }
    }

    /* 转换字符串为整数 */
    public static int s2i(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }

    /* 判断字符串非法 */
    public static boolean isStringInvalid(String str) {
        return str == null || str.trim().length() <= 0 || str.equalsIgnoreCase("null");
    }

    public static boolean isSdcardReady() {
        String state = Environment.getExternalStorageState();
        if (isStringInvalid(state)) {
            return false;
        }
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public static String genKeyForUrl(String url) {
        String cacheKey = MD5Utils.getStringMD5(url);
        if (CommonUtils.isStringInvalid(cacheKey)) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apkFile
     */
    public static void installApk(Context context, File apkFile) {
        apkFile.setReadable(true, false);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void installApk(Context context, String apkFilePath) {
        installApk(context, new File(apkFilePath));
    }
}
