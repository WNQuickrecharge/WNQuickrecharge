package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.MyApplication;
import com.optimumnano.quickcharge.bean.BaseHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by herry on 2017/5/15.
 */

public class GetUIPushResult extends BaseChargeResult {
    public GetUIPushResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        BaseHttpResp resp = JSON.parseObject(response.body().string(), BaseHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        MyApplication.getuiflag = true;
        return HttpResult.SUCCESS;
    }
}
