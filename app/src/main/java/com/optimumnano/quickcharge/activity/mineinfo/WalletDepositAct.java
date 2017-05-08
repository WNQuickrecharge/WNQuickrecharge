package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.AlipayBean;
import com.optimumnano.quickcharge.bean.WXPaySignBean;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.utils.Tool;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.optimumnano.quickcharge.Constants.WX_APP_ID;
import static com.optimumnano.quickcharge.Constants.WX_PARTNER_ID;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * 作者：邓传亮 on 2017/4/11 10:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class WalletDepositAct extends BaseActivity {

    private static final int SDK_PAY_FLAG = 001;
    @Bind(R.id.act_wallet_deposit_tv_payway)
    TextView mTvPayway;
    @Bind(R.id.act_wallet_deposit_rl_payway)
    RelativeLayout mRlPayway;
    @Bind(R.id.act_wallet_deposit_et_amount)
    EditText mEtAmount;
    @Bind(R.id.act_wallet_deposit_tv_next)
    TextView mTvNext;
    private PayDialog mPayDialog;
    private PayWayDialog mPayWayDialog;
    private int mChosePayway=PayDialog.pay_wx;//默认使用微信充值
    private AlertDialog mChosePaywayDialog;
    private String mPayPsd;
    private String mAmount;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==SDK_PAY_FLAG){
                Map mapresult =(Map) msg.obj;
                JSONObject dataJson = new JSONObject(mapresult);
                logtesti("alipayresult "+dataJson.toString());
                String resultStatus = dataJson.optString("resultStatus");// 结果码
                switch (resultStatus){
                    case "9000"://支付成功
                    case "8000"://正在处理,支付结果确认中
                    case "6004"://支付结果未知
                        showToast("支付成功");
                        float addAmount=Float.valueOf(mAmount);
                        Intent intent = new Intent(WalletDepositAct.this, WalletDepositSuccessAct.class);
                        intent.putExtra("payway",mChosePayway);
                        intent.putExtra("amount", StringUtils.formatDouble(addAmount));
                        startActivity(intent);
                        finish();

                        break;
                    case "4000":
                        showToast("订单支付失败");
                    case "5000":
                        showToast("订单不能重复支付");
                        break;
                    case "6001":
                        showToast("取消支付");
                        break;
                    case "6002":
                        showToast("网络连接出错");
                        break;
                    default:
                        showToast("支付异常");
                        break;
                }

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_deposit);
        ButterKnife.bind(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        int payway = SharedPreferencesUtil.getValue(SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_wx);
        if (payway == PayDialog.pay_yue){
            mChosePayway = PayDialog.pay_wx;//不能使用余额给余额充值
        }else {
            mChosePayway = payway;
        }
        PayWayViewHelp.showPayWayStatus(WalletDepositAct.this,mTvPayway,mChosePayway);

        mPayDialog = new PayDialog(WalletDepositAct.this);
        mPayWayDialog = new PayWayDialog(WalletDepositAct.this);
        mPayWayDialog.getPayWayItemViewById(R.id.dialog_chose_payway_ye).setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("充值");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        mPayWayDialog=null;
        mPayDialog=null;
    }

    @OnClick({R.id.act_wallet_deposit_tv_next,R.id.act_wallet_deposit_rl_payway})
    public void onClick(View view) {
        switch (view.getId()){
                    case R.id.act_wallet_deposit_tv_next:
                        Tool.hiddenSoftKeyboard(WalletDepositAct.this,getCurrentFocus());
                        if (!payCheck()) return;
                        if (mChosePayway==PayDialog.pay_zfb)
                            callALiPay();
                        else if (mChosePayway==PayDialog.pay_wx){
                            callWXPay();
                        }
                        break;
                    case R.id.act_wallet_deposit_rl_payway:
                        showChosePayWayDialog();
                        break;
                }
    }

    private void callWXPay() {
        GetMineInfoManager.getPayOrderInfoDeposit(mAmount,mChosePayway, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                logtesti("returnContent "+returnContent.toString());
                JSONObject dataJson=null;
                try {
                    dataJson = new JSONObject(returnContent.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final IWXAPI wxApi = WXAPIFactory.createWXAPI(WalletDepositAct.this, WX_APP_ID);
                //将该app注册到微信
                wxApi.registerApp(WX_APP_ID);

                String sign = dataJson.optString("sign");
                WXPaySignBean wxpayBean = JSON.parseObject(sign.replace("\\",""), WXPaySignBean.class);
                boolean isPaySupported = wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;//判断微信版本是否支持微信支付
                if (isPaySupported) {
                    PayReq request = new PayReq();
                    request.appId = WX_APP_ID;
                    request.partnerId = WX_PARTNER_ID;
                    request.prepayId = wxpayBean.prepayid;
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = wxpayBean.noncestr;
                    request.timeStamp = wxpayBean.timestamp;
                    request.sign = wxpayBean.sign;
                    request.extData = mChosePayway+","+mAmount ;
                    wxApi.sendReq(request);
                }else {
                    showToast("微信未安装或者版本过低");
                }

            }

            @Override
            public void onFailure(String msg) {
                showToast("请求失败 "+msg);
                super.onFailure(msg);
            }

        });
    }

    private void callALiPay() {

        GetMineInfoManager.getPayOrderInfoDeposit(mAmount,mChosePayway, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                logtesti("returnContent "+returnContent.toString());

                final String orderInfo = returnContent.toString();// 签名后的订单信息
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(WalletDepositAct.this);
                        AlipayBean alipayBean = JSON.parseObject(orderInfo, AlipayBean.class);
                        String sign = alipayBean.getSign();
                        LogUtil.i("sign=="+sign);
                        Map<String, String> result = alipay.payV2(sign, true);//true表示唤起loading等待界面

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();

            }

            @Override
            public void onFailure(String msg) {
                showToast("请求失败 "+msg);
                super.onFailure(msg);
            }

        });

    }

    private boolean payCheck() {
        mAmount = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(mAmount)){
            showToast("充值金额不能为空");
            return false;
        }

        if (Double.parseDouble(mAmount)==0){
            showToast("充值金额不能小于0.01");
            return false;
        }
        return true;
    }

    private void changePayWayStatus(int payway) {
        PayWayViewHelp.showPayWayStatus(WalletDepositAct.this,mTvPayway,payway);
        mPayDialog.setPayway(payway);
    }

    private void showChosePayWayDialog() {
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
        if (null!= mPayDialog){
            mPayDialog.close();
        }
    }
}
