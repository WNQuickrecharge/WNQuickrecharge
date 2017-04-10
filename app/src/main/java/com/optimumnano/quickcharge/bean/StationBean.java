package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by mfwn on 2017/4/8.
 */

public class StationBean implements Serializable{
    public double lng;
    public double lat;
    public String address;
    public String serviceName;
    public String onServiceTime;
    public String freeGuns;
    public String totalGuns;
    public String electricPrice;
    public String servicePrice;
    public String stationName;

    public StationBean(double lng, double lat, String address, String serviceName, String onServiceTime, String freeGuns, String totalGuns, String electricPrice, String servicePrice, String stationName) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.serviceName = serviceName;
        this.onServiceTime = onServiceTime;
        this.freeGuns = freeGuns;
        this.totalGuns = totalGuns;
        this.electricPrice = electricPrice;
        this.servicePrice = servicePrice;
        this.stationName = stationName;
    }
}
