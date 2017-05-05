package com.optimumnano.quickcharge.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.optimumnano.quickcharge.Constants.WX_APP_ID;


/**
 * 作者：邓传亮 on 2017/4/25 11:23
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @Bind(R.id.tv_pay_result)
    TextView mTvPayResult;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paysucess);
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
            String code = "" + String.valueOf(resp.errCode);
            if (code.equals("0")) {
                String result = ((PayResp) resp).extData;
                logtesti(result);
                mTvPayResult.setText(result);
            }
            else if (code.equals("-1")) {//错误
                logtesti("支付异常");
                mTvPayResult.setText("支付异常");
            }
            else if (code.equals("-2")) {
                logtesti("取消支付");
                mTvPayResult.setText("取消支付");
            }
            else {
                logtesti("其他异常");
                mTvPayResult.setText("其他异常");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.detach();
        ButterKnife.unbind(this);
    }
}
