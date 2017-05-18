package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/14.
 */

public class UserAccountResp extends BaseHttpResp {
    private UserAccount result;

    public UserAccount getResult() {
        return result;
    }

    public void setResult(UserAccount result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserAccountResp{" +
                "result=" + result +
                '}';
    }
}
