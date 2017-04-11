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
 * 作者：邓传亮 on 2017/4/8 10:07
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */

public class GetMineInfoManager {
    public static void getTransactionBill(int index,int pagesize, final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_transaction_bill);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("cur_page",index);
        requestJson.put("page_size",pagesize);
        String json = JSON.toJSONString(requestJson);
        params.setBodyContent(json);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_COOKIE,""));
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

}