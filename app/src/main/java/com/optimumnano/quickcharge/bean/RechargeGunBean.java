package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by ds on 2017/4/11.
 * 充电枪信息
 */
public class RechargeGunBean implements Serializable{
//    {"service_cost":0,"pile_type":"直流","power":150,"elec_current":150,"price":2}
    public String gun_code;
    public Double service_cost;
    public String pile_type;
    public int power;
    public double elec_current;
    public Double price;
}
