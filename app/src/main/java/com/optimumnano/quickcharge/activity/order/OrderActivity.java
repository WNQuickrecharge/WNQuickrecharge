package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
                    skipActivity(RechargeControlActivity.class,null);
                    payDialog.close();
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
            case R.id.dialog_chose_payment_qx:
                payWayDialog.close();
                if (isConfirm){
                    payDialog.show();
                }
                break;
            case R.id.dialog_chose_payment_wx:
                choosePayway(0);
                break;
            case R.id.dialog_chose_payment_zfb:
                choosePayway(1);
                break;
            case R.id.dialog_chose_payment_ye:
                choosePayway(2);
                break;
        }
    }

    private void choosePayway(int payway) {
        payDialog.setPayway(payway);
        payWayDialog.close();
        payDialog.show();
    }
}
