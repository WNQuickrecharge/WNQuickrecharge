package com.optimumnano.quickcharge.utils;

import android.util.Log;

import com.optimumnano.quickcharge.BuildConfig;

public final class LogUtils {
    private LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void e(String error) {
        if (BuildConfig.DEBUG) Log.e("quickcharge", error);
    }

    public static void d(String debug) {
        if (BuildConfig.DEBUG) Log.d("quickcharge", debug);
    }

    public static void v(String verbose) {
        if (BuildConfig.DEBUG) Log.e("quickcharge", verbose);
    }

    public static void i(String info) {
        if (BuildConfig.DEBUG) Log.i("quickcharge", info);
    }

    public static void w(String warn) {
        if (BuildConfig.DEBUG) Log.i("quickcharge", warn);
    }
}