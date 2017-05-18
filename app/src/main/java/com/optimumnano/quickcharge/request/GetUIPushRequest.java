package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.InternalConstants;
import com.optimumnano.quickcharge.manager.GetuiPushManager;
import com.optimumnano.quickcharge.net.HttpApi;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by herry on 2017/5/15.
 */

public class GetUIPushRequest extends BaseChargeRequest {
    private String registerId;

    public GetUIPushRequest(BaseResult result, String registerId) {
        super(result);
        this.registerId = registerId;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.set_registerid_url);
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
        HashMap<String, Object> requestJson = new HashMap<String, Object>();
        requestJson.put("registerId", registerId);
        requestJson.put("platform", GetuiPushManager.PushChannel.GETUI);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
