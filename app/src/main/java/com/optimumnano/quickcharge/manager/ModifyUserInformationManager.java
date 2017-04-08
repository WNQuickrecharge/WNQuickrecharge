package com.optimumnano.quickcharge.manager;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;

import org.xutils.http.RequestParams;

import java.util.HashMap;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by mfwn on 2017/4/6.
 */

public class ModifyUserInformationManager {
    public void modifyPassword(String mobile, String oldpwd,String newpwd, final ManagerCallback callback){
        if (StringUtils.isEmpty(mobile)){
            callback.onFailure("用户名不能为空");
        }
        else if (StringUtils.isEmpty(oldpwd)){
            callback.onFailure("旧密码不能为空");
        }else if (StringUtils.isEmpty(newpwd)){
            callback.onFailure("新密码不能为空");
        }else if (newpwd.length()<6){
            callback.onFailure("密码长度小于6位");
        }
        else {
            String url = HttpApi.getInstance().getUrl(HttpApi.modify_password_url);
            RequestParams params= new RequestParams(url);
            HashMap<String ,Object> requestJson=new HashMap<>();
            requestJson.put("mobile",mobile);
            requestJson.put("oldpwd",oldpwd);
            requestJson.put("newpwd",newpwd);
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

    public void modifyNickNameAndSex(String nickname, int sex, final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.modify_userinfo_url);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("nick_name",nickname);
        requestJson.put("sex",sex);
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

    public void modifyHeadView(String imagebase64, final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.upload_headView_url);
        RequestParams params= new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("imagebase64",imagebase64);
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
