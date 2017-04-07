package com.optimumnano.quickcharge.manager;

import android.app.ProgressDialog;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.HashMap;

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
            HashMap<String ,Object> requestJson=new HashMap<>();
            requestJson.put("mobile",username);
            requestJson.put("pwd",pwd);
            requestJson.put("type",1);
            String json = JSON.toJSONString(requestJson);
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
    }

    /**
     * 注册
     */
    public void register(String mobile,String pwd,String checkNum,String confirmpwd, final ManagerCallback callback,final int httpCode){
        if (!registerCheck(mobile,pwd,checkNum,confirmpwd,callback)){
            return;
        }
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
    private boolean registerCheck(String mobile,String pwd,String checkNum,String confirmpwd,ManagerCallback callback){
        if (StringUtils.isEmpty(mobile)){
            callback.onFailure("请输入电话号码");
            return false;
        }
        if (!StringUtils.isMobile(mobile)){
            callback.onFailure("电话号码格式有误");
            return false;
        }
        if (StringUtils.isEmpty(pwd)){
            callback.onFailure("密码不能为空");
            return false;
        }
        if (StringUtils.isEmpty(checkNum)){
            callback.onFailure("验证码不能为空");
            return false;
        }
        if (StringUtils.isEmpty(confirmpwd)){
            callback.onFailure("确认密码不能为空");
            return false;
        }
        if (!pwd.equals(confirmpwd)){
            callback.onFailure("两次密码输入不一致");
            return false;
        }
        return true;
    }
    public void getCheckNum(String mobile,String purpose, final ManagerCallback callback,final int httpCode) {
        String url = HttpApi.getInstance().getUrl(HttpApi.register_checknum);
        RequestParams params = new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("mobile",mobile);
        requestJson.put("purpose",purpose);
        String json = JSON.toJSONString(requestJson);
        params.setBodyContent(json);
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

    public void forgetPassword(String mobile,String purpose,String validate_code,String newpwd,int userType,
                               final ManagerCallback callback) {
        String url = HttpApi.getInstance().getUrl(HttpApi.forget_password_url);
        RequestParams params = new RequestParams(url);
        HashMap<String ,Object> requestJson=new HashMap<>();
        requestJson.put("mobile",mobile);
        requestJson.put("purpose",purpose);
        requestJson.put("validate_code",validate_code);
        requestJson.put("newpwd",newpwd);
        requestJson.put("user_type",userType);
        String json = JSON.toJSONString(requestJson);
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
}
