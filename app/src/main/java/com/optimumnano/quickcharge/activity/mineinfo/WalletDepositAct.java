package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：邓传亮 on 2017/4/11 10:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class WalletDepositAct extends BaseActivity {

    @Bind(R.id.act_wallet_deposit_tv_payway)
    TextView mTvPayway;
    @Bind(R.id.act_wallet_deposit_rl_payway)
    RelativeLayout mRlPayway;
    @Bind(R.id.act_wallet_deposit_et_amount)
    EditText mEtAmount;
    @Bind(R.id.act_wallet_deposit_tv_next)
    TextView mTvNext;
    private ModifyUserInformationManager mManager;
    private PayDialog mPayDialog;
    private PayWayDialog mPayWayDialog;
    private int mChosePayway=PayDialog.pay_wx;//默认使用微信充值
    private AlertDialog mChosePaywayDialog;
    private String mPayPsd;
    private String mAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_deposit);
        ButterKnife.bind(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mManager = new ModifyUserInformationManager();
        int payway = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_wx);
        if (payway == PayDialog.pay_yue)
            mChosePayway = PayDialog.pay_wx;//不能使用余额给余额充值
        mPayPsd = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_PAYPASSWORD, "");
        logi("mPayPsd "+mPayPsd);
        showPayWayStatus(mChosePayway);


        mPayDialog = new PayDialog(WalletDepositAct.this);
        mPayWayDialog = new PayWayDialog(WalletDepositAct.this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("充值");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        mPayWayDialog=null;
    }

    @OnClick({R.id.act_wallet_deposit_tv_next,R.id.act_wallet_deposit_rl_payway})
    public void onClick(View view) {
        switch (view.getId()){
                    case R.id.act_wallet_deposit_tv_next:
                        //showPayPsdDialog();
                        callPay();
                        break;
                    case R.id.act_wallet_deposit_rl_payway:
                        showChosePayWayDialog();
                        break;
                }
    }

    private void callPay() {
        showToast("调起支付");
        Intent intent = new Intent(WalletDepositAct.this, WalletDepositSuccessAct.class);
        intent.putExtra("payway",mChosePayway);
        intent.putExtra("amount",mEtAmount.getText().toString().trim());
        startActivity(intent);
    }

    private void showPayPsdDialog() {
        mAmount = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(mAmount)){
            showToast("充值金额不能为空");
            return;
        }
        mPayDialog.setPayway(mChosePayway);
        mPayDialog.setMoney(Double.valueOf(mAmount));
        mPayDialog.setStatus(PayDialog.EDTPWD);
        mPayDialog.setPayName("充值");
        mPayDialog.setPaywayListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChosePayWayDialog();
            }
        });
        mPayDialog.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==6){
                    if (!mPayPsd.equals(s.toString())){
                        showToast("支付密码错误");
                        mPayDialog.cleanPasswordView();
                    }else {
                        mPayDialog.setStatus(PayDialog.PAYSUCCESS);
                    }

                    logi("amount "+mEtAmount.getText().toString()+" mChosePayway "+mChosePayway);
                }
            }
        });

        mPayDialog.show();
    }


    private void showPayWayStatus(int payway) {
        Drawable drawable=null;
        switch (payway){
            case PayDialog.pay_wx:
                drawable= getResources().getDrawable(R.drawable.wx);
                mTvPayway.setText("微信");
                break;
            case PayDialog.pay_zfb:
                drawable= getResources().getDrawable(R.drawable.zfb);
                mTvPayway.setText("支付宝");
                break;
            case PayDialog.pay_yue:
                drawable= getResources().getDrawable(R.drawable.yue);
                mTvPayway.setText("余额");
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvPayway.setCompoundDrawables(drawable,null,null,null);
    }

    private void changePayWayStatus(int payway) {
        showPayWayStatus(payway);
        mPayDialog.setPayway(payway);
    }

    private void showChosePayWayDialog() {
        mPayWayDialog.setViewClickListener(new PayWayDialog.PayWayDialogClick() {
            @Override
            public void onMenuClick(int payway) {
                mChosePayway = payway;
                mPayWayDialog.close();
                changePayWayStatus(payway);
            }
        });
        mPayWayDialog.show();

    }

    private void dismissDialog(){
        if (null!= mPayWayDialog){
            mPayWayDialog.close();
        }
    }
}
