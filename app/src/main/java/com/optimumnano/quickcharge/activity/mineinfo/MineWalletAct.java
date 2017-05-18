package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.request.GetAccountInfoRequest;
import com.optimumnano.quickcharge.response.GetAccountInfoResult;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.utils.Tool;
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
public class MineWalletAct extends BaseActivity implements HttpCallback {

    @Bind(R.id.act_mineinfo_wallet_mi_balance)
    MenuItem1 mBalance;
    @Bind(R.id.act_mineinfo_wallet_mi_trans_Bill)
    MenuItem1 mTransBill;
    @Bind(R.id.act_mineinfo_wallet_tv_payway)
    TextView mPayway;
    @Bind(R.id.act_mineinfo_wallet_mi_mycard)
    MenuItem1 mMycard;
    private PayWayDialog mPayWayDialog;
    private int mChosePayway = PayDialog.pay_yue;//默认余额支付

    private int mGetAccountInfoTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineinfo_wallet);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mPayWayDialog = new PayWayDialog(MineWalletAct.this);
//        GetMineInfoManager.getAccountInfo(new ManagerCallback() {
//            @Override
//            public void onSuccess(Object returnContent) {
//                super.onSuccess(returnContent);
//                String s = returnContent.toString();
//                UserAccount userAccount = JSON.parseObject(s, UserAccount.class);
//                double restCash = userAccount.getRestCash();
//                mBalance.setRightText(StringUtils.formatDouble(restCash));
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//            }
//        });

        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }
        mGetAccountInfoTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetAccountInfoTaskId,
                new GetAccountInfoRequest(new GetAccountInfoResult(mContext)), this));
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("我的钱包");
        mChosePayway = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue);
        PayWayViewHelp.showPayWayStatus(MineWalletAct.this, mPayway, mChosePayway);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        mPayWayDialog = null;
        mTaskDispatcher.cancel(mGetAccountInfoTaskId);
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

    private void changePayWayStatus(int payway) {
        PayWayViewHelp.showPayWayStatus(MineWalletAct.this, mPayway, payway);
        SharedPreferencesUtil.putValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, payway);
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

    private void dismissDialog() {
        if (null != mPayWayDialog) {
            mPayWayDialog.close();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBalanceChangeEvent(EventManager.onBalanceChangeEvent event) {
        mBalance.setRightText(event.balance);
        logtesti("onBalanceChangeEvent " + event.balance);
    }

    //http

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetAccountInfoTaskId == id) {
            UserAccount userAccount = ((GetAccountInfoResult) result).getResp().getResult();
            double restCash = userAccount.getRestCash();
            mBalance.setRightText(StringUtils.formatDouble(restCash));
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
