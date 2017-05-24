package com.optimumnano.quickcharge.bean;

/**
 * Created by chenwenguang on 2017/5/20.
 */

public class GetAskChargeBean {

    /**
     * ask_state : -1
     */

    private int ask_state;
    /**
     * ask_no : AC20170520154622024
     * charge_phone : 15802551216
     * charge_name : 陈文广
     * charge_plate : 粤BCS991
     */

    private String ask_no;
    private String charge_phone;
    private String charge_name;
    private String charge_plate;
    /**
     * car_vin : LGHT7V1D2GG100995
     */

    private String car_vin;
    /**
     * capp_lat : 22.73403
     * capp_lng : 114.397789
     */

    private String capp_lat;
    private String capp_lng;

    public int getAsk_state() {
        return ask_state;
    }

    public void setAsk_state(int ask_state) {
        this.ask_state = ask_state;
    }

    public String getAsk_no() {
        return ask_no;
    }

    public void setAsk_no(String ask_no) {
        this.ask_no = ask_no;
    }

    public String getCharge_phone() {
        return charge_phone;
    }

    public void setCharge_phone(String charge_phone) {
        this.charge_phone = charge_phone;
    }

    public String getCharge_name() {
        return charge_name;
    }

    public void setCharge_name(String charge_name) {
        this.charge_name = charge_name;
    }

    public String getCharge_plate() {
        return charge_plate;
    }

    public void setCharge_plate(String charge_plate) {
        this.charge_plate = charge_plate;
    }

    public String getCar_vin() {
        return car_vin;
    }

    public void setCar_vin(String car_vin) {
        this.car_vin = car_vin;
    }

    public String getCapp_lat() {
        return capp_lat;
    }

    public void setCapp_lat(String capp_lat) {
        this.capp_lat = capp_lat;
    }

    public String getCapp_lng() {
        return capp_lng;
    }

    public void setCapp_lng(String capp_lng) {
        this.capp_lng = capp_lng;
    }
}
