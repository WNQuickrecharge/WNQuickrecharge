/*
 * Copyright (c) 2015. 深圳市讯联智付网络有限公司版权所有
 */

package com.optimumnano.quickcharge.utils;


import android.text.TextUtils;

import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 * 字符串操作工具包
 *
 * @author W.Y
 * @version 1.0
 * @created 2014年5月21日 下午4:18:16
 */
public class StringUtils {
    private static final String TAG = StringUtils.class.getSimpleName();
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    //private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
    private final static Pattern banker = Pattern
            .compile("^\\d{16}|\\d{19}$");
    private static final Pattern mobile = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是正确的银行卡号
     *
     * @param bank
     * @return
     */
    public static boolean isBankCard(CharSequence bank) {
        if (TextUtils.isEmpty(bank)) {
            return false;
        }
        return banker.matcher(bank).matches();
    }

    public static boolean luhnCheck(CharSequence number) {
        int sumOdd = 0;
        int sumEven = 0;
        int length = number.length();
        int[] wei = new int[length];
        for (int i = 0; i < number.length(); i++) {
            wei[i] = Integer.parseInt(number.subSequence(length - i - 1, length
                    - i).toString());// 从最末一位开始提取，每一位上的数值
        }
        for (int i = 0; i < length / 2; i++) {
            sumOdd += wei[2 * i];
            if ((wei[2 * i + 1] * 2) > 9)
                wei[2 * i + 1] = wei[2 * i + 1] * 2 - 9;
            else
                wei[2 * i + 1] *= 2;
            sumEven += wei[2 * i + 1];
        }
        System.out.println("奇数位的和是：" + sumOdd);
        System.out.println("偶数位的和是：" + sumEven);
        if ((sumOdd + sumEven) % 10 == 0) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isMobile(CharSequence mobileStr) {
        if (TextUtils.isEmpty(mobileStr)) {
            return false;
        }
        return mobile.matcher(mobileStr).matches();
    }


    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null) return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 返回长度为【strLength】的随机数，在前面补0
     */
    public static String getFixLenthString(int strLength) {

        Random rm = new Random();
        // 获得随机数
        int pross = (int) ((1 + rm.nextDouble()) * Math.pow(10, strLength));
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    /**
     * 描述：
     * 校验电话号码输入框的输入内容
     * 肯尼亚电话号码格式未知？？
     *
     * @param phoneNum
     * @return
     * @author W.Y
     * @version 1.0
     * @created 2014年5月20日 下午4:06:39
     */
    public static boolean checkMobileNO(String phoneNum) {
        if (phoneNum == null || phoneNum.length() < 1) {
            return false;
        }
        String expression = "(^[0-9]{3,4}[0-9]{7,8}$)|(^[0-9]{7,8}$)|(^[0-9]{3,4}[0-9]{7,8}$)|(^0{0,1}[0-9]{11}$)|(^\\+{0,1}86[0-9]{11}$)";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    public static String formatNumToMoney(String string) {
        if (string == null) return "0";
        String str = "0.00";
        if (string.equals(str)) {
            str = string;
        } else {
            str = string.startsWith(".") ? "0" + str : string;
            try {
                double f = Double.valueOf(str);
                DecimalFormat format = new DecimalFormat("###0.00");
                str = format.format(f);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return str;
    }

    public static String formatNumToYuan(String string) {
        String str = "0.00";
        try {
            double f = Double.valueOf(str) / 100;
            DecimalFormat format = new DecimalFormat("###0.00");
            str = format.format(f);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return str;
    }

    public static String formatDouble(double res) {
        String str = "0.00";
        try {
            double f = Double.valueOf(res);
            DecimalFormat format = new DecimalFormat("0.00");
            str = format.format(f);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return str;
    }

}
