package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by herry on 2017/5/16.
 */

public class StationCollectionHttpResp extends BaseHttpResp {
    private List<StationBean> result;

    public List<StationBean> getResult() {
        return result;
    }

    public void setResult(List<StationBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "StationCollectionHttpResp{" +
                "result=" + result +
                '}';
    }
}
