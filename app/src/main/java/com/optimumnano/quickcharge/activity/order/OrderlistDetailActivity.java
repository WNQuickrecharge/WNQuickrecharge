package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.views.MenuItem1;

public class OrderlistDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvPay,tvCancel,tvWatchStatus;
    private TextView tvStatus,tvOrdernum,tvCompany,tvAddress,tvDate;
    private OrderBean orderBean;
    private MenuItem1 miGunNum,miPileType,miElec,miPower,miForzenCatsh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist_detail);
        getExtras();
        initViews();
        initListener();
        initData();
    }
    private void getExtras(){
        orderBean = (OrderBean) getIntent().getExtras().getSerializable("orderbean");
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("订单详情");

        tvPay = (TextView) findViewById(R.id.orderlistDetl_tvPay);
        tvCancel = (TextView) findViewById(R.id.orderdtel_tvCancel);
        tvWatchStatus = (TextView) findViewById(R.id.orderlistDetl_tvWatchStatus);
        tvStatus = (TextView) findViewById(R.id.orderlistDetl_tvStatus);
        miGunNum = (MenuItem1) findViewById(R.id.orderlistDetl_miGunNum);
        miPileType = (MenuItem1) findViewById(R.id.orderlistDetl_miPileType);
        miElec = (MenuItem1) findViewById(R.id.orderlistDetl_miElec);
        miPower = (MenuItem1) findViewById(R.id.orderlistDetl_miPower);
        miForzenCatsh = (MenuItem1) findViewById(R.id.orderlistDetl_miFrozenCash);
        tvOrdernum = (TextView) findViewById(R.id.orderlistDetl_tvOrdernum);
        tvCompany = (TextView) findViewById(R.id.orderlistDetl_tvCompany);
        tvAddress = (TextView) findViewById(R.id.orderlistDetl_tvAddress);
        tvDate = (TextView) findViewById(R.id.orderlistDetl_tvTime);
    }
    private void initListener(){
        tvPay.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvWatchStatus.setOnClickListener(this);
    }
    private void initData(){
        if (orderBean.order_status==2){
            tvStatus.setText("待支付");
            tvPay.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
            tvWatchStatus.setVisibility(View.GONE);
        }
        else {
            tvStatus.setText("充电中");
            tvPay.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            tvWatchStatus.setVisibility(View.VISIBLE);
        }
        tvOrdernum.setText(orderBean.order_no);
        miGunNum.setRightText(orderBean.gun_code);
        miPileType.setRightText(orderBean.pile_type);
        miElec.setRightText(orderBean.elec_current+"A");
        miPower.setRightText(orderBean.power+"/kwh");
        miForzenCatsh.setRightText("￥"+orderBean.frozen_cash);
        tvDate.setText(orderBean.start_time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.orderlistDetl_tvPay:
                skipActivity(OrderActivity.class,null);
                break;
            case R.id.orderdtel_tvCancel:

                break;
            case R.id.orderlistDetl_tvWatchStatus:

                break;
            default:
                break;
        }
    }
}
