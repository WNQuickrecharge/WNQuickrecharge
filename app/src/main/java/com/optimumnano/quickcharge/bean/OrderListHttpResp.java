package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by herry on 2017/5/15.
 */

public class OrderListHttpResp extends BaseHttpResp {
    private List<OrderBean> result;

    public List<OrderBean> getResult() {
        return result;
    }

    public void setResult(List<OrderBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OrderListHttpResp{" +
                "result=" + result +
                '}';
    }
}
