package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.InternalConstants;
import com.optimumnano.quickcharge.net.HttpApi;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by mfwn on 2017/5/19.
 */

public class GetAskChargeRequest extends BaseChargeRequest {
    private PreferencesHelper mHelper;

    public GetAskChargeRequest(BaseResult result, PreferencesHelper helper) {
        super(result);
        mHelper = helper;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.get_ask_charge);
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
        HashMap<String, Object> requestJson = new HashMap<>();
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
