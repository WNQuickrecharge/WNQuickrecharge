package com.optimumnano.quickcharge.net;

import android.os.Build;

import com.optimumnano.quickcharge.BuildConfig;

import java.util.Locale;

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
//    private static final String baseUrl = "http://112.74.44.166:4830/";
    private static final String baseUrl = "http://119.23.71.104:4711/";//生产库地址
//    private static final String baseUrl = "http://172.200.28.132:4840/";//临时测试
//    private static final String baseUrl="http://120.77.149.109:4720/"; //测试库地址修改了
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
    public static final String get_pay_orderinfo_deposit = "capp/order/add_deposit";//上传支付金额和支付方式获取调起支付的orderInfo
    public static final String get_accountinfo = "capp/user/get_accountinfo";//获取账户信息
    public static final String  region_pile_url = "capp/bs/region_stations";//获取附近站点信息
    public static final String logout_url = "capp/user/logout";//登出
    public static final String modify_pay_password_url = "capp/user/set_paypwd";//
    public static final String get_password_url = "capp/user/get_paypwd";//获取支付密码
    public static final String forget_pay_password_url = "capp/user/forget_paypwd";//
    public static final String order_list = "capp/order/all_orders";//
    public static final String get_guninfo = "capp/bs/get_gunInfo";//获取充电枪详情
    public static final String get_guninfo1 = "capp/bs/get_gunInfo1";//获取充电枪详情
    public static final String add_order = "capp/order/add_order";//下单
    public static final String get_gunConnect= "capp/bs/get_gunConnect";//获取握手状态
    public static final String start_charge = "capp/bs/start_charge";//开始充电
    public static final String get_chargeProgress = "capp/bs/get_chargeProgress";//获取充电过程中的时间和电量
    public static final String pay_charge_balance = "capp/order/pay_charge_balance";//余额支付  测试
    public static final String cancel_order = "capp/order/cancel_order";//取消订单
    public static final String delete_order = "capp/order/delete_order";//删除订单
    public static final String get_collection = "capp/user/get_collection";//获取个人收藏站点信息
    public static final String add_collection = "capp/user/add_collection";//增加收藏站点
    public static final String about_url = baseUrl+"aboutus.html";//关于我们
    public static final String delete_collection = "capp/user/delete_collection";//增加收藏站点
    public static final String stop_charge = "capp/bs/stop_charge";//结束付款
    public static String region_station_url="capp/bs/region_stations";
    public static String region_carpile="capp/bs/region_carpile";
    public static final String get_station_detail = "capp/bs/get_station_detail";//获取站点详情信息
    public static final String set_registerid_url = "capp/user/set_registerid";//提交推送ID
    public static final String USER_AGENT = "android/" + Build.VERSION.SDK_INT + "/quickcharge/" + BuildConfig.VERSION_NAME+"/"
            + Locale.getDefault();

    public static final String ask_charge="capp/bs/ask_charge";

    public static final String  get_ordersign = "capp/pay/get_ordersign";//获取签名
    public static final String  get_city_stations = "capp/bs/get_city_stations";//获取当前城市所有站点信息
    public static final String get_invoice_consume = "capp/invoice/get_invoice_consume";//获取没有开发票的交易记录
    public static final String pay_invoice_balance = "capp/invoice/pay_invoice_balance";//发票订单余额支付成功时调用
    public static final String long_connet_url = baseUrl+"capp_monitor";//充电过程中长连接地址
    public static final String add_invoice = "capp/invoice/add_invoice";//提交发票订单
    public static final String get_invoice_orderlist = "capp/invoice/get_invoice_orderlist";//获取已提交的发票历史订单()
    public static final String get_invoice_sign = "capp/invoice/get_invoice_sign";//获取微信或者支付宝的签名，余额支付不需要调用此接口
    public static final String getby_orderno = "capp/order/getby_orderno";//根据订单号获取订单信息
    public static final String update_apk_url = "http://172.200.29.72:8080/update/apk.json";//本地测试升级apk地址


}
