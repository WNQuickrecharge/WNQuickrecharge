package com.optimumnano.quickcharge.utils;

/**
 * 作者：邓传亮 on 2017/4/6 14:15
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public interface SPConstant {
    /***sharedpreferences****用户信息的xml名******/
    String SP_USERINFO = "sp_userinfo";
    String SP_COOKIE = "sp_cookie";
    String SP_CITY = "sp_city";

    /***sharedpreferences****用户头像地址key******/
    String KEY_USERINFO_HEADIMG_URL="userinfo_headimg_url";
    /***sharedpreferences****用户性别key******/
    String KEY_USERINFO_SEX ="userinfo_sex";
    /***sharedpreferences****用户昵称key******/
    String KEY_USERINFO_NICKNAME ="userinfo_nickname";
    /***sharedpreferences****用户默认支付方式key******/
    String KEY_USERINFO_DEFPAYWAY ="userinfo_defpayway";
    /***sharedpreferences****用户钱包余额key******/
    String KEY_USERINFO_BALANCE ="userinfo_balance";
    /**
     * 用户登录的手机号
     */
    String KEY_USERINFO_MOBILE="userinfo_mobile";
    String KEY_USERINFO_COOKIE="userinfo_cookie";
    //String KEY_USERINFO_PAYPASSWORD="userinfo_pay_password";
    String KEY_USERINFO_IS_REMEMBER="userinfo_is_remember";
    String KEY_USERINFO_PASSWORD="userinfo_password";
//    String KEY_USERINFO_CURRENT_CITY="current_city";
    String KEY_USERINFO_CURRENT_LAT="lat";
    String KEY_USERINFO_CURRENT_LON="lon";
    String KEY_USERINFO_USER_ID="id";

}
