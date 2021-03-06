package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.xutils.http.RequestParams;

import java.util.HashMap;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;

/**
 * Created by chenwenguang on 2017/4/16.
 */

public class StationManager {
    public void getGunsDetail(int stationId, final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_station_detail);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("station_id",stationId);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        String json = JSON.toJSONString(requestJson);
        params.setBodyContent(json);

        MyHttpUtils.getInstance().post(params ,new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }
    public static void getCityStations(String city,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_city_stations);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("city",city);
//        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        String json = JSON.toJSONString(requestJson);
        params.setBodyContent(json);

        MyHttpUtils.getInstance().post(params ,new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }
}
