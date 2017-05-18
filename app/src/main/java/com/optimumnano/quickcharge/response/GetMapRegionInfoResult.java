package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.MapRegionInfoHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by herry on 2017/5/15.
 */

public class GetMapRegionInfoResult extends BaseChargeResult {
    private MapRegionInfoHttpResp mapRegionInfoHttpResp;

    public MapRegionInfoHttpResp getMapRegionInfoHttpResp() {
        return mapRegionInfoHttpResp;
    }

    public GetMapRegionInfoResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        mapRegionInfoHttpResp = JSON.parseObject(response.body().string(), MapRegionInfoHttpResp.class);
        if (mapRegionInfoHttpResp == null) {
            return HttpResult.FAIL;
        }
        if (mapRegionInfoHttpResp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}
