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

public class RegisterRequest extends BaseChargeRequest {
    private String mobile;
    private String checkNum;
    private String pwd;

    public RegisterRequest(BaseResult result, String mobile, String checkNum, String pwd) {
        super(result);
        this.mobile = mobile;
        this.checkNum = checkNum;
        this.pwd = pwd;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.register_url);
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
        requestJson.put("mobile", mobile);
        requestJson.put("purpose", "RegisterCApp");
        requestJson.put("validate_code", checkNum);
        requestJson.put("password", pwd);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
