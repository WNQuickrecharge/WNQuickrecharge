package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by ds on 2017/3/29.
 * 订单bean
 */
public class OrderBean implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 订单号 **/
    public String order_no;
    /** 订单状态
     * 免单=0,
     * 已取消=1,
     * 待支付=2,
     * 待充电=3,
     * 充电中=4,
     * 待评价=5,
     * 已完成=6
     **/
    public int order_status;
    /** 充电枪编号 **/
    public String gun_code;
    /** 充电桩类型 **/
    public String pile_type;
    /** 预支付金额 **/
    public Double frozen_cash;
    /** 实际充电费用 **/
    public double charge_cash;
    /** 充电桩功率 **/
    public int power;
    /** 充电桩电流 **/
    public int elec_current;
    /** 充电量 **/
    public double charge_vol;
    /** 支付类型 **/
    public String pay_type;
    /** 开始充电时间 **/
    public String start_time;
    /** 结束充电时间 **/
    public String end_time;
    /** 是否已开发票 **/
    public String is_bill;
    /** 充电耗时 **/
    public int power_time;
    public OrderBean(){
    }

}
