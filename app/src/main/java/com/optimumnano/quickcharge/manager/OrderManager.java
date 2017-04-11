package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.List;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by ds on 2017/4/10.
 * 订单处理
 */
public class OrderManager {

    /**
     * 获取订单列表
     * @param pageSize 当前页，1开始
     * @param pageCount 每页显示条数
     * @param callback 回调
     */
    public void getAllOrderlist(int pageSize, int pageCount, final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.order_list);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("page_size",pageCount);
        ha.put("cur_page",pageSize);

        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_COOKIE,""));
        params.setBodyContent(JSON.toJSONString(ha));
        MyHttpUtils.getInstance().post(params, new HttpCallback<List<OrderBean>>() {
            @Override
            public void onSuccess(List<OrderBean> result, int httpCode) {
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


    public void getGunInfo(String gun_code,final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_guninfo);
        RequestParams params = new RequestParams(url);
        HashMap<String,Object> ha = new HashMap<>();
        ha.put("gun_code ",gun_code);
        params.setBodyContent(JSON.toJSONString(ha));
//        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback() {
            @Override
            public void onSuccess(Object result, int httpCode) {
                super.onSuccess(result, httpCode);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }
}
