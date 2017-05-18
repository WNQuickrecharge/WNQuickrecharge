package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.UserAccountResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by herry on 2017/5/14.
 */

public class GetUserInfoResult extends BaseChargeResult {
    private UserAccountResp userAccountResp;

    public UserAccountResp getUserAccountResp() {
        return userAccountResp;
    }

    public GetUserInfoResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        userAccountResp = JSON.parseObject(response.body().string(), UserAccountResp.class);
        if (userAccountResp == null) {
            return HttpResult.FAIL;
        }
        if (userAccountResp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}
