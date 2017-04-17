package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by 秋平 on 2017/4/8 0008.
 */

public class Point implements Serializable{


    /**
     * Phone : 18766662256
     * yyprovince : 广东省
     * ManagementCompany : wtm
     * Id : 3006
     * ChargingStationId : 3006
     * Lng : 113.950075
     * min_service : 0.2
     * Address : 深圳市坪山新区兰景北路68号
     * max_price : 4.56
     * IsDel : false
     * OperationDate : 2017-02-26T00:00:00
     * FreePiles : 4
     * max_service : 0.2
     * yydistrict : 南山区
     * TotalPiles : 4
     * min_price : 4.56
     * BuiltDate : 2017-02-01T00:00:00
     * Lat : 22.571174
     * yycity : 深圳市
     * StationName : 壮辉充电站
     * State : 3
     * CreateTime : 2017-02-15T14:27:07.517
     * UpdateTime : 2017-02-15T14:27:07.517
     * StationNo : 44030501000002
     * RunTimeSpan : 0点到24点
     * City : 广东省-深圳市-南山区
     */

    public String Phone;
    public String yyprovince;
    public String ManagementCompany;
    public int Id;
    public int ChargingStationId;
    public String Lng;
    public double min_service;
    public String Address;
    public double max_price;
    public boolean IsDel;
    public String OperationDate;
    public int FreePiles;
    public double max_service;
    public String yydistrict;
    public int TotalPiles;
    public double min_price;
    public String BuiltDate;
    public String Lat;
    public String yycity;
    public String StationName;
    public int State;
    public String CreateTime;
    public String UpdateTime;
    public String StationNo;
    public String RunTimeSpan;
    public String City;
    public String distance;

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (!(obj instanceof Point)) return false;
        final Point other = (Point)obj;

        if(this.Id==(other.Id)&& this.State==other.State&&this.StationName.equals(other.StationName)
                &&this.UpdateTime.equals(other.UpdateTime))
            return true;
        else
            return false;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getYyprovince() {
        return yyprovince;
    }

    public void setYyprovince(String yyprovince) {
        this.yyprovince = yyprovince;
    }

    public String getManagementCompany() {
        return ManagementCompany;
    }

    public void setManagementCompany(String ManagementCompany) {
        this.ManagementCompany = ManagementCompany;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getChargingStationId() {
        return ChargingStationId;
    }

    public void setChargingStationId(int ChargingStationId) {
        this.ChargingStationId = ChargingStationId;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String Lng) {
        this.Lng = Lng;
    }

    public double getMin_service() {
        return min_service;
    }

    public void setMin_service(double min_service) {
        this.min_service = min_service;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public double getMax_price() {
        return max_price;
    }

    public void setMax_price(double max_price) {
        this.max_price = max_price;
    }

    public boolean isIsDel() {
        return IsDel;
    }

    public void setIsDel(boolean IsDel) {
        this.IsDel = IsDel;
    }

    public String getOperationDate() {
        return OperationDate;
    }

    public void setOperationDate(String OperationDate) {
        this.OperationDate = OperationDate;
    }

    public int getFreePiles() {
        return FreePiles;
    }

    public void setFreePiles(int FreePiles) {
        this.FreePiles = FreePiles;
    }

    public double getMax_service() {
        return max_service;
    }

    public void setMax_service(double max_service) {
        this.max_service = max_service;
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

    public double getMin_price() {
        return min_price;
    }

    public void setMin_price(double min_price) {
        this.min_price = min_price;
    }

    public String getBuiltDate() {
        return BuiltDate;
    }

    public void setBuiltDate(String BuiltDate) {
        this.BuiltDate = BuiltDate;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    public String getYycity() {
        return yycity;
    }

    public void setYycity(String yycity) {
        this.yycity = yycity;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String StationName) {
        this.StationName = StationName;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
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

    public String getStationNo() {
        return StationNo;
    }

    public void setStationNo(String StationNo) {
        this.StationNo = StationNo;
    }

    public String getRunTimeSpan() {
        return RunTimeSpan;
    }

    public void setRunTimeSpan(String RunTimeSpan) {
        this.RunTimeSpan = RunTimeSpan;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }
}
