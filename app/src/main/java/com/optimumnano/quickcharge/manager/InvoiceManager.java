package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.optimumnano.quickcharge.bean.InvoiceOrder;
import com.optimumnano.quickcharge.bean.InvoiceOrderRsp;
import com.optimumnano.quickcharge.bean.InvoiceRecordBean;
import com.optimumnano.quickcharge.bean.InvoiceSignRsp;
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
     */
    public void addInvoiceOrder(double postage,String consume_ids,String title,double invoice_amount,
                                String name,String address,String mobile,String regPhone,String regAddress,
                                String bankCard,String indentifyNum,String remark,
                                final ManagerCallback callback){
        if (StringUtils.isEmpty(mobile)){
            callback.onFailure("电话号码不能为空");
            return;
        }
        String url = HttpApi.getInstance().getUrl(HttpApi.add_invoice);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        if (!StringUtils.isEmpty(regPhone)){
            ha.put("tax_no",indentifyNum);
            ha.put("register_addr",regAddress);
            ha.put("register_phone",regPhone);
            ha.put("bank_num",bankCard);
            ha.put("remark",remark);
        }
//        ha.put("postage",postage);
        ha.put("consume_ids",consume_ids);
        ha.put("title",title);
        ha.put("invoice_amount",invoice_amount);
        ha.put("name",name);
        ha.put("address",address);
        ha.put("mobile",mobile);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<InvoiceOrderRsp>() {
            @Override
            public void onSuccess(InvoiceOrderRsp result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
            }
        });
    }
    public void addInvoiceOrder(double postage,String consume_ids,String title,double invoice_amount,
                                String name,String address,String mobile,
                                final ManagerCallback callback){
        addInvoiceOrder(postage,consume_ids,title,invoice_amount, name,address,mobile,
                "","","","","",
        callback);
    }

    /**
     * 获取已提交的发票历史订单()
     * @param callback  回调
     */
    public void getOrderlist(final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_invoice_orderlist);
        RequestParams params = new RequestParams(url);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<List<InvoiceRecordBean>>() {
            @Override
            public void onSuccess(List<InvoiceRecordBean> result, int httpCode) {
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
     * 获取发票订单签名
     * @param order_no 订单号
     * @param payType 支付类型
     * @param callback
     */
    public void getInvoiceSign(String order_no,int payType,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_invoice_sign);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("order_no",order_no);
        ha.put("pay_type",payType);
        params.setBodyContent(JSON.toJSONString(ha));
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<InvoiceSignRsp>() {
            @Override
            public void onSuccess(InvoiceSignRsp result, int httpCode) {
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
     *  发票余额支付
     * @param order_no
     * @param pay_cash
     * @param callback
     */
    public void payBalance(String order_no,double pay_cash,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.pay_invoice_balance);
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
            }
        });
    }


}
