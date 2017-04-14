package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MenuItem1;

public class OrderlistDetailtwoActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvDeleteOrder;
    private TextView tvStatus,tvOrdernum,tvCompany,tvAddress,tvDate,tvServiceCash;
    private OrderBean orderBean;
    private MenuItem1 miUsertime,miAllelec,miRechargeCash,miAllMoney,miSMoney,miYFMoney,miBackMoney;
    private OrderManager orderManager = new OrderManager();
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
        tvDeleteOrder = (TextView) findViewById(R.id.orderlistDetl_tvDeleteOrder);
        tvStatus = (TextView) findViewById(R.id.orderlistDetl_tvStatus);
        tvOrdernum = (TextView) findViewById(R.id.orderlistDetl_tvOrdernum);
        tvCompany = (TextView) findViewById(R.id.orderlistDetl_tvCompany);
        tvAddress = (TextView) findViewById(R.id.orderlistDetl_tvAddress);
        tvDate = (TextView) findViewById(R.id.orderlistDetl_tvTime);
        miUsertime = (MenuItem1) findViewById(R.id.orderdetl_miUseTime);
        miAllelec = (MenuItem1) findViewById(R.id.orderdetl_miAllelec);
        miRechargeCash = (MenuItem1) findViewById(R.id.orderdetl_miRechargeCash);
        tvServiceCash = (TextView) findViewById(R.id.orderdetl_tvServiceCash);
        miAllMoney = (MenuItem1) findViewById(R.id.orderdetl_miAllMoney);
        miSMoney = (MenuItem1) findViewById(R.id.orderdetl_miSMoney);
        miYFMoney = (MenuItem1) findViewById(R.id.orderlistDetl_miYFCash);
        miBackMoney = (MenuItem1) findViewById(R.id.orderlistDetl_miBackCash);

        tvDeleteOrder.setOnClickListener(this);
    }
    private void initData(){
        tvOrdernum.setText(orderBean.order_no);
        tvDate.setText(orderBean.start_time);
        miYFMoney.setRightText("￥"+orderBean.frozen_cash);
        miSMoney.setRightText("￥"+orderBean.charge_cash);
        miAllelec.setRightText(orderBean.charge_vol+"kwh");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderlistDetl_tvDeleteOrder:
                deleteOrder();
                break;
        }
    }
    private void deleteOrder(){
        orderManager.deleteOrder(orderBean.order_no, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg);
            }
        });
    }
}
