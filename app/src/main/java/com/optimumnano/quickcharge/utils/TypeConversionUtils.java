package com.optimumnano.quickcharge.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 作者：凌章 on 2016/12/16 15:34
 * 邮箱：lilingzhang@longshine.com
 */

public class TypeConversionUtils {
    public static double toDouble(String str) {
        double d = 0;
        if (TextUtils.isEmpty(str))
            return 0;
        else
            try {
                d = Double.valueOf(str);
            } catch (ClassCastException e) {
                Log.e("ClassCastException", "String is not double");
            }
        return d;
    }

    public static int toInt(String str) {
        int i = 0;
        if (TextUtils.isEmpty(str))
            return 0;
        else
            try {
                i = Integer.valueOf(str);
            } catch (ClassCastException e) {
                Log.e("ClassCastException", "String is not int");
            }
        return i;
    }

    public static float toFloat(String str) {
        float f = 0;
        if (TextUtils.isEmpty(str))
            return 0;
        else
            try {
                f = Float.valueOf(str);
            } catch (ClassCastException e) {
                Log.e("ClassCastException", "String is not float");
            }
        return f;
    }

}
