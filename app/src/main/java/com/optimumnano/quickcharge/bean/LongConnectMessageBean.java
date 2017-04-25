package com.optimumnano.quickcharge.bean;

/**
 * Created by mfwn on 2017/4/24.
 */

public class LongConnectMessageBean {
    /**
     * order_no : C1234
     * power_time : 50
     * charge_soc : 98
     * consume_money : 40
     * forzen_cash : 50
     * back_cash : 10
     * status : 6
     * signalr_msg_type : 2
     */

    private String order_no;
    private int power_time;
    private int charge_soc;
    private Double forzen_cash;
    private Double back_cash;
    private int status;
    private int signalr_msg_type;
    private int soc;
    private int time_remain;
    private int total_power_time;
    private Double consume_money;

    public Double getConsume_money() {
        return consume_money;
    }

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public int getTime_remain() {
        return time_remain;
    }

    public void setTime_remain(int time_remain) {
        this.time_remain = time_remain;
    }

    public int getTotal_power_time() {
        return total_power_time;
    }

    public void setTotal_power_time(int total_power_time) {
        this.total_power_time = total_power_time;
    }

    public void setConsume_money(Double consume_money) {
        this.consume_money = consume_money;
    }

    public LongConnectMessageBean() {
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getPower_time() {
        return power_time;
    }

    public void setPower_time(int power_time) {
        this.power_time = power_time;
    }

    public int getCharge_soc() {
        return charge_soc;
    }

    public void setCharge_soc(int charge_soc) {
        this.charge_soc = charge_soc;
    }


    public Double getForzen_cash() {
        return forzen_cash;
    }

    public void setForzen_cash(Double forzen_cash) {
        this.forzen_cash = forzen_cash;
    }

    public Double getBack_cash() {
        return back_cash;
    }

    public void setBack_cash(Double back_cash) {
        this.back_cash = back_cash;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSignalr_msg_type() {
        return signalr_msg_type;
    }

    public void setSignalr_msg_type(int signalr_msg_type) {
        this.signalr_msg_type = signalr_msg_type;
    }


}
