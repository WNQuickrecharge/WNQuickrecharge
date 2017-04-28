package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MenuItem1;

import static com.optimumnano.quickcharge.R.id.iv_order_icon;

public class OrderlistDetailActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback {
    private TextView tvPay,tvCancel,tvWatchStatus;
    private TextView tvStatus,tvOrdernum,tvCompany,tvAddress,tvDate;
    private OrderBean orderBean;
    private MenuItem1 miGunNum,miPileType,miElec,miPower,miForzenCatsh;

    private PayDialog payDialog;
    private OrderManager orderManager = new OrderManager();
    private ImageView orderIcon;

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
        orderIcon = (ImageView) findViewById(iv_order_icon);
    }
    private void initListener(){
        tvPay.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvWatchStatus.setOnClickListener(this);
    }
    private void initData(){
        payDialog = new PayDialog(this);
        payDialog.setMoney(orderBean.frozen_cash,orderBean.order_no);
        payDialog.setPayCallback(this);
        if (orderBean.order_status == 1){
            tvStatus.setText("已取消");
            tvPay.setVisibility(View.VISIBLE);
            tvPay.setText("确定");
            tvCancel.setVisibility(View.GONE);
            tvWatchStatus.setVisibility(View.GONE);
        }
        else if (orderBean.order_status==2){
            tvPay.setText("支付");
            tvStatus.setText("待支付");
            tvPay.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
            tvWatchStatus.setVisibility(View.GONE);
        }
        else if (orderBean.order_status == 3){
            tvStatus.setText("待充电");
            tvPay.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            tvWatchStatus.setVisibility(View.VISIBLE);
        }
        else if (orderBean.order_status == 4){
            tvStatus.setText("充电中");
            tvPay.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            tvWatchStatus.setVisibility(View.VISIBLE);
        }
        tvOrdernum.setText(orderBean.order_no);
        miGunNum.setRightText(orderBean.gun_code);
        miPileType.setRightText(orderBean.pile_type);
        miElec.setRightText(orderBean.elec_current+"A");
        miPower.setRightText(orderBean.power+"kw");
        miForzenCatsh.setRightText("￥"+orderBean.frozen_cash);
        tvDate.setText(orderBean.start_time);
        switch (orderBean.charge_from) {
            case 1:
                orderIcon.setImageResource(R.mipmap.chongdianzhuang);
            break;
            case 2:
                orderIcon.setImageResource(R.mipmap.budianche);
            break;

            default :

            break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.orderlistDetl_tvPay:
                if (orderBean.order_status == 1){
                    finish();
                }
                else {
                    payDialog.show();
                }
                break;
            case R.id.orderdtel_tvCancel:
                cancelOrder();
                break;
            case R.id.orderlistDetl_tvWatchStatus:
                Bundle bundle = new Bundle();
                if (orderBean.order_status == 3){//待充电
                    bundle.putInt("order_status", Constants.STARTCHARGE);
                }
                if (orderBean.order_status == 4){//充电中
                    bundle.putInt("order_status", Constants.GETCHARGEPROGRESS);
                }
                bundle.putString("order_no",orderBean.order_no);
                skipActivity(RechargeControlActivity.class,bundle);
                break;
            default:
                break;
        }
    }

    private void cancelOrder(){
        orderManager.cancelOrder(orderBean.order_no, new ManagerCallback<String>() {
            @Override
            public void onSuccess(String returnContent) {
                super.onSuccess(returnContent);
                showToast("取消成功");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }


    @Override
    public void paySuccess(String order_no) {
        Bundle bundle = new Bundle();
        bundle.putString("order_no",orderBean.order_no);
        bundle.putInt("order_status", Constants.STARTCHARGE);
        skipActivity(RechargeControlActivity.class,bundle);
        finish();
    }

    @Override
    public void payFail(String msg) {
        showToast(msg);
    }
}
