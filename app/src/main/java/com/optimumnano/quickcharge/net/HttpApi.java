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
//    private static final String baseUrl = "http://172.200.31.57:4840/";//临时测试
    public String getUrl(String api){
        return baseUrl+api;
    }

    public static final String login_url = "capp/user/login";
    public static final String register_checknum = "capp/user/register_code";
    public static final String register_url = "capp/user/register";
    public static final String forget_password_url = "capp/user/forget_pwd";
    public static final String modify_password_url = "capp/user/modify_pwd";
    public static final String modify_userinfo_url = "capp/user/set_userinfo";//修改个人资料
    public static final String upload_headView_url = "capp/user/upload_avatar";//上传头像
    public static final String get_transaction_bill = "capp/order/user_consume";//获取交易明细
    public static final String balance_deposit_url = "capp/order/add_deposit";//钱包余额充值
    public static final String  region_pile_url = "capp/bs/region_pile";//修改个人资料
    public static final String logout_url = "capp/user/logout";//登出
    public static final String modify_pay_password_url = "capp/user/set_paypwd";//
    public static final String get_password_url = "capp/user/get_paypwd";//
    public static final String forget_pay_password_url = "capp/user/forget_paypwd";//
    public static final String order_list = "capp/order/all_orders";//
    public static final String get_guninfo = "capp/bs/get_gunInfo";//获取充电枪详情
    public static final String add_order = "capp/order/add_order";//下单
    public static final String get_gunConnect= "capp/bs/get_gunConnect";//获取握手状态
    public static final String start_charge = "capp/bs/start_charge";//开始充电
    public static final String get_chargeProgress = "capp/bs/get_chargeProgress";//获取充电过程中的时间和电量
    public static final String pay_success = "capp/order/pay_succ";//支付成功  测试
    public static final String cancel_order = "capp/order/cancel_order";//取消订单
    public static final String delete_order = "capp/order/delete_order";//删除订单
    public static final String get_collection = "capp/user/get_collection";//获取个人收藏站点信息
    public static final String add_collection = "capp/user/AddCollection";//增加收藏站点


}
