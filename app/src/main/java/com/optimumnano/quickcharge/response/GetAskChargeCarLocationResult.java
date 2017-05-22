package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.GetAskChargeCarLocationHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by chenwenguang on 2017/5/20.
 */

public class GetAskChargeCarLocationResult extends BaseChargeResult {
    private GetAskChargeCarLocationHttpResp resp;

    public GetAskChargeCarLocationHttpResp getResp() {
        return resp;
    }

    public GetAskChargeCarLocationResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        resp = JSON.parseObject(response.body().string(), GetAskChargeCarLocationHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}
