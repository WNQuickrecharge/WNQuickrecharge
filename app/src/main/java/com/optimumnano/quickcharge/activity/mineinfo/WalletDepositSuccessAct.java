package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.views.MenuItem1;

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

        GetMineInfoManager.geAccountInfo("",new ManagerCallback(){
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                logtesti("returnContent "+returnContent.toString());

                //SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_BALANCE, NewBalance);
                //EventBus.getDefault().post(new EventManager.onBalanceChangeEvent(NewBalance));
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
                super.onFailure(msg);
            }
        } );
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
