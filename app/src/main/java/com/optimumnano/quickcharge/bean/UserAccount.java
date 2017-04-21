package com.optimumnano.quickcharge.bean;

/**
 * Created by ds on 2017/4/1.
 */
public class UserAccount {

    /**
     * UserId : 19
     * IsDel : false
     * TotalCash : 0.01
     * AmmountType : 1
     * RestCash : 0.01
     * CreateTime : 2017-04-12T11:35:33.13
     * Id : 12
     * CreateBy : Register
     * FrozenCash : 0
     */

    private int UserId;
    private boolean IsDel;
    private double TotalCash;
    private int AmmountType;
    private double RestCash;
    private String CreateTime;
    private int Id;
    private String CreateBy;
    private int FrozenCash;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public boolean isIsDel() {
        return IsDel;
    }

    public void setIsDel(boolean IsDel) {
        this.IsDel = IsDel;
    }

    public double getTotalCash() {
        return TotalCash;
    }

    public void setTotalCash(double TotalCash) {
        this.TotalCash = TotalCash;
    }

    public int getAmmountType() {
        return AmmountType;
    }

    public void setAmmountType(int AmmountType) {
        this.AmmountType = AmmountType;
    }

    public double getRestCash() {
        return RestCash;
    }

    public void setRestCash(double RestCash) {
        this.RestCash = RestCash;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String CreateBy) {
        this.CreateBy = CreateBy;
    }

    public int getFrozenCash() {
        return FrozenCash;
    }

    public void setFrozenCash(int FrozenCash) {
        this.FrozenCash = FrozenCash;
    }
}
