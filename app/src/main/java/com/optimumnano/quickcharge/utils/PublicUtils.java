package com.optimumnano.quickcharge.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangjiancheng on 2017/6/8.
 */

public class PublicUtils {
    /**
     * 校验车牌
     * @param str
     * @return
     */
    public static boolean isCar(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[京,津,渝,沪,冀,晋,辽,吉,黑,苏,浙,皖,闽,赣,鲁,豫,鄂,湘,粤,琼,川,贵,云,陕,秦,甘,陇,青,台,蒙,桂,宁,新,藏,澳,军,海,航,警]{1}[A-Z]{1}[0-9,A-Z]{5,6}$"); // 验证车牌号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * {"plate":"粤F12345","vin":"LGAX4C448F8012597","terminalnum":"1507010273"}
     * 扫码解析出车牌
     * @param jsonStr
     * @return
     */
    public static String getPlateValue(String jsonStr ,String strs) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String str = jsonObject.getString(strs);
            return  str;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
