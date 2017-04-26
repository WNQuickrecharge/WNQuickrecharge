package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.bean.RechargeGunBean;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.List;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;

/**
 * Created by ds on 2017/4/10.
 * 订单处理
 */
public class OrderManager {

    /**
     * 获取订单列表
     * @param pageSize 当前页，1开始
     * @param pageCount 每页显示条数
     * @param callback 回调
     */
    public void getAllOrderlist(int pageSize, int pageCount, final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.order_list);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("page_size",pageCount);
        ha.put("cur_page",pageSize);

        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        params.setBodyContent(JSON.toJSONString(ha));
        MyHttpUtils.getInstance().post(params, new HttpCallback<List<OrderBean>>() {
            @Override
            public void onSuccess(List<OrderBean> result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }


    /**
     * 获取充电枪的信息
     * @param gun_code 充电枪好
     * @param callback
     */
    public void getGunInfo(String gun_code,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_guninfo);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("gun_code",gun_code);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<RechargeGunBean>() {
            @Override
            public void onSuccess(RechargeGunBean result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }

    /**
     * 下单
     * @param gun_code 充电枪号
     * @param frozen_cash 预交金额
     * @param callback 回调
     */
    public void addOrder(String gun_code,String frozen_cash,int payway,final ManagerCallback callback){
        if (StringUtils.isEmpty(frozen_cash)){
            callback.onFailure("预付金额不能为空");
            return;
        }
        String url = HttpApi.getInstance().getUrl(HttpApi.add_order);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("pay_type",payway);
        ha.put("gun_code",gun_code);
        ha.put("frozen_cash",frozen_cash);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
//        ha.put(" ask_no"," ask_no");//如果是移动补电车呼叫则添加该参数
        params.setBodyContent(JSON.toJSONString(ha));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }


    /**
     * 获取握手状态
     * @param gun_code 充电枪号
     * @param callback 回调
     */
    public void getGunConnect (String gun_code,final ManagerCallback callback,final int requestCode){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_gunConnect);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("gun_code",gun_code);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result,requestCode);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg,requestCode);
            }
        });
    }

    /**
     * 开始充电
     * @param order_no 订单号
     * @param callback 回调
     */
    public void startCharge (String order_no,final ManagerCallback callback,final int requestCode){
        String url = HttpApi.getInstance().getUrl(HttpApi.start_charge);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result,requestCode);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg,requestCode);
            }
        });
    }

    /**
     * 充电进度查询
     * @param order_no 订单号
     * @param test_no 测试数据，从1到10，每循环一次加一
     * @param callback 回调
     */
    public void getChargeProgress (String order_no,int test_no,final ManagerCallback callback,final int requestCode){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_chargeProgress);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        ha.put("test_no",test_no);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result,requestCode);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg,requestCode);
            }
        });
    }

    /**
     * 支付成功  测试
     * @param order_no 订单号
     * @param pay_cash 支付金额
     * @param callback 回调
     */
    public void startPay (String order_no,double pay_cash,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.pay_charge_balance);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        ha.put("pay_cash",pay_cash);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }

    /**
     * 取消订单
     * @param order_no 订单号
     * @param callback 回调
     */
    public void cancelOrder (String order_no,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.cancel_order);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }

    /**
     * 删除订单
     * @param order_no 订单号
     * @param callback 回调
     */
    public void deleteOrder (String order_no,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.delete_order);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }

    /**
     * 充电控制中的结束充电
     * @param order_no 订单号
     * @param callback 回调
     */
    public void stopCharge(String order_no,final ManagerCallback callback,final int requestCode){
        String url = HttpApi.getInstance().getUrl(HttpApi.stop_charge);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result,requestCode);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg,requestCode);
            }
        });
    }

    /**
     * 获取签名
     * @param order_no 订单号
     * @param payway 支付方式
     * @param callback 回调
     */
    public void getSign(String order_no,int payway,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_ordersign);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("pay_type",payway);
        ha.put("order_no",order_no);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
//        ha.put(" ask_no"," ask_no");//如果是移动补电车呼叫则添加该参数
        params.setBodyContent(JSON.toJSONString(ha));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }

    public static void getOrderByOrderNo(String order_no,final  ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.getby_orderno);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
//        ha.put(" ask_no"," ask_no");//如果是移动补电车呼叫则添加该参数
        params.setBodyContent(JSON.toJSONString(ha));
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }
}
