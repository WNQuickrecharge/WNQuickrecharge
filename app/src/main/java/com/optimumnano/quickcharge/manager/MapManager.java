package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.Point;
import com.optimumnano.quickcharge.data.PreferencesHelper;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.List;

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
        requestJson.put("lat", 22.647552);
        requestJson.put("lng", 114.06667);
//        requestJson.put("city", "深圳市");
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


}
