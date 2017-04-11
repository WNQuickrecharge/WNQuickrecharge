package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by mfwn on 2017/4/11.
 */

public class GunBean implements Serializable {
    public String gunNumber;
    public String gunStatus;

    public GunBean(String gunNumber, String gunStatus) {
        this.gunNumber = gunNumber;
        this.gunStatus = gunStatus;
    }
}
