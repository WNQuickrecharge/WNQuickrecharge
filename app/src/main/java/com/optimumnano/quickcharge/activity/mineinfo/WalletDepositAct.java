package com.optimumnano.quickcharge.activity.mineinfo;

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
    private int mChosePayway=2;//默认余额支付
    private AlertDialog mChosePaywayDialog;
    private String mPayPsd;

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
        mPayDialog = new PayDialog(WalletDepositAct.this);

    }

    private void initData() {
        mManager = new ModifyUserInformationManager();
        mChosePayway = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, 2);
        mPayPsd = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_PAYPASSWORD, "");
        logi("mPayPsd "+mPayPsd);
        showPayWayStatus(mChosePayway);
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

    }

    @OnClick({R.id.act_wallet_deposit_tv_next,R.id.act_wallet_deposit_rl_payway})
    public void onClick(View view) {
        switch (view.getId()){
                    case R.id.act_wallet_deposit_tv_next:
                        showPayPsdDialog();
                        break;
                    case R.id.act_wallet_deposit_rl_payway:
                        alertChosePayWayDialog();
                        break;
                }
    }

    private void showPayPsdDialog() {
        String amount = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amount)){
            showToast("充值金额不能为空");
            return;
        }
        mPayDialog.setPayway(mChosePayway);
        mPayDialog.setMoney(Double.valueOf(amount));
        mPayDialog.setStatus(0);
        mPayDialog.setPaywayListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertChosePayWayDialog();
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
                    if (mPayPsd.equals(s.toString())){
                        showToast("支付密码错误");
                    }else {
                        mPayDialog.setStatus(2);
                        mPayDialog.show();
                    }
                }
            }
        });

        mPayDialog.show();
        logi("amount "+amount);
        logi("mChosePayway "+mChosePayway);
    }


    private void showPayWayStatus(int payway) {
        Drawable drawable=null;
        switch (payway){
            case 0:
                drawable= getResources().getDrawable(R.drawable.wx);
                mTvPayway.setText("微信");
                break;
            case 1:
                drawable= getResources().getDrawable(R.drawable.zfb);
                mTvPayway.setText("支付宝");
                break;
            case 2:
                drawable= getResources().getDrawable(R.drawable.yue);
                mTvPayway.setText("余额");
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvPayway.setCompoundDrawables(drawable,null,null,null);
    }

    private void changePayWayStatus(int payway) {
        showPayWayStatus(payway);
        SharedPreferencesUtil.putValue(SPConstant.SP_USERINFO,SPConstant.KEY_USERINFO_DEFPAYWAY,payway);
    }

    private void alertChosePayWayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_chose_payway_tv, null);
        builder.setView(view);
        mChosePaywayDialog = builder.show();
        view.findViewById(R.id.dialog_chose_payway_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosePayway =0;
                dismissDialog();
                changePayWayStatus(mChosePayway);
            }
        });

        view.findViewById(R.id.dialog_chose_payway_zfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosePayway =1;
                dismissDialog();
                changePayWayStatus(mChosePayway);
            }
        });

        view.findViewById(R.id.dialog_chose_payway_ye).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosePayway =2;
                dismissDialog();
                changePayWayStatus(mChosePayway);
            }
        });

        view.findViewById(R.id.dialog_chose_payway_qx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

    }

    private void dismissDialog(){
        if (null!= mChosePaywayDialog && mChosePaywayDialog.isShowing()){
            mChosePaywayDialog.dismiss();
        }
    }
}
