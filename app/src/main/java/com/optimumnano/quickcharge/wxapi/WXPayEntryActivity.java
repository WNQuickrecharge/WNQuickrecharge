package com.optimumnano.quickcharge.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.mineinfo.MineWalletAct;
import com.optimumnano.quickcharge.activity.mineinfo.WalletDepositAct;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.optimumnano.quickcharge.Constants.WX_APP_ID;


/**
 * 作者：邓传亮 on 2017/4/25 11:23
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Bind(R.id.iv_pay_result)
    ImageView mIvPayResult;
    @Bind(R.id.tv_pay_result)
    TextView mTvPayResult;
    @Bind(R.id.act_wallet_deposit_suc_tv_payway)
    TextView mTvPayway;
    @Bind(R.id.rl_pay_info_payway)
    RelativeLayout mRlPayInfoPayway;
    @Bind(R.id.act_wallet_deposit_suc_mi_amount)
    MenuItem1 mMiAmount;
    @Bind(R.id.act_wallet_deposit_suc_tv_back)
    TextView mTvBack;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_deposit_suc);
        ButterKnife.bind(this);
        initViews();
        setTitle("支付结果");
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        logtesti("开始请求");
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            switch (code){
                case 0:
                    Intent intent = getIntent();
                    int payway = intent.getIntExtra("payway", -1);
                    String amount = intent.getStringExtra("amount");
                    mMiAmount.setRightText("¥ "+amount);
                    PayWayViewHelp.showPayWayStatus(WXPayEntryActivity.this,mTvPayway,payway);
                    showToast("支付成功");
                    mTvPayResult.setText("支付成功");
                    break;
                case -1:
                    logtesti("支付异常");
                    mTvPayResult.setText("支付异常");
                    showPayFail();
                    break;
                case -2:
                    mTvPayResult.setText("取消支付");
                    showToast("取消支付");
                    showPayFail();
                    break;
                default:
                    logtesti("其他异常");
                    mTvPayResult.setText("支付出错");
                    showPayFail();
                    break;
            }
        }
    }

    private void showPayFail() {
        mIvPayResult.setImageResource(R.drawable.pay_fail);
        mRlPayInfoPayway.setVisibility(View.INVISIBLE);
        mMiAmount.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.detach();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.act_wallet_deposit_suc_tv_back)
    public void onClick() {
        Intent intent = new Intent(WXPayEntryActivity.this, MineWalletAct.class);
        startActivity(intent);
        AppManager.getAppManager().finishActivity(WalletDepositAct.class);
        finish();
    }
}
