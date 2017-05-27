package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.search.core.PoiInfo;
import com.optimumnano.quickcharge.bean.SuggestionInfo;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.InternalConstants;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_NICKNAME;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by herry on 2017/5/17.
 */

public class AskChargeRequest extends BaseChargeRequest {
    private PreferencesHelper helper;
    private String mobile;
    private String name;
    private String address;
    private String plate;
    private PoiInfo info;

    public AskChargeRequest(BaseResult result, PoiInfo info, String mobile, String name, String address, String plate) {
        super(result);
        this.mobile = mobile;
        this.name = name;
        this.address = address;
        this.plate = plate;
        this.info = info;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.ask_charge);
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
        requestJson.put("lat", info.location.latitude);
        requestJson.put("lng", info.location.longitude);
        requestJson.put("mobile", mobile);
        requestJson.put("name", SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_NICKNAME, ""));
        requestJson.put("address", address);
        requestJson.put("plate", plate);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
