package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by mfwn on 2017/4/11.
 */

public class GunBean implements Serializable {

    /**
     * GunStatus : 1
     * price : 4.56
     * PileNo : 壮辉14桩
     * gun_code : 2017
     * ServiceCharge : 0.2
     */

    private int GunStatus;
    private double price;
    private String PileNo;
    private String gun_code;
    private double ServiceCharge;
    public double max_price;
    public double min_price;
    public double max_service;
    public double min_service;


    public int getGunStatus() {
        return GunStatus;
    }

    public void setGunStatus(int GunStatus) {
        this.GunStatus = GunStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPileNo() {
        return PileNo;
    }

    public void setPileNo(String PileNo) {
        this.PileNo = PileNo;
    }

    public String getGun_code() {
        return gun_code;
    }

    public void setGun_code(String gun_code) {
        this.gun_code = gun_code;
    }

    public double getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(double ServiceCharge) {
        this.ServiceCharge = ServiceCharge;
    }
}
