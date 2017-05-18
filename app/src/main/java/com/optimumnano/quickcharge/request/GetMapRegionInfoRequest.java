package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.InternalConstants;
import com.optimumnano.quickcharge.net.HttpApi;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by herry on 2017/5/15.
 */

public class GetMapRegionInfoRequest extends BaseChargeRequest {
    private PreferencesHelper mHelper;

    public GetMapRegionInfoRequest(BaseResult result, PreferencesHelper helper) {
        super(result);
        mHelper = helper;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.region_station_url);
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
        requestJson.put("lat", mHelper.getLocation().lat);
        requestJson.put("lng", mHelper.getLocation().lng);
        requestJson.put("distance", mHelper.showDistance());
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
