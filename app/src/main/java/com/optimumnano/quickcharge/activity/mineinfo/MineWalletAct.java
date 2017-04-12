package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @Bind(R.id.act_mineinfo_wallet_tv_payway)
    TextView mPayway;
    @Bind(R.id.act_mineinfo_wallet_mi_mycard)
    MenuItem1 mMycard;
    private ModifyUserInformationManager mManager;
    private PayWayDialog mPayWayDialog;
    private int mChosePayway =PayDialog.pay_yue;//默认余额支付

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineinfo_wallet);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mManager = new ModifyUserInformationManager();
        mPayWayDialog = new PayWayDialog(MineWalletAct.this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("我的钱包");
        mChosePayway = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue);
        showPayWayStatus(mChosePayway);
        float balance = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_BALANCE, 0.0f);
        mBalance.setRightText(balance+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        EventBus.getDefault().unregister(this);
        mPayWayDialog=null;
    }

    @OnClick({R.id.act_mineinfo_wallet_mi_balance, R.id.act_mineinfo_wallet_mi_trans_Bill, R.id.act_mineinfo_wallet_rl_payway, R.id.act_mineinfo_wallet_mi_mycard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_mineinfo_wallet_mi_balance:
                startActivity(new Intent(MineWalletAct.this, WalletBalanceAct.class));
                break;
            case R.id.act_mineinfo_wallet_mi_trans_Bill:
                startActivity(new Intent(MineWalletAct.this, WalletBillAct.class));
                break;
            case R.id.act_mineinfo_wallet_rl_payway:
                alertChosePayWayDialog();
                break;
            case R.id.act_mineinfo_wallet_mi_mycard://银行卡暂时不实现
                break;
        }
    }

    private void showPayWayStatus(int payway) {
        Drawable drawable=null;
        switch (payway){
            case PayDialog.pay_wx:
                drawable= getResources().getDrawable(R.drawable.wx);
                mPayway.setText("微信");
                break;
            case PayDialog.pay_zfb:
                drawable= getResources().getDrawable(R.drawable.zfb);
                mPayway.setText("支付宝");
                break;
            case PayDialog.pay_yue:
                drawable= getResources().getDrawable(R.drawable.yue);
                mPayway.setText("余额");
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPayway.setCompoundDrawables(drawable,null,null,null);
    }

    private void changePayWayStatus(int payway) {
        showPayWayStatus(payway);
        SharedPreferencesUtil.putValue(SPConstant.SP_USERINFO,SPConstant.KEY_USERINFO_DEFPAYWAY,payway);
    }

    private void alertChosePayWayDialog() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBalanceChangeEvent(EventManager.onBalanceChangeEvent event) {
        mBalance.setRightText(event.balance);
        logtesti("onBalanceChangeEvent "+event.balance);
    }
}
