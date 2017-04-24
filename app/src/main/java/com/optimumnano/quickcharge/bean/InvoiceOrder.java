package com.optimumnano.quickcharge.bean;

/**
 * Created by ds on 2017/4/22.
 */

public class InvoiceOrder {
//    B_UserAccountID 消费账户ID
//    C_ChargeOrderID 消费的订单ID
//    C_ChargeOrderNum 消费的订单编号
//    ConsumeCash 消费金额
//    BeforeCash 消费前余额
//    AfterCash 消费后余额
//    PayType 支付方式(0=支付宝 1=微信 2=银联支付 3=余额支付 4=其他)
//    TradeNum 交易流水号
//    ConsumeMonth 消费月份
//    Address 充电桩地址
    public  int Id;
    public  int B_UserAccountId;
    public  int C_ChargeOrderId;
    public  String C_ChargeOrderNum;
    public  double ConsumeCash;
    public  double BeforeCash;
    public  double AfterCash;
    public  String PayType;
    public  String TradeNum;
    public  int ConsumeMonth;
    public  String Address;
    public boolean isChecked;
}
