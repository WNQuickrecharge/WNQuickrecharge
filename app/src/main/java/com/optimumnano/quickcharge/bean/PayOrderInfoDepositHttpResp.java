package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/17.
 */

public class PayOrderInfoDepositHttpResp extends BaseHttpResp {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "PayOrderInfoDepositHttpResp{" +
                "result='" + result + '\'' +
                '}';
    }
}
