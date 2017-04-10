package com.optimumnano.quickcharge.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.xutils.x;


/**
 * 描述：
 * sharepreferences操作
 * Created by ds on 2017/1/13.
 */
public class SharedPreferencesUtil {

    /**
     * 向SharedPreferences中写入int类型数据
     *
     * @param name  对应的xml文件名称
     * @param key   键
     * @param value 值
     */
    public static void putValue(String name, String key,
                                int value) {
        Editor sp = getEditor(name);
        sp.putInt(key, value);
        sp.commit();
    }

    /**
     * 向SharedPreferences中写入boolean类型的数据
     *
     * @param name  对应的xml文件名称
     * @param key   键
     * @param value 值
     */
    public static void putValue(String name, String key,
                                boolean value) {
        Editor sp = getEditor(name);
        sp.putBoolean(key, value);
        sp.commit();
    }

    /**
     * 向SharedPreferences中写入String类型的数据
     *
     * @param name  对应的xml文件名称
     * @param key   键
     * @param value 值
     */
    public static void putValue(String name, String key,
                                String value) {
        Editor sp = getEditor(name);
        sp.putString(key, value);
        sp.commit();
    }

    /**
     * 向SharedPreferences中写入float类型的数据
     *
     * @param name  对应的xml文件名称
     * @param key   键
     * @param value 值
     */
    public static void putValue(String name, String key,
                                float value) {
        Editor sp = getEditor(name);
        sp.putFloat(key, value);
        sp.commit();
    }

    /**
     * 向SharedPreferences中写入long类型的数据
     *
     * @param name  对应的xml文件名称
     * @param key   键
     * @param value 值
     */
    public static void putValue(String name, String key,
                                long value) {
        Editor sp = getEditor(name);
        sp.putLong(key, value);
        sp.commit();
    }

    /**
     * 从SharedPreferences中读取int类型的数据
     *
     * @param name     对应的xml文件名称
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static int getValue(String name, String key,
                               int defValue) {
        SharedPreferences sp = getSharedPreferences(name);
        int value = sp.getInt(key, defValue);
        return value;
    }

    /**
     * 从SharedPreferences中读取boolean类型的数据
     *
     * @param name     对应的xml文件名称
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static boolean getValue(String name, String key,
                                   boolean defValue) {
        SharedPreferences sp = getSharedPreferences(name);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    /**
     * 从SharedPreferences中读取String类型的数据
     *
     * @param name     对应的xml文件名称
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static String getValue(String name, String key,
                                  String defValue) {
        SharedPreferences sp = getSharedPreferences(name);
        String value = sp.getString(key, defValue);
        return value;
    }

    /**
     * 从SharedPreferences中读取float类型的数据
     *
     * @param name     对应的xml文件名称
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static float getValue(String name, String key,
                                 float defValue) {
        SharedPreferences sp = getSharedPreferences(name);
        float value = sp.getFloat(key, defValue);
        return value;
    }

    /**
     * 从SharedPreferences中读取long类型的数据
     *
     * @param name     对应的xml文件名称
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    public static long getValue(String name, String key,
                                long defValue) {
        SharedPreferences sp = getSharedPreferences(name);
        long value = sp.getLong(key, defValue);
        return value;
    }

    //获取Editor实例  
    public static Editor getEditor(String name) {
        return getSharedPreferences(name).edit();
    }

    //获取SharedPreferences实例  
    private static SharedPreferences getSharedPreferences(String name) {
        return x.app().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

}
