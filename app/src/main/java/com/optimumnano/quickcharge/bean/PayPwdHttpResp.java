package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/16.
 */

public class PayPwdHttpResp extends BaseHttpResp {

    private PwdData result;

    public PwdData getResult() {
        return result;
    }

    public void setResult(PwdData result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "PayPwdHttpResp{" +
                "result=" + result +
                '}';
    }

    public static class PwdData {
        private String paypwd;

        public String getPaypwd() {
            return paypwd;
        }

        public void setPaypwd(String paypwd) {
            this.paypwd = paypwd;
        }

        @Override
        public String toString() {
            return "pwdData{" +
                    "paypwd='" + paypwd + '\'' +
                    '}';
        }
    }
}
