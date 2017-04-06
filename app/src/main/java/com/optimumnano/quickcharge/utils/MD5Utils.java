package com.optimumnano.quickcharge.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者：凌章 on 2016/10/17 19:25
 * 邮箱：lilingzhang@longshine.com
 */

public class MD5Utils {
    public static String encodeMD5(String password) {//密码加密
        try {
            return getMD5(password);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return getString(m);
    }

    private static String getString(byte[] b) {
        StringBuilder buf = new StringBuilder();
        for (byte aB : b) {
            int a = aB;
            if (a < 0)
                a += 256;
            if (a < 16)
                buf.append("0");
            buf.append(Integer.toHexString(a));

        }
        return buf.toString();  //32位
    }
}
