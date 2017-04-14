package com.optimumnano.quickcharge.activity.mineinfo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.alipay.util.OrderInfoUtil2_0;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private ModifyUserInformationManager mManager;
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
                Object obj = msg.obj;
                String jsonresult=obj.toString();

                JSONObject dataJson = null;
                try {
                    dataJson = new JSONObject(jsonresult);
                } catch (JSONException e) {
                    e.printStackTrace();
                    logtesti("parse error "+e.getMessage());
                    return;
                }
                String resultStatus = dataJson.optString("resultStatus");// 结果码
                switch (resultStatus){
                    case "9000":
                        showToast("支付成功");
                        break;
                    case "8000":
                        showToast("正在处理中");
                        break;
                    case "4000":
                        showToast("订单支付失败");
                        break;
                    case "5000":
                        showToast("重复请求");
                        break;
                    case "6001":
                        showToast("取消支付");
                        break;
                    case "6002":
                        showToast("网络连接出错");
                        break;
                    case "6004":
                        showToast("支付结果未知");
                        break;
                    default:
                        showToast("支付异常");
                        break;
                }


                showToast(jsonresult);
                logtesti("alipayresult "+jsonresult);
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
        mManager = new ModifyUserInformationManager();
        int payway = SharedPreferencesUtil.getValue(SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_wx);
        if (payway == PayDialog.pay_yue)
            mChosePayway = PayDialog.pay_wx;//不能使用余额给余额充值
        mPayPsd = SharedPreferencesUtil.getValue(SP_USERINFO, SPConstant.KEY_USERINFO_PAYPASSWORD, "");
        logtesti("mPayPsd "+mPayPsd);
        showPayWayStatus(mChosePayway);


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
//                        showPayPsdDialog();
                        callPay();
                        break;
                    case R.id.act_wallet_deposit_rl_payway:
                        showChosePayWayDialog();
                        break;
                }
    }

    private void callPay() {
        mAmount = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(mAmount)){
            showToast("充值金额不能为空");
            return;
        }
        //TODO
        showToast("调起支付");

        ModifyUserInformationManager.walletBalanceDeposit(mAmount, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                logtesti("returnContent "+returnContent.toString());

                String APP_ID="2017041306678802";
                String APP_PRIVATE_KEY="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCMhyZqQnMVlQ8g3dqK4Ch+KXT8ef0Oj0KI7RozvgmJ92cbU34S0ClLmHWdBxeJToghZRbxFXXppmODcYFwbo7fLMRbqL83GF4CzQuqrFkrSvE6JCO+fgvC0zh/dUfPdR0n205q7GwupjniijbS4Q7X9U/XR0MRoGFnfi+ZjenDLWHVSF9IbWV5cDqV9YJFCjUU+rFTgw3qaNF0EIFqujw0HOCn3A0Mj8Wp7W0Z3egqZ2gGI7kEQOwASVPftVhmU1nTxRaoAiOLE0oLUZNARbTQvidoyk+t9cmvqJ6J0KLvDPCOt5C9etFbzepwlKleaNEAiv3HM0QaoSEd1VVwOv2vAgMBAAECggEAIzRQWVpgPk3jRlaNwzC4tDJqjj15OcaF4ouTftbiyN9jwyK9eLURQ1DkVfxK1ykHTWZnwumfanM2ht1Okf4AaMRsRJIXpRPDqWv4uj8G76OMnwYitjwZcis8AiI9ZSlvrmZwVLT5vQ4Dfk8lwNqEv3FDGSlPEgFdpXGlNxCoOS6qtwCjUWPRMJwrBvozTy1YyxgdKxX2sj7hkO7vEsz7qJi0p4R7Ra6hNQAjI7sUiWJq69iPCQqLoM4t/T3wXgZVdWxcCKuNomiY6DqsQyAUFJUGpdWtZTuquIxSwj6uAReVBOIJvtuQGe0utaqNPyD7cmRZvcEfSTxXNnWX8Zjp4QKBgQDjPaqWUqjQ2P9B2sa3VWUx4UiGPeueqFf1vQGS/OhRavno7/9/ElbtLlJcKOOEWBtAGZk/iuNFlmOh33ifHgTqf6xP9gbcnND363fo9iZTbAPR1enP1LpgIMIGmyyI47qAMQ9RwtFLvaxTxSzGI4LLqNIKbdOBIsg+wQVwb9De6QKBgQCeUBZfnZ7eV5JAgHkSVDT22H79OSgDUpHH+kk3cBh7dfWyw5nGS6X68sZhXsct9Q3fyc8Biq4g+2EtORbO/Out29liixGZ2lv3PNPzXBx2HxLpmMwNyi8oQLvxXNvLdftuJPog9wY4iR+dLI47NXABvCRpuhDwriGJ+JHsT8SI1wKBgAkL3munlMLjsJ29iOWph6LJtipp8qRZTU8iHBru6Iy9Nn+4djJn14APtQWoNw6At20A1+H4tH2DtCmwjMvA1S9Er+ZrA/DqaUNFY3upuPLfPPOwkWNNs7G5/B9pNAerETSjP1ng6JYcUezvz41/wmg7K0lw/9eHO/OLmZzVRrppAoGBAJxqpLB0G7Oy8l2okfb6HBqIbW/kORSPUFo1gIG1z3qsYcXuXND/jU8U78zkyAcwSDUEP/NWAhW0SZ9U+KQqYPG2dmJW5V0N0i/FeeTI7ZYHvbfvsC0vxsgOraMcDE5Kk9GP11Mavpq9YO76Rs1ZTwFn0tJjdLlYUurvHb5OUfChAoGBAITNf7jhc/T+6VxCvpAbjDBOKF67vJO5tdQJziY3AAMWZWHHQhFCDfpM2JQnNBrZNXDLU2n4o0AgPJnEMyDFwV4OOR2Z7f8gnc9TNnFo1x1hIs53nhjxdu7wJr51D/tlex4PRFPZ/H5tCB+ihQjyEojgjObY7aeRqJFY1rZ3B2AM";

                JSONObject dataJson = null;
                try {
                    dataJson = new JSONObject(returnContent.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String orderNum = dataJson.optString("d_order_no");// 订单号

                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APP_ID, true, orderNum, mAmount);
                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
                String sign = OrderInfoUtil2_0.getSign(params, APP_PRIVATE_KEY, true);
                final String orderInfo = orderParam + "&" + sign;

                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(WalletDepositAct.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);//true表示唤起loading等待界面

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();


                /*showToast("充值成功");

                JSONObject dataJson = null;
                try {
                    dataJson = new JSONObject(returnContent.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String newBalanceStr = dataJson.optString("rest_cash");
                logtesti("newBalanceStr "+newBalanceStr);
                DecimalFormat df = new DecimalFormat("0.00");
                float addAmount=Float.valueOf(mAmount);
                float newBalance=Float.valueOf(newBalanceStr);

                String formatAddAmount = df.format(addAmount);
                String formatNewBalance = df.format(newBalance);
                EventBus.getDefault().post(new EventManager.onBalanceChangeEvent(formatNewBalance));
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_BALANCE, formatNewBalance);

                Intent intent = new Intent(WalletDepositAct.this, WalletDepositSuccessAct.class);
                intent.putExtra("payway",mChosePayway);
                intent.putExtra("amount",formatAddAmount);
                startActivity(intent);
                finish();*/
            }

            @Override
            public void onFailure(String msg) {
                showToast(msg);
                super.onFailure(msg);
            }

        });

    }

    private void showPayPsdDialog() {
        mAmount = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(mAmount)){
            showToast("充值金额不能为空");
            return;
        }
        mPayDialog.setPayway(mChosePayway);
        mPayDialog.setMoney(Double.valueOf(mAmount));
        mPayDialog.setStatus(PayDialog.EDTPWD);
        mPayDialog.setPayName("充值");
        mPayDialog.setPaywayListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChosePayWayDialog();
            }
        });
        mPayDialog.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==6){
                    if (!mPayPsd.equals(s.toString())){
                        showToast("支付密码错误");
                        mPayDialog.cleanPasswordView();
                    }else {
                        callPay();
                        dismissDialog();
//                        mPayDialog.setStatus(PayDialog.PAYSUCCESS);
                    }

                    logtesti("amount "+mEtAmount.getText().toString()+" mChosePayway "+mChosePayway);
                }
            }
        });

        mPayDialog.show();
    }


    private void showPayWayStatus(int payway) {
        Drawable drawable=null;
        switch (payway){
            case PayDialog.pay_wx:
                drawable= getResources().getDrawable(R.drawable.wx);
                mTvPayway.setText("微信");
                break;
            case PayDialog.pay_zfb:
                drawable= getResources().getDrawable(R.drawable.zfb);
                mTvPayway.setText("支付宝");
                break;
            case PayDialog.pay_yue:
                drawable= getResources().getDrawable(R.drawable.yue);
                mTvPayway.setText("余额");
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvPayway.setCompoundDrawables(drawable,null,null,null);
    }

    private void changePayWayStatus(int payway) {
        showPayWayStatus(payway);
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
