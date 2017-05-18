package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.BaseHttpResp;
import com.optimumnano.quickcharge.http.CookieMgr;
import com.optimumnano.quickcharge.http.HttpResult;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import okhttp3.Response;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_IS_REMEMBER;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_MOBILE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by herry on 2017/5/15.
 */

public class LogoutResult extends BaseChargeResult {
    private BaseHttpResp resp;

    public BaseHttpResp getResp() {
        return resp;
    }

    public LogoutResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        resp = JSON.parseObject(response.body().string(), BaseHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        CookieMgr.getInstance(context).clear();

        String phone = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_MOBILE, "");
        boolean isRemember = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, false);
        SharedPreferencesUtil.getEditor(SP_USERINFO).clear().commit();
        SharedPreferencesUtil.getEditor(SP_COOKIE).clear().commit();
        if (isRemember) {
            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_MOBILE, phone);
            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, true);
        } else {
            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_MOBILE, "");
            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, false);
        }
        return HttpResult.SUCCESS;
    }
}
