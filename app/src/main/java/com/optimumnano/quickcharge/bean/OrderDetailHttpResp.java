package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/17.
 */

public class OrderDetailHttpResp extends BaseHttpResp {
    private OrderBean result;

    public OrderBean getResult() {
        return result;
    }

    public void setResult(OrderBean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OrderDetailHttpResp{" +
                "result=" + result +
                '}';
    }
}
