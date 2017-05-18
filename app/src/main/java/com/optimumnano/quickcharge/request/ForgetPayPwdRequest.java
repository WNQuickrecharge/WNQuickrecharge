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

public class ForgetPayPwdRequest extends BaseChargeRequest {
    private String mobile;
    private String purpose;
    private String validate_code;
    private String new_paypwd;

    public ForgetPayPwdRequest(BaseResult result, String mobile, String purpose, String validate_code, String new_paypwd) {
        super(result);
        this.mobile = mobile;
        this.purpose = purpose;
        this.validate_code = validate_code;
        this.new_paypwd = new_paypwd;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.forget_pay_password_url);
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
        requestJson.put("mobile", mobile);
        requestJson.put("purpose", purpose);
        requestJson.put("validate_code", validate_code);
        requestJson.put("new_paypwd", new_paypwd);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
