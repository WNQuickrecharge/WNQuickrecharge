package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_BALANCE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * 作者：邓传亮 on 2017/4/11 12:07
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class WalletDepositSuccessAct extends BaseActivity {

    @Bind(R.id.act_wallet_deposit_suc_tv_payway)
    TextView mTvPayway;
    @Bind(R.id.act_wallet_deposit_suc_mi_amount)
    MenuItem1 mMiAmount;
    @Bind(R.id.act_wallet_deposit_suc_tv_back)
    TextView mTvBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_deposit_suc);
        ButterKnife.bind(this);
        initData();
        initViews();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        DecimalFormat df = new DecimalFormat("0.00");
        Intent intent = getIntent();
        int payway = intent.getIntExtra("payway",PayDialog.pay_wx);
        String amount = intent.getStringExtra("amount");
        showPayWayStatus(payway);

        String oldBalance = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_BALANCE, "");
        logtesti("oldBalance "+oldBalance);
        float addBalance=Float.valueOf(amount);
        float finalBalance=Float.valueOf(oldBalance)+addBalance;
        mMiAmount.setRightText("¥ "+df.format(addBalance));

        String formatBalance = df.format(finalBalance);
        EventBus.getDefault().post(new EventManager.onBalanceChangeEvent(formatBalance));
        SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_BALANCE, formatBalance);
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

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        setTitle("充值详情");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick(R.id.act_wallet_deposit_suc_tv_back)
    public void onClick() {
        Intent intent = new Intent(WalletDepositSuccessAct.this, MineWalletAct.class);
        startActivity(intent);
        finish();
    }
}
