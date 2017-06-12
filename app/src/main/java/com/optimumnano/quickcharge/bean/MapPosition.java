package com.optimumnano.quickcharge.bean;


import java.io.Serializable;

/**
 * Created: huangyangxian@longshine.com
 * Date: 2016-11-11 11:11
 * function: 线路信息
 */
public class MapPosition implements Serializable{
    private int sort;
    private String province;
    private String city;
    private String country;
    private String address;
    private String name;
    private double lon;
    private double lat;

    public MapPosition() {
    }

    public MapPosition(int sort, String province, String city, String country, String address, String name, double lon, double lat) {
        this.sort = sort;
        this.province = province;
        this.city = city;
        this.country = country;
        this.address = address;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
