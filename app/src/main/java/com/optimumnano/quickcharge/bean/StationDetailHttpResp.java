package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by herry on 2017/5/17.
 */

public class StationDetailHttpResp extends BaseHttpResp {
    private List<GunBean> result;

    public List<GunBean> getResult() {
        return result;
    }

    public void setResult(List<GunBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "StationDetailHttpResp{" +
                "result=" + result +
                '}';
    }
}
