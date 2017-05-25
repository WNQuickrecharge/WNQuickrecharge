package com.optimumnano.quickcharge.bean;

/**
 * Created by PC on 2017/4/23.
 */

public class InvoiceOrderGroup {
    public String ConsumeMonth;
    public double money;
    public boolean isChecked;

    public InvoiceOrderGroup(){

    }
    public InvoiceOrderGroup(String consumeMonth, double money, boolean isChecked) {
        ConsumeMonth = consumeMonth;
        this.money = money;
        this.isChecked = isChecked;
    }

    public String getConsumeMonth() {
        return ConsumeMonth;
    }

    public void setConsumeMonth(String consumeMonth) {
        ConsumeMonth = consumeMonth;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
