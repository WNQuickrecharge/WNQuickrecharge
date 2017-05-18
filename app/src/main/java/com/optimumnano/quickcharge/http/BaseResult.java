package com.optimumnano.quickcharge.http;

import android.content.Context;

import okhttp3.Response;

public abstract class BaseResult {
    protected Context context;
    protected BaseRequest request;

    public BaseResult(Context context) {
        this.context = context;
    }

    void setRequest(BaseRequest request) {
        this.request = request;
    }

    protected int preProcess() {
        return HttpResult.FAIL;// default
    }

    /*默认是不处理*/
    protected boolean processStatus(int status) {
        return false;
    }

    protected abstract int parseResponse(Response response) throws Exception;
}
