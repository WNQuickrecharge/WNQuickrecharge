package com.optimumnano.quickcharge.bean;

/**
 * Created by chenwenguang on 2017/5/20.
 */

public class GetAskChargeHttpResp extends BaseHttpResp {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetAskChargeHttpResp{" +
                "result='" + result + '\'' +
                '}';
    }
}
