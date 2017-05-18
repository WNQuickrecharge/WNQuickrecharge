package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/17.
 */

public class InvoiceSignHttpResp extends BaseHttpResp {

    private InvoiceSignRsp result;

    public InvoiceSignRsp getResult() {
        return result;
    }

    public void setResult(InvoiceSignRsp result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "InvoiceSignHttpResp{" +
                "result=" + result +
                '}';
    }
}
