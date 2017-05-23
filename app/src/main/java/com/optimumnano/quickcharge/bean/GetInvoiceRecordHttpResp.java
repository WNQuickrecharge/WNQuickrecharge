package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by zhangjiancheng on 2017/5/18.
 */

public class GetInvoiceRecordHttpResp extends BaseHttpResp {
    private List<InvoiceRecordBean> result;

    public List<InvoiceRecordBean> getResult() {
        return result;
    }

    public void setResult(List<InvoiceRecordBean> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetInvoiceRecordHttpResp{" +
                "result=" + result +
                '}';
    }
}
