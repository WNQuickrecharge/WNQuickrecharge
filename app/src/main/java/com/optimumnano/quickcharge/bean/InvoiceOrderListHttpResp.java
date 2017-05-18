package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by herry on 2017/5/17.
 */

public class InvoiceOrderListHttpResp extends BaseHttpResp {
    List<InvoiceRecordBean> result;

    public List<InvoiceRecordBean> getResult() {
        return result;
    }

    public void setResult(List<InvoiceRecordBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "InvoiceOrderListHttpResp{" +
                "result=" + result +
                '}';
    }
}
