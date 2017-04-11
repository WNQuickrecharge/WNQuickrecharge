package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MenuItem1;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.order_tvConfirm)
    TextView tvConfirm;
    @Bind(R.id.order_payway)
    MenuItem1 miPayway;
    @Bind(R.id.order_miRechargeNum)
    MenuItem1 miRechargenum;
    @Bind(R.id.order_miType)
    MenuItem1 miType;
    @Bind(R.id.order_miElectric)
    MenuItem1 miElectric;
    @Bind(R.id.order_miPower)
    MenuItem1 miPower;
    @Bind(R.id.order_miSimPrice)
    MenuItem1 miSimprice;
    @Bind(R.id.order_edtMoney)
    EditText edtMoney;
    @Bind(R.id.order_tvAllkwh)
    TextView tvAllkwh;

    private PayDialog payDialog;
    private PayWayDialog payWayDialog;
    private OrderManager orderManager = new OrderManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initViews();
        initData();
        initDialog();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电订单");

        tvConfirm.setOnClickListener(this);
        miPayway.setOnClickListener(this);
    }
    private void initData(){
        orderManager.getGunInfo("67867678901234517", new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }
    private void initDialog(){
        payDialog = new PayDialog(this);
        payWayDialog = new PayWayDialog(this);
        payDialog.setPaywayListener(this);
        payWayDialog.setViewClickListener(new PayWayDialog.PayWayDialogClick() {
            @Override
            public void onMenuClick(int payway) {
                switch (payway){
                    //微信
                    case PayDialog.pay_wx:
                        miPayway.setIvLeftDrawable(R.drawable.wx);
                        miPayway.setTvLeftText("微信");
                        break;
                    //支付寶
                    case PayDialog.pay_zfb:
                        miPayway.setIvLeftDrawable(R.drawable.zfb);
                        miPayway.setTvLeftText("支付宝");
                        break;
                    //余額
                    case PayDialog.pay_yue:
                        miPayway.setIvLeftDrawable(R.drawable.yue);
                        miPayway.setTvLeftText("余额");
                        break;
                }
            }
        });

        payDialog.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6){
                    if (s.toString().equals("123456")){
                        payDialog.setStatus(PayDialog.PAYSUCCESS);
                        skipActivity(RechargeControlActivity.class,null);
                        payDialog.close();
                    }
                    else {
                        payDialog.setStatus(PayDialog.PAYFAIL);
                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_tvConfirm:
                payDialog.setStatus(0);
                payDialog.show();
                break;
            case R.id.order_payway:
                payWayDialog.show();
                break;

        }
    }

    private void choosePayway(int payway) {
        payDialog.setPayway(payway);
        payWayDialog.close();
    }
}
