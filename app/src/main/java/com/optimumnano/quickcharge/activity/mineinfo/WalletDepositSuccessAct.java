package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        Intent intent = getIntent();
        int payway = intent.getIntExtra("payway",PayDialog.pay_wx);
        String amount = intent.getStringExtra("amount");
        mMiAmount.setRightText("¥ "+amount);
        PayWayViewHelp.showPayWayStatus(WalletDepositSuccessAct.this,mTvPayway,payway);
        GetMineInfoManager.getAccountInfo(new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                String s = returnContent.toString();
                UserAccount userAccount = JSON.parseObject(s, UserAccount.class);
                double restCash = userAccount.getRestCash();
                EventBus.getDefault().post(new EventManager.onBalanceChangeEvent(restCash+""));
                DecimalFormat df = new DecimalFormat("0.00");
                SharedPreferencesUtil.putValue(SPConstant.SP_USERINFO,SPConstant.KEY_USERINFO_BALANCE,df.format(restCash));
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg);
            }
        });
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
