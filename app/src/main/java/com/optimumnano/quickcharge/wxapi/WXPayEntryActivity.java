package com.optimumnano.quickcharge.wxapi;

import android.content.Intent;
import android.os.Bundle;
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


    @Bind(R.id.act_wallet_deposit_suc_tv_payway)
    TextView mTvPayway;
    @Bind(R.id.act_wallet_deposit_suc_mi_amount)
    MenuItem1 mMiAmount;
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
                    break;
                case -1:
                    showToast("支付异常");
                    logtesti("支付异常 code=-1");
                    finish();
                    break;
                case -2:
                    showToast("取消支付");
                    finish();
                    break;
                default:
                    showToast("支付异常");
                    logtesti("其他支付异常");
                    finish();
                    break;
            }
        }
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
