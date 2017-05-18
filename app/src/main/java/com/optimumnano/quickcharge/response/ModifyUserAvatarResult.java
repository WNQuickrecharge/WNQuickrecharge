package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.ModifyUserAvatarHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by herry on 2017/5/16.
 */

public class ModifyUserAvatarResult extends BaseChargeResult {
    ModifyUserAvatarHttpResp resp;

    public ModifyUserAvatarHttpResp getResp() {
        return resp;
    }

    public ModifyUserAvatarResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        resp = JSON.parseObject(response.body().string(), ModifyUserAvatarHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}
