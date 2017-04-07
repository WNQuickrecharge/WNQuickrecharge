package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.MenuItem1;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：邓传亮 on 2017/4/7 11:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class MineWalletAct extends BaseActivity {

    @Bind(R.id.act_mineinfo_wallet_mi_balance)
    MenuItem1 mBalance;
    @Bind(R.id.act_mineinfo_wallet_mi_trans_Bill)
    MenuItem1 mTransBill;
    @Bind(R.id.act_mineinfo_wallet_tv_payment)
    TextView mPayment;
    @Bind(R.id.act_mineinfo_wallet_mi_mycard)
    MenuItem1 mMycard;
    private ModifyUserInformationManager mManager;
    private AlertDialog mChosePaymentDialog;
    private int mChosePayment=3;//默认余额支付

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineinfo_wallet);
        ButterKnife.bind(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mManager = new ModifyUserInformationManager();
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("我的钱包");
        int payment = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYMENT, 3);
        showPaymentStatus(payment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        mChosePaymentDialog=null;
    }

    @OnClick({R.id.act_mineinfo_wallet_mi_balance, R.id.act_mineinfo_wallet_mi_trans_Bill, R.id.act_mineinfo_wallet_rl_payment, R.id.act_mineinfo_wallet_mi_mycard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_mineinfo_wallet_mi_balance:
                startActivity(new Intent(MineWalletAct.this, WalletBalanceAct.class));
                break;
            case R.id.act_mineinfo_wallet_mi_trans_Bill:
                startActivity(new Intent(MineWalletAct.this, WalletBillAct.class));
                break;
            case R.id.act_mineinfo_wallet_rl_payment:
                alertChosePaymentDialog();
                break;
            case R.id.act_mineinfo_wallet_mi_mycard://银行卡暂时不实现
                break;
        }
    }

    private void showPaymentStatus(int payment) {
        Drawable drawable=null;
        switch (payment){
            case 1:
                drawable= getResources().getDrawable(R.drawable.wx);
                mPayment.setText("微信");
                break;
            case 2:
                drawable= getResources().getDrawable(R.drawable.zfb);
                mPayment.setText("支付宝");
                break;
            case 3:
                drawable= getResources().getDrawable(R.drawable.icon_text_tip);
                mPayment.setText("余额");
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPayment.setCompoundDrawables(drawable,null,null,null);
    }

    private void changePaymentStatus(int payment) {
        showPaymentStatus(payment);
        SharedPreferencesUtil.putValue(SPConstant.SP_USERINFO,SPConstant.KEY_USERINFO_DEFPAYMENT,payment);
    }

    private void alertChosePaymentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_chose_payment_tv, null);
        builder.setView(view);
        mChosePaymentDialog = builder.show();
        view.findViewById(R.id.dialog_chose_payment_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosePayment=1;
                dismissDialog();
                changePaymentStatus(mChosePayment);
            }
        });

        view.findViewById(R.id.dialog_chose_payment_zfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosePayment=2;
                dismissDialog();
                changePaymentStatus(mChosePayment);
            }
        });

        view.findViewById(R.id.dialog_chose_payment_ye).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosePayment=3;
                dismissDialog();
                changePaymentStatus(mChosePayment);
            }
        });

        view.findViewById(R.id.dialog_chose_payment_qx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

    }

    private void dismissDialog(){
        if (null!=mChosePaymentDialog&&mChosePaymentDialog.isShowing()){
            mChosePaymentDialog.dismiss();
        }
    }
}
