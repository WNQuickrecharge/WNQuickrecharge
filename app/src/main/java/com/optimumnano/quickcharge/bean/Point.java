package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by 秋平 on 2017/4/8 0008.
 */

public class Point implements Serializable{

    public String yyprovince;//湖北省",
    public String yycity;//: "武汉市",
    public String yydistrict;//": "江夏区",
    public String TotalPiles;//": 0,
    public String FreePiles;//": 0,
    public double distance;//": 13.009926795959473,
    public int Id;//": 3035
    public String StationNo;//": "A008",
    public String StationName;//": "充电站七号",
    public String City;//": "湖北省-武汉市-硚口区",
    public int State;//": 1,
    public String Phone;//": "13912345678",
    public String OperationDate;//": "2017-01-02T00:00:00",
    public String BuiltDate;//": null,
    public String ManagementCompany;//": "依迅",
    public double Lng;//": "114.06667",
    public double Lat;//": "22.61667",
    public String Address;//": "深圳",
    public String CreateTime;//": "2017-04-05T10:13:46.877",
    public String UpdateTime;//": "2017-04-05T10:13:46.877",
    public boolean IsDel;//": false
    public double max_price;
    public double min_price;
    public double max_service;
    public double min_service;
    public String RunTimeSpan;

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (!(obj instanceof Point)) return false;
        final Point other = (Point)obj;

        if(this.Id==(other.Id)&& this.State==other.State&&this.StationName.equals(other.StationName))
            return true;
        else
            return false;
    }
}
