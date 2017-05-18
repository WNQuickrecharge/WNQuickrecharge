package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by herry on 2017/5/17.
 */

public class GetInvoiceConsumeHttpResp extends BaseHttpResp {
    private List<InvoiceOrder> result;

    public List<InvoiceOrder> getResult() {
        return result;
    }

    public void setResult(List<InvoiceOrder> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetInvoiceConsumeHttpResp{" +
                "result=" + result +
                '}';
    }
}
