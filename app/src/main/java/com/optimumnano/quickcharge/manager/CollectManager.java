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
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by mfwn on 2017/4/12.
 */

public class CollectManager {
    public void getCollect(final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_collection);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_COOKIE,""));
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
    public void addCollectStation(int station_id,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.add_collection);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("station_id",station_id);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_COOKIE,""));
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
    public void deleteCollectStation(int station_id,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.delete_collection);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("station_id",station_id);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_COOKIE,""));
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
