package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.optimumnano.quickcharge.bean.InvoiceOrder;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.List;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;

/**
 * Created by ds on 2017/4/22.
 */

public class InvoiceManager {
    /**
     * 获取没有开发票的交易记录
     */
    public void getInvoiceRecord(final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_invoice_consume);
        RequestParams params = new RequestParams(url);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<List<InvoiceOrder>>() {
            @Override
            public void onSuccess(List<InvoiceOrder> result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
            }
        });
    }

    /**
     * 提交发票订单
     * @param postage 邮费	decimal	邮费
     * @param consume_ids 消费ID
     * @param title 抬头	tring
     * @param invoice_amount 发票金额	decimal	发票金额
     * @param name 姓名
     * @param address 地址
     * @param mobile 手机号
     * @param pay_type 支付类别
     */
    public void addInvoiceOrder(double postage,String consume_ids,String title,double invoice_amount,
                                String name,String address,String mobile,int pay_type,
                                final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.add_invoice);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("postage",postage);
        ha.put("consume_ids",consume_ids);
        ha.put("title",title);
        ha.put("invoice_amount",invoice_amount);
        ha.put("name",name);
        ha.put("address",address);
        ha.put("mobile",mobile);
        ha.put("pay_type",pay_type);
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
            }
        });
    }

    /**
     * 获取已提交的发票历史订单()
     * @param callback  回调
     */
    public void getOrderlist(final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_invoice_orderlist);
        RequestParams params = new RequestParams(url);
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
            }
        });
    }


}
