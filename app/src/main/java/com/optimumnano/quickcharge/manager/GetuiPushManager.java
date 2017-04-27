package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.xutils.http.RequestParams;

import java.util.HashMap;

import static com.optimumnano.quickcharge.MyApplication.getuiflag;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;


/**
 * 作者：邓传亮 on 2017/4/21 11:07
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */

public class GetuiPushManager {

    public static void setGetuiRegisterid(String registerId, final ManagerCallback callback){
        if (getuiflag){
            return;
        }
        getuiflag=true;

        String url = HttpApi.getInstance().getUrl(HttpApi.set_registerid_url);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("registerId",registerId);
        requestJson.put("platform",PushChannel.GETUI);
        String json = JSON.toJSONString(requestJson);
        params.setBodyContent(json);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
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



    enum PushChannel{
        HUAWEI("huawei"),GETUI("getui");

        PushChannel(String channel) {
            this.channel = channel;
        }

        public String getChannel() {
            return channel;
        }

        private String channel;

    }
}
