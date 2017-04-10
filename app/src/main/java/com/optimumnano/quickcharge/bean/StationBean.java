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

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOnServiceTime() {
        return onServiceTime;
    }

    public void setOnServiceTime(String onServiceTime) {
        this.onServiceTime = onServiceTime;
    }

    public String getFreeGuns() {
        return freeGuns;
    }

    public void setFreeGuns(String freeGuns) {
        this.freeGuns = freeGuns;
    }

    public String getTotalGuns() {
        return totalGuns;
    }

    public void setTotalGuns(String totalGuns) {
        this.totalGuns = totalGuns;
    }

    public String getElectricPrice() {
        return electricPrice;
    }

    public void setElectricPrice(String electricPrice) {
        this.electricPrice = electricPrice;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
