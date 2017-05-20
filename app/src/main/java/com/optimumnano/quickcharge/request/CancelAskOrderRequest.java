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
 * Created by jack on 2017/5/20.
 */

public class CancelAskOrderRequest extends BaseChargeRequest {
    private String askNo;

    public CancelAskOrderRequest(BaseResult result, String askNo) {
        super(result);
        this.askNo = askNo;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.cancel_ask);
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
        ha.put("ask_no", askNo);
        String json = JSON.toJSONString(ha);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
