package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * 作者：邓传亮 on 2017/4/26 10:33
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class CityModel implements Serializable{
    public int ret;
    public String msg;

    public String cityName;
    public String cityCode;
    //    private String shortName;
    public String pinyin;
    public String letter;
    public String positionLon;
    public String positionLat;

    public CityModel(String cityName, String pinyin) {
        this.cityName = cityName;
        this.pinyin = pinyin;
    }

    public CityModel(String cityName, String positionLon, String positionLat) {
        this.cityName = cityName;
        this.positionLat = positionLat;
        this.positionLon = positionLon;
    }
}
