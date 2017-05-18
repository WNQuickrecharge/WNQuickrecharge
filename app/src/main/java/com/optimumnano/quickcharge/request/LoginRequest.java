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
 * Created by herry on 2017/5/15.
 */

public class LoginRequest extends BaseChargeRequest {
    private String username;
    private String pwd;
    private int userType;

    public LoginRequest(BaseResult result, String username, String pwd, int userType) {
        super(result);
        this.username = username;
        this.pwd = pwd;
        this.userType = userType;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.login_url);
    }

    @Override
    protected ArrayMap<String, String> getHeaders() {
        ArrayMap<String, String> headers = new ArrayMap<String, String>();
        headers.put(InternalConstants.HEADER_USER_AGENT, HttpApi.USER_AGENT);
        return headers;
    }

    @Override
    protected ArrayMap<String, String> getParams() {
        return null;
    }

    @Override
    protected RequestBody getRequestBody() {
        HashMap<String, Object> requestJson = new HashMap<String, Object>();
        requestJson.put("mobile", username);
        requestJson.put("pwd", pwd);
        requestJson.put("type", userType);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
