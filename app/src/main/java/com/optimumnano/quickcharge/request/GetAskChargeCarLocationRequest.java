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
 * Created by chenwenguang on 2017/5/20.
 */

public class GetAskChargeCarLocationRequest extends BaseChargeRequest {
    private String carVin;
    public GetAskChargeCarLocationRequest(BaseResult result, String carVin) {
        super(result);
        this.carVin=carVin;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.get_chargeCar_location);
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
        requestJson.put("car_vin", carVin);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
