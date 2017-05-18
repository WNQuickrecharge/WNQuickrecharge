package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/16.
 */

public class GunInfoHttpResp extends BaseHttpResp {
    private RechargeGunBean result;

    public RechargeGunBean getResult() {
        return result;
    }

    public void setResult(RechargeGunBean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GunInfoHttpResp{" +
                "result=" + result +
                '}';
    }
}
