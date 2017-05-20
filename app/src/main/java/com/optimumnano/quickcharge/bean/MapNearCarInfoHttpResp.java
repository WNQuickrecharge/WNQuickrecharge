package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by mfwn on 2017/5/19.
 */

public class MapNearCarInfoHttpResp extends BaseHttpResp {
    private List<CarPoint> result;

    public List<CarPoint> getResult() {
        return result;
    }

    public void setResult(List<CarPoint> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MapNearCarInfoHttpResp{" +
                "result=" + result +
                '}';
    }
}
