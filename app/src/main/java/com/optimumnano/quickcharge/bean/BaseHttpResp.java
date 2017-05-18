package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/14.
 */

public class BaseHttpResp {
    private int status;

    private String resultMsg;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return "BaseHttpResp{" +
                "status=" + status +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
