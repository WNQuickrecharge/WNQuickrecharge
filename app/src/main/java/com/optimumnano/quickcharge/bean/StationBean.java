package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by mfwn on 2017/4/8.
 */

public class StationBean implements Serializable{
    /**
     * yyprovince : 广东省
     * yycity : 深圳市
     * yydistrict : 南山区
     * TotalPiles : 4
     * FreePiles : 4
     * Id : 3006
     * StationNo : 44030501000002
     * StationName : 壮辉充电站
     * City : 广东省-深圳市-南山区
     * State : 3
     * Phone : 18766662256
     * OperationDate : 2017-02-26T00:00:00
     * BuiltDate : 2017-02-01T00:00:00
     * ManagementCompany : wtm
     * Lng : 113.950075
     * Lat : 22.571174
     * Address : 深圳市坪山新区兰景北路68号
     * RunTimeSpan : 0点到24点
     * CreateTime : 2017-02-15T14:27:07.517
     * UpdateTime : 2017-02-15T14:27:07.517
     * IsDel : false
     */

    private String yyprovince;
    private String yycity;
    private String yydistrict;
    private int TotalPiles;
    private int FreePiles;
    private int Id;
    private String StationNo;
    private String StationName;
    private String City;
    private int State;
    private String Phone;
    private String OperationDate;
    private String BuiltDate;
    private String ManagementCompany;
    private String Lng;
    private String Lat;
    private String Address;
    private String RunTimeSpan;
    private String CreateTime;
    private String UpdateTime;
    private String distance;
    private boolean IsDel;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isDel() {
        return IsDel;
    }

    public void setDel(boolean del) {
        IsDel = del;
    }

    public StationBean() {
    }

    public String getYyprovince() {
        return yyprovince;
    }

    public void setYyprovince(String yyprovince) {
        this.yyprovince = yyprovince;
    }

    public String getYycity() {
        return yycity;
    }

    public void setYycity(String yycity) {
        this.yycity = yycity;
    }

    public String getYydistrict() {
        return yydistrict;
    }

    public void setYydistrict(String yydistrict) {
        this.yydistrict = yydistrict;
    }

    public int getTotalPiles() {
        return TotalPiles;
    }

    public void setTotalPiles(int TotalPiles) {
        this.TotalPiles = TotalPiles;
    }

    public int getFreePiles() {
        return FreePiles;
    }

    public void setFreePiles(int FreePiles) {
        this.FreePiles = FreePiles;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getStationNo() {
        return StationNo;
    }

    public void setStationNo(String StationNo) {
        this.StationNo = StationNo;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String StationName) {
        this.StationName = StationName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getOperationDate() {
        return OperationDate;
    }

    public void setOperationDate(String OperationDate) {
        this.OperationDate = OperationDate;
    }

    public String getBuiltDate() {
        return BuiltDate;
    }

    public void setBuiltDate(String BuiltDate) {
        this.BuiltDate = BuiltDate;
    }

    public String getManagementCompany() {
        return ManagementCompany;
    }

    public void setManagementCompany(String ManagementCompany) {
        this.ManagementCompany = ManagementCompany;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String Lng) {
        this.Lng = Lng;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getRunTimeSpan() {
        return RunTimeSpan;
    }

    public void setRunTimeSpan(String RunTimeSpan) {
        this.RunTimeSpan = RunTimeSpan;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public boolean isIsDel() {
        return IsDel;
    }

    public void setIsDel(boolean IsDel) {
        this.IsDel = IsDel;
    }
}
