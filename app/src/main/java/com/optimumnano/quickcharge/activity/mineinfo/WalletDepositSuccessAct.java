package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;

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
        Intent intent = getIntent();
        int payway = intent.getIntExtra("payway",-1);
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
                SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_BALANCE, StringUtils.formatDouble(restCash));
                EventBus.getDefault().post(new EventManager.onBalanceChangeEvent(restCash+""));
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
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
        AppManager.getAppManager().finishActivity(WalletDepositAct.class);
        finish();
    }
}
