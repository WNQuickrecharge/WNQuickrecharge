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

public class GetOrderSignRequest extends BaseChargeRequest {

    private int payway;
    private String order_no;

    public GetOrderSignRequest(BaseResult result, int payway, String order_no) {
        super(result);
        this.payway = payway;
        this.order_no = order_no;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.get_ordersign);
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
        HashMap<String, Object> ha = new HashMap<String, Object>();
        ha.put("pay_type", payway);
        ha.put("order_no", order_no);
        String json = JSON.toJSONString(ha);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}