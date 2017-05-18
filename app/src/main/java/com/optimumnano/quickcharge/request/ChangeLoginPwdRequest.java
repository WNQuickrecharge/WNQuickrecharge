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
 * Created by herry on 2017/5/16.
 */

public class ChangeLoginPwdRequest extends BaseChargeRequest {
    private String mobileNo;
    private String oldPwd;
    private String newPwd;

    public ChangeLoginPwdRequest(BaseResult result, String mobileNo, String oldPwd, String newPwd) {
        super(result);
        this.mobileNo = mobileNo;
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.modify_password_url);
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
        requestJson.put("mobile", mobileNo);
        requestJson.put("oldpwd", oldPwd);
        requestJson.put("newpwd", newPwd);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
