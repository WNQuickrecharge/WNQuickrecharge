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

//    "Id": 78,
//            "B_UserAccountId": 9,
//            "C_ChargeOrderId": 437,
//            "C_ChargeOrderNum": "C20170425215421001",
//            "ConsumeCash": 50.00,
//            "BeforeCash": 100.00,
//            "AfterCash": 50.00,
//            "PayType": 0,
//            "TradeNum": "1",
//            "ConsumeMonth": 4,
//            "Address": "深圳市坪山新区兰景北路68号",
//            "EndTime": "2017-04-26 08:58:00",
//            "ChargeFrom": 1
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
    public String EndTime;

    public InvoiceOrder() {
    }

    public InvoiceOrder(int id, int b_UserAccountId, int c_ChargeOrderId, String c_ChargeOrderNum, double consumeCash, double beforeCash, double afterCash, String payType, String tradeNum, int consumeMonth, String address, boolean isChecked, String endTime) {
        Id = id;
        B_UserAccountId = b_UserAccountId;
        C_ChargeOrderId = c_ChargeOrderId;
        C_ChargeOrderNum = c_ChargeOrderNum;
        ConsumeCash = consumeCash;
        BeforeCash = beforeCash;
        AfterCash = afterCash;
        PayType = payType;
        TradeNum = tradeNum;
        ConsumeMonth = consumeMonth;
        Address = address;
        this.isChecked = isChecked;
        EndTime = endTime;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getB_UserAccountId() {
        return B_UserAccountId;
    }

    public void setB_UserAccountId(int b_UserAccountId) {
        B_UserAccountId = b_UserAccountId;
    }

    public int getC_ChargeOrderId() {
        return C_ChargeOrderId;
    }

    public void setC_ChargeOrderId(int c_ChargeOrderId) {
        C_ChargeOrderId = c_ChargeOrderId;
    }

    public String getC_ChargeOrderNum() {
        return C_ChargeOrderNum;
    }

    public void setC_ChargeOrderNum(String c_ChargeOrderNum) {
        C_ChargeOrderNum = c_ChargeOrderNum;
    }

    public double getConsumeCash() {
        return ConsumeCash;
    }

    public void setConsumeCash(double consumeCash) {
        ConsumeCash = consumeCash;
    }

    public double getBeforeCash() {
        return BeforeCash;
    }

    public void setBeforeCash(double beforeCash) {
        BeforeCash = beforeCash;
    }

    public double getAfterCash() {
        return AfterCash;
    }

    public void setAfterCash(double afterCash) {
        AfterCash = afterCash;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getTradeNum() {
        return TradeNum;
    }

    public void setTradeNum(String tradeNum) {
        TradeNum = tradeNum;
    }

    public int getConsumeMonth() {
        return ConsumeMonth;
    }

    public void setConsumeMonth(int consumeMonth) {
        ConsumeMonth = consumeMonth;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
