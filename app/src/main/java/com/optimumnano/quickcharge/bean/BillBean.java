package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * 作者：邓传亮 on 2017/4/10 15:33
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class BillBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * OrderNo : C20170415153613001
     * Cash : 10
     * FrozenCash : 0.01
     * BackCash : -9.99
     * TradeNum : 586cbb86-31b9-4ad3-8baf-ae9988671224
     * PayType : 微信支付
     * CreateTime : 2017-04-24 08:54:16
     * ChargeVolume : 10
     * Remark : null
     * DealType : 1
     * Title : 充电
     */

    public String OrderNo;
    public double Cash;
    public double FrozenCash;
    public double BackCash;
    public String TradeNum;
    public String PayType;
    public String CreateTime;
    public int ChargeVolume;
    public String Remark;
    public int DealType;
    public String Title;

    @Override
    public String toString() {
        return "BillBean{" +
                "OrderNo='" + OrderNo + '\'' +
                ", Cash=" + Cash +
                ", FrozenCash=" + FrozenCash +
                ", BackCash=" + BackCash +
                ", TradeNum='" + TradeNum + '\'' +
                ", PayType=" + PayType +
                ", CreateTime='" + CreateTime + '\'' +
                ", ChargeVolume=" + ChargeVolume +
                ", Remark=" + Remark +
                ", DealType=" + DealType +
                ", Title=" + Title +
                '}';
    }
}
