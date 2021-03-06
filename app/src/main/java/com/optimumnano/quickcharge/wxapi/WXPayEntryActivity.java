package com.optimumnano.quickcharge.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.mineinfo.MineWalletAct;
import com.optimumnano.quickcharge.activity.mineinfo.WalletDepositAct;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.TypeConversionUtils;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

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
            if (0 != code){ EventBus.getDefault().post(new EventManager.WeiXinPayCallback(code,"支付失败")); }
            switch (code){
                case 0:
                    showToast("支付成功");
                    String result = ((PayResp) resp).extData;// request.extData = mChosePayway+","+mAmount ;
                    logtesti("result="+result);
                    String[] resultArr = result.split(",");
                    if (resultArr.length<3){//余额支付
                        int payway = TypeConversionUtils.toInt(resultArr[0]);
                        String amount = resultArr[1];
                        mMiAmount.setRightText("¥ "+amount);
                        PayWayViewHelp.showPayWayStatus(WXPayEntryActivity.this,mTvPayway,payway);
                    }else {//订单支付
                        if (!TextUtils.isEmpty(resultArr[2])){
                            EventBus.getDefault().post(new EventManager.WeiXinPayCallback(0,resultArr[2]));
                            finish();
                        }
                    }
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
