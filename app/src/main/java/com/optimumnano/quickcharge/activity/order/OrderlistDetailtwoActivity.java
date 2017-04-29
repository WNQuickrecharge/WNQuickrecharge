package com.optimumnano.quickcharge.activity.order;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.text.DecimalFormat;

public class OrderlistDetailtwoActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvDeleteOrder;
    private TextView tvStatus,tvOrdernum,tvCompany,tvAddress,tvDate,tvServiceCash;
    private OrderBean orderBean;
    private MenuItem1 miUsertime,miAllelec,miRechargeCash,miAllMoney,miSMoney,miYFMoney,miBackMoney;
    private OrderManager orderManager = new OrderManager();
    private TextView payWay;
    private ImageView orderIcon;

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
        payWay = (TextView) findViewById(R.id.orderlistDetl_tvPayWay);
        orderIcon = (ImageView) findViewById(R.id.iv_order_icon);

        tvDeleteOrder.setOnClickListener(this);
    }
    private void initData(){
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        tvOrdernum.setText(orderBean.order_no);
        tvDate.setText(orderBean.start_time);
        String formatFrozenCash = decimalFormat.format(orderBean.frozen_cash);
        miYFMoney.setRightText("￥"+formatFrozenCash);
        String formatChargeCash = decimalFormat.format(orderBean.charge_cash);
        miSMoney.setRightText("￥"+formatChargeCash);
        miAllelec.setRightText(orderBean.charge_vol+"kwh");
        double backMoney = orderBean.frozen_cash - orderBean.charge_cash;

        String format = decimalFormat.format(backMoney);
        miBackMoney.setRightText("￥"+format);
        miAllMoney.setRightText("￥"+orderBean.charge_cash);
        miUsertime.setRightText(orderBean.power_time+"分钟");
        payWay.setText(orderBean.pay_type);
        showPayWay(orderBean.pay_type);
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

    private void showPayWay(String choosepayWay){
        switch (choosepayWay) {
            case "余额支付":
                Drawable img = getResources().getDrawable(R.drawable.yue);
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                payWay.setCompoundDrawables(img,null,null,null);
                break;

            case "支付宝":
                Drawable img1 = getResources().getDrawable(R.drawable.zfb);
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                img1.setBounds(0, 0, img1.getMinimumWidth(), img1.getMinimumHeight());
                payWay.setCompoundDrawables(img1,null,null,null);
                break;
            case "微信支付":
                Drawable img2 = getResources().getDrawable(R.drawable.wx);
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                img2.setBounds(0, 0, img2.getMinimumWidth(), img2.getMinimumHeight());
                payWay.setCompoundDrawables(img2,null,null,null);
                break;
            default:
                break;
        }
    }
}
