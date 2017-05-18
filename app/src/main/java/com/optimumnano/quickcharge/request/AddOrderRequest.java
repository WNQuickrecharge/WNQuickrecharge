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

public class AddOrderRequest extends BaseChargeRequest {
    private int payway;
    private String gun_code;
    private String frozen_cash;

    public AddOrderRequest(BaseResult result, int payway, String gun_code, String frozen_cash) {
        super(result);
        this.payway = payway;
        this.gun_code = gun_code;
        this.frozen_cash = frozen_cash;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.add_order);
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
        ha.put("pay_type", payway);
        ha.put("gun_code", gun_code);
        ha.put("frozen_cash", frozen_cash);
        String json = JSON.toJSONString(ha);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
