package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.GetInvoiceRecordHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by zhangjiancheng on 2017/5/18.
 */

public class GetInvoiceRecordResult extends BaseChargeResult {
    private GetInvoiceRecordHttpResp resp;

    public GetInvoiceRecordHttpResp getResp() {
        return resp;
    }
    public GetInvoiceRecordResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        resp = JSON.parseObject(response.body().string(), GetInvoiceRecordHttpResp.class);
        if (resp == null) {
            return HttpResult.FAIL;
        }
        if (resp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}
