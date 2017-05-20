package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.MapNearCarInfoHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by mfwn on 2017/5/19.
 */

public class GetAskChargeResult extends BaseChargeResult {
    private MapNearCarInfoHttpResp resp;

    public MapNearCarInfoHttpResp getResp() {
        return resp;
    }

    public GetAskChargeResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        resp = JSON.parseObject(response.body().string(), MapNearCarInfoHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}
