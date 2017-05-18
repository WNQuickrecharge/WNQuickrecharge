package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.InternalConstants;
import com.optimumnano.quickcharge.net.HttpApi;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by herry on 2017/5/14.
 */

public class GetUserInfoRequest extends BaseChargeRequest {
    public GetUserInfoRequest(BaseResult result) {
        super(result);
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.get_accountinfo);
    }

    @Override
    protected ArrayMap<String, String> getHeaders() {
        return null;
    }

    @Override
    protected ArrayMap<String, String> getParams() {
        return null;
    }

    @Override
    protected RequestBody getRequestBody() {
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), "{}");
    }
}
