package com.optimumnano.quickcharge.response;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.LoginHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;
import com.optimumnano.quickcharge.utils.GlideCacheUtil;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;

import org.xutils.common.util.LogUtil;

import okhttp3.Response;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_BALANCE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_HEADIMG_URL;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_IS_REMEMBER;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_MOBILE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_NICKNAME;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_SEX;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_USER_ID;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by herry on 2017/5/15.
 */


public class LoginResult extends BaseChargeResult {
    private LoginHttpResp loginHttpResp;

    public LoginHttpResp getLoginHttpResp() {
        return loginHttpResp;
    }

    public LoginResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        String body = response.body().string();

        loginHttpResp = JSON.parseObject(body, LoginHttpResp.class);
        if (loginHttpResp == null) {
            return HttpResult.FAIL;
        }
        if (loginHttpResp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        LoginHttpResp.UserInfo userInfo = loginHttpResp.getResult().getUserinfo();
        //save to pref
        String phoneNum = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_MOBILE, "");
        boolean isRemember = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, false);
        if (!phoneNum.equals(userInfo.getPhoneNum())) {
            GlideCacheUtil.getInstance().clearImageAllCache(context);
            SharedPreferencesUtil.getEditor(SP_USERINFO).clear().commit();
            LogUtil.i("test==Glide  clearDiskCache");
        }

        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_NICKNAME,
                TextUtils.isEmpty(userInfo.getNickName()) ? "" : userInfo.getNickName());
        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_HEADIMG_URL,
                TextUtils.isEmpty(userInfo.getAvatarUrl()) ? "" : userInfo.getAvatarUrl());
        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_SEX, userInfo.getGender());
        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_MOBILE, userInfo.getPhoneNum());
        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_USER_ID, userInfo.getId());
        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, isRemember);

        boolean b = loginHttpResp.getResult().getAccount() == null;
        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_BALANCE,
                b ? "0.00" : StringUtils.formatDouble(loginHttpResp.getResult().getAccount().getRestCash()));

        return HttpResult.SUCCESS;
    }
}
