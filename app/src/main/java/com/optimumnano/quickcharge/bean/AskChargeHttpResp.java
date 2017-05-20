package com.optimumnano.quickcharge.bean;

/**
 * Created by jack on 2017/5/20.
 */

public class AskChargeHttpResp extends BaseHttpResp {
    private AskChargeResultBean result;

    public AskChargeResultBean getResult() {
        return result;
    }

    public void setResult(AskChargeResultBean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AskChargeHttpResp{" +
                "result='" + result + '\'' +
                '}';
    }

    public class AskChargeResultBean {
        public String ask_no;
    }
}
