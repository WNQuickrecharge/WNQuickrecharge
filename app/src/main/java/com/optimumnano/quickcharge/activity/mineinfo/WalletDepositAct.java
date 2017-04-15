package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
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
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.text.DecimalFormat;
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
                        DecimalFormat df = new DecimalFormat("0.00");
                        float addAmount=Float.valueOf(mAmount);
                        String formatAddAmount = df.format(addAmount);
                        Intent intent = new Intent(WalletDepositAct.this, WalletDepositSuccessAct.class);
                        intent.putExtra("payway",mChosePayway);
                        intent.putExtra("amount",formatAddAmount);
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
                        if (mChosePayway==PayDialog.pay_zfb)
                            callALiPay();
                        else if (mChosePayway==PayDialog.pay_wx){
                            //callWXPay();
                        }
                        break;
                    case R.id.act_wallet_deposit_rl_payway:
                        showChosePayWayDialog();
                        break;
                }
    }

    private void callALiPay() {
        mAmount = mEtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(mAmount)){
            showToast("充值金额不能为空");
            return;
        }

        GetMineInfoManager.getALiPayOrderInfoDeposit(mAmount, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                logtesti("returnContent "+returnContent.toString());

                final String orderInfo = returnContent.toString();// 签名后的订单信息
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
                        callALiPay();
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
