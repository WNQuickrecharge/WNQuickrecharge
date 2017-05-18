package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.InternalConstants;
import com.optimumnano.quickcharge.net.HttpApi;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by herry on 2017/5/17.
 */

public class PayChargeBalanceRequest extends BaseChargeRequest {
    private String order_no;
    private double pay_cash;

    public PayChargeBalanceRequest(BaseResult result, String order_no, double pay_cash) {
        super(result);
        this.order_no = order_no;
        this.pay_cash = pay_cash;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.pay_charge_balance);
    }

    @Override
    protected ArrayMap<String, String> getHeaders() {
        return null;
    }

    @Override
    protected ArrayMap<String, String> getParams() {
        return null;
    }

    @Override
    protected RequestBody getRequestBody() {
        HashMap<String, Object> ha = new HashMap<>();
        ha.put("order_no", order_no);
        ha.put("pay_cash", pay_cash);
        String json = JSON.toJSONString(ha);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
