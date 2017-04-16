package com.optimumnano.quickcharge.bean;

import java.util.List;

/**
 * Created by chenwenguang on 2017/4/16.
 */

public class PileBean {
    private String pileNo;
    private String price;
    private String servicePrice;
    private List<GunBean> gunList;
    private int status;

    @Override
    public String toString() {
        return "PileBean{" +
                "pileNo='" + pileNo + '\'' +
                ", price='" + price + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", gunList=" + gunList +
                ", status=" + status +
                '}';
    }

    public String getPileNo() {
        return pileNo;
    }

    public void setPileNo(String pileNo) {
        this.pileNo = pileNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public List<GunBean> getGunList() {
        return gunList;
    }

    public void setGunList(List<GunBean> gunList) {
        this.gunList = gunList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
