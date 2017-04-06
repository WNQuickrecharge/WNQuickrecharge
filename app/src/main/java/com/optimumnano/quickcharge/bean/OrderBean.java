package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by ds on 2017/3/29.
 */
public class OrderBean implements Serializable{
    private static final long serialVersionUID = 1L;
    public int status;//0待支付 1充电中 2已完成未评论 3已完成
    public OrderBean(){
    }

    public OrderBean(int status) {
        this.status = status;
    }
}
