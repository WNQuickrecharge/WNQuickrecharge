package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.BaseHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by herry on 2017/5/17.
 */

public class RegisterResult extends BaseChargeResult {
    BaseHttpResp resp;

    public BaseHttpResp getResp() {
        return resp;
    }

    public RegisterResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        resp = JSON.parseObject(response.body().string(), BaseHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}