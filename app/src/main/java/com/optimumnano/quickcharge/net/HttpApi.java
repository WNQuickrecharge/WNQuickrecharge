package com.optimumnano.quickcharge.net;

/**
 * Created by ds on 2017/4/1.
 * url管理
 */
public class HttpApi {
    private static HttpApi instance;
    public static HttpApi getInstance(){
        if (instance == null){
            synchronized (HttpApi.class){
                instance = new HttpApi();
            }
        }
        return instance;
    }
    private static final String baseUrl = "http://112.74.44.166:4840/";
    public String getUrl(String api){
        return baseUrl+api;
    }

    public static final String login_url = "capp/user/login";
    public static final String register_checknum = "capp/user/register_code";
    public static final String register_url = "capp/user/register";
    public static final String forget_password_url = "capp/user/forget_pwd";
    public static final String modify_password_url = "capp/user/modify_pwd";
    public static final String modify_userinfo_url = "capp/user/set_userinfo";//修改个人资料
    public static final String logout_url = "capp/user/logout";//登出
    public static final String modify_pay_password_url = "capp/user/set_paypwd";//
    public static final String get_password_url = "capp/user/get_paypwd";//
    public static final String forget_pay_password_url = "capp/user/forget_paypwd";//

}
