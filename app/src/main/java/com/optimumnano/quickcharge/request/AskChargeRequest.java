package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
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
    private String moblie;
    private String name;
    private String address;
    private String plate;

    public AskChargeRequest(BaseResult result, PreferencesHelper helper, String moblie, String name, String address, String plate) {
        super(result);
        this.helper = helper;
        this.moblie = moblie;
        this.name = name;
        this.address = address;
        this.plate = plate;
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
        requestJson.put("lat", helper.getLocation().lat);
        requestJson.put("lng", helper.getLocation().lng);
        requestJson.put("mobile", moblie);
        requestJson.put("name", SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_NICKNAME, ""));
        requestJson.put("address", address);
        requestJson.put("plate", plate);
        String json = JSON.toJSONString(requestJson);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
