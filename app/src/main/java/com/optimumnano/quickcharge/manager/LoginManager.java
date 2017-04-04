package com.optimumnano.quickcharge.manager;

import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.StringUtils;

import org.xutils.http.RequestParams;

/**
 * Created by ds on 2017/4/1.
 * 登录注册相关 manager
 */
public class LoginManager {
    public void login(String username, String pwd, final ManagerCallback callback){
        if (StringUtils.isEmpty(username)){
            callback.onFailure("用户名不能为空");
        }
        else if (StringUtils.isEmpty(pwd)){
            callback.onFailure("密码不能为空");
        }
        else {
            String url = HttpApi.getInstance().getUrl(HttpApi.login_url);
            RequestParams params = new RequestParams(url);
            params.addBodyParameter("mobile",username);
            params.addBodyParameter("pwd",pwd);
            params.addBodyParameter("type",1+"");
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

    /**
     * 注册
     */
    public void register(String mobile,String pwd,String checkNum,String confirmpwd, final ManagerCallback callback,final int httpCode){
        registerCheck(mobile,pwd,checkNum,confirmpwd);
        String url = HttpApi.getInstance().getUrl(HttpApi.register_url);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("mobile",mobile);
        params.addBodyParameter("purpose","RegisterCApp");
        params.addBodyParameter("validate_code",checkNum);
        params.addBodyParameter("password",pwd);

        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result,httpCode);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }
    //注册参数判断
    private void registerCheck(String mobile,String pwd,String checkNum,String confirmpwd){

    }
    private void getCheckNum(String mobile, final ManagerCallback callback,final int httpCode){
        String url = HttpApi.getInstance().getUrl(HttpApi.register_checknum);
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("mobile",mobile);
        params.addBodyParameter("purpose","RegisterCApp");
        MyHttpUtils.getInstance().post(params, new HttpCallback<String>() {
            @Override
            public void onSuccess(String result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result,httpCode);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
                callback.onFailure(msg);
            }
        });
    }
}
