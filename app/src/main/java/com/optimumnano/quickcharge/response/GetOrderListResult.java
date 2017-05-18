package com.optimumnano.quickcharge.response;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.bean.OrderListHttpResp;
import com.optimumnano.quickcharge.http.HttpResult;

import okhttp3.Response;

/**
 * Created by herry on 2017/5/15.
 */

public class GetOrderListResult extends BaseChargeResult {
    private OrderListHttpResp orderListHttpResp;

    public OrderListHttpResp getOrderListHttpResp() {
        return orderListHttpResp;
    }

    public GetOrderListResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        orderListHttpResp = JSON.parseObject(response.body().string(), OrderListHttpResp.class);
        if (orderListHttpResp == null) {
            return HttpResult.FAIL;
        }
        if (orderListHttpResp.getStatus() != 0) {
            return HttpResult.FAIL;
        }
        return HttpResult.SUCCESS;
    }
}
