package com.optimumnano.quickcharge.bean;

/**
 * Created by chenwenguang on 2017/5/21.
 */

public class GetAskChargeCarLocationHttpResp extends BaseHttpResp {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetAskChargeCarLocationHttpResp{" +
                "result='" + result + '\'' +
                '}';
    }
}
