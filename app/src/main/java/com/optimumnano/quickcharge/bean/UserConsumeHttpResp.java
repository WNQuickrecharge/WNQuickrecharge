package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by herry on 2017/5/16.
 */

public class UserConsumeHttpResp extends BaseHttpResp {
    private List<BillBean> result;

    public List<BillBean> getResult() {
        return result;
    }

    public void setResult(List<BillBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserConsumeHttpResp{" +
                "result=" + result +
                '}';
    }
}
