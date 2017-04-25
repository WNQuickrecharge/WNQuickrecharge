package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.CarPoint;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.List;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_NICKNAME;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by 秋平 on 2017/4/8 0008.
 */

public class MapManager {

    public void getReigonInfo(PreferencesHelper mHelper, final ManagerCallback callback) {
        String url = HttpApi.getInstance().getUrl(HttpApi.region_station_url);
        RequestParams params = new RequestParams(url);
        HashMap<String, Object> requestJson = new HashMap<>();
//        requestJson.put("lat", mHelper.getLocation().lat);
//        requestJson.put("lng", mHelper.getLocation().lng);
        requestJson.put("lat", 22.731936);
        requestJson.put("lng", 114.387322);
        requestJson.put("city", "深圳市");
        requestJson.put("distance", mHelper.showDistance());
        String json = JSON.toJSONString(requestJson);
        params.setBodyContent(json);

        MyHttpUtils.getInstance().post(params, new HttpCallback<List<Point>>() {
            @Override
            public void onSuccess(List<Point> result, int httpCode) {
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

    public void getAskCharge(PreferencesHelper mHelper,String moblie,String name,String address,String plate, final ManagerCallback callback) {
        String url = HttpApi.getInstance().getUrl(HttpApi.ask_charge);
        RequestParams params = new RequestParams(url);
        HashMap<String, Object> requestJson = new HashMap<>();
//        requestJson.put("lat", mHelper.getLocation().lat);
//        requestJson.put("lng", mHelper.getLocation().lng);
        requestJson.put("lat", 22.647552);
        requestJson.put("lng", 114.06667);
        requestJson.put("moblie", moblie);
        requestJson.put("name", SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_NICKNAME,""));
        requestJson.put("address", address);
        requestJson.put("plate", plate);
        String json = JSON.toJSONString(requestJson);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        params.setBodyContent(json);
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
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
    public void getregionCarpile(PreferencesHelper mHelper, final ManagerCallback callback) {
        String url = HttpApi.getInstance().getUrl(HttpApi.region_carpile);
        RequestParams params = new RequestParams(url);
        HashMap<String, Object> requestJson = new HashMap<>();
//        requestJson.put("lat", mHelper.getLocation().lat);
//        requestJson.put("lng", mHelper.getLocation().lng);
        requestJson.put("lat", 22.731936);
        requestJson.put("lng", 114.387322);
//        requestJson.put("city", "深圳市");
        requestJson.put("distance", mHelper.showDistance());
        String json = JSON.toJSONString(requestJson);
        params.setBodyContent(json);

        MyHttpUtils.getInstance().post(params, new HttpCallback<List<CarPoint>>() {
            @Override
            public void onSuccess(List<CarPoint> result, int httpCode) {
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


//    GetChargeCarLocation
//    LGAX4C448F3008319
}
