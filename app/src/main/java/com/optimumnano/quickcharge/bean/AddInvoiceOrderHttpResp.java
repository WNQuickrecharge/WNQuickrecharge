package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/17.
 */

public class AddInvoiceOrderHttpResp extends BaseHttpResp {

    private InvoiceOrderRsp result;

    public InvoiceOrderRsp getResult() {
        return result;
    }

    public void setResult(InvoiceOrderRsp result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AddInvoiceOrderHttpResp{" +
                "result=" + result +
                '}';
    }
}
