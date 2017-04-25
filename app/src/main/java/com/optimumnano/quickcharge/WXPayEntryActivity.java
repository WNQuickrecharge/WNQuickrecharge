package com.optimumnano.quickcharge;

import android.content.Intent;
import android.os.Bundle;

import com.optimumnano.quickcharge.base.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static android.provider.UserDictionary.Words.APP_ID;


/**
 * 作者：邓传亮 on 2017/4/25 11:23
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
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
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String code = "" + String.valueOf(resp.errCode);
            if (code.equals("0")) {
                String result = ((PayResp) resp).extData;
                logtesti(result);
            } else if (code.equals("-1")) {//错误
                logtesti("支付异常");
            } else if (code.equals("-2")) {
                logtesti("取消支付");
            }else {
                logtesti("其他异常");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.detach();
    }
}
