package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.CityStationHttpResp;
import com.optimumnano.quickcharge.bean.GunInfoHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by herry on 2017/5/16.
 */

public class GetGunInfoResult extends BaseChargeResult {
    private GunInfoHttpResp resp;

    public GunInfoHttpResp getResp() {
        return resp;
    }

    public GetGunInfoResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        resp = JSON.parseObject(response.body().string(), GunInfoHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
//        return 0;
    }
}
