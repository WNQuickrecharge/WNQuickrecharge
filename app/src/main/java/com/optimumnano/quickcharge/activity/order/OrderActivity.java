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

    private boolean isConfirm = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        initViews();
        initDialog();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电订单");

        tvConfirm.setOnClickListener(this);
        miPayway.setOnClickListener(this);
    }
    private void initDialog(){
        payDialog = new PayDialog(this);
        payWayDialog = new PayWayDialog(this);
        payDialog.setPaywayListener(this);
        payWayDialog.setViewClickListener(this);

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
                        payDialog.setStatus(1);
                        skipActivity(RechargeControlActivity.class,null);
                        payDialog.close();
                    }
                    else {
                        payDialog.setStatus(2);
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
                isConfirm = true;
                payDialog.setStatus(0);
                payDialog.show();
                break;
            case R.id.order_payway:
                isConfirm = false;
                payWayDialog.show();
                break;
            //支付彈框 修改支付方式
            case R.id.pay_payWay:
                payDialog.close();
                payWayDialog.show();
                break;
            //弹框关闭按钮
            case R.id.dialog_chose_payment_qx:
                payWayDialog.close();
                if (isConfirm){
                    payDialog.show();
                }
                break;
            //微信支付
            case R.id.dialog_chose_payment_wx:
                choosePayway(0);
                break;
            //支付宝支付
            case R.id.dialog_chose_payment_zfb:
                choosePayway(1);
                break;
            //余额支付
            case R.id.dialog_chose_payment_ye:
                choosePayway(2);
                break;
            //修改密码
            case R.id.pay_tvUpdatePwd:

                break;
            //重新输入
            case R.id.pay_tvReInput:
                payDialog.setStatus(0);
                break;
        }
    }

    private void choosePayway(int payway) {
        payDialog.setPayway(payway);
        payWayDialog.close();
        if (isConfirm) {
            payDialog.show();
        }
    }
}
