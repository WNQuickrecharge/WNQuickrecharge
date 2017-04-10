package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.views.MenuItem1;

public class OrderlistDetailtwoActivity extends BaseActivity {

    private TextView tvStatus,tvOrdernum,tvCompany,tvAddress,tvDate;
    private OrderBean orderBean;
    private MenuItem1 miGunNum,miPileType,miElec,miPower,miForzenCatsh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist_detailtwo);
        getExtras();
        initViews();
        initData();
    }
    private void getExtras(){
        orderBean = (OrderBean) getIntent().getExtras().getSerializable("orderbean");
    }
    @Override
    public void initViews() {
        super.initViews();
        setTitle("订单详情");
    }
    private void initData(){

    }
}
