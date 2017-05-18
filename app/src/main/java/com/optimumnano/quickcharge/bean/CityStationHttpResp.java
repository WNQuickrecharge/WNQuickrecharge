package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by herry on 2017/5/17.
 */

public class CityStationHttpResp extends BaseHttpResp {
    private List<Point> result;

    public List<Point> getResult() {
        return result;
    }

    public void setResult(List<Point> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CityStationHttpResp{" +
                "result=" + result +
                '}';
    }
}
