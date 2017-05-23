package com.optimumnano.quickcharge.activity.invoice;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.order.OrderActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.InvoiceManager;
import com.optimumnano.quickcharge.request.GetAccountInfoRequest;
import com.optimumnano.quickcharge.request.GetInvoiceSignRequest;
import com.optimumnano.quickcharge.request.GetPayPwdRequest;
import com.optimumnano.quickcharge.request.PayInvoiceBalanceRequest;
import com.optimumnano.quickcharge.response.GetAccountInfoResult;
import com.optimumnano.quickcharge.response.GetInvoiceSignResult;
import com.optimumnano.quickcharge.response.GetPayPwdResult;
import com.optimumnano.quickcharge.response.PayInvoiceBalanceResult;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 支付中心界面
 */

public class PayCenterActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback, HttpCallback {
    @Bind(R.id.paycenter_miMoney)
    MenuItem1 miMoney;
//    @Bind(R.id.paycenter_tvPayway)
//    TextView tvPayway;
    @Bind(R.id.paycenter_tvNext)
//    TextView tvNext;
//    @Bind(R.id.paycenter_rlPayway)
    RelativeLayout rlPayway;
    @Bind(R.id.paycenter_miOrderno)
    MenuItem1 miOrderno;
    @Bind(R.id.order_payway)
    MenuItem1 miPayway;

    private double money = 0;
    private double allMoney = 0;

    private String formatRestCash;
    private double restCash;

    /**
     * 支付方式弹窗
     */
    private PayDialog payDialog;

    private PayWayDialog payWayDialog;

    private int payWay;//支付方式

    private String order_no;
    private int payType = 3;
    private InvoiceManager manager = new InvoiceManager();

    private int mGetInvoiceSignTaskId;
    private int mPayInvoiceBalanceTaskId;
    private int mGetPayPwdTaskId;
    /**
     * 请求账户信息识别ID
     */
    private int mGetAccountInfoTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_center);
        ButterKnife.bind(this);
        getExtras();
        initViews();
        initDialog();
        initAcountInfoData();
    }

    private void getExtras() {
        money = getIntent().getExtras().getDouble("money");
        allMoney = getIntent().getExtras().getDouble("allmoney");
        order_no = getIntent().getExtras().getString("order_no");
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("支付中心");

        miMoney.setRightText("￥" + money);
        miOrderno.setRightText(order_no);

        miPayway.setOnClickListener(this);
        rlPayway.setOnClickListener(this);
//        tvNext.setOnClickListener(this);
    }

    private void initAcountInfoData() {
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }
        mGetAccountInfoTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetAccountInfoTaskId,
                new GetAccountInfoRequest(new GetAccountInfoResult(mContext)), this));

    }

    private void initDialog() {
        payDialog = new PayDialog(this);
        payDialog.setPayCallback(this);
        payWayDialog = new PayWayDialog(this);
        payDialog.setPaywayListener(this);
        payWayDialog.setViewClickListener(new PayWayDialog.PayWayDialogClick() {
            @Override
            public void onMenuClick(int payway) {
                PayWayViewHelp.showPayWayStatus(miPayway, payway, formatRestCash);
                payWay = payway;
            }
        });
    }

    /*int paway = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue);
    payDialog.setPayway(paway);
    payDialog.setMoney(item.frozen_cash,item.order_no);
    if (PayDialog.pay_yue == paway){
        payDialog.setStatus(PayDialog.EDTPWD);
    }else {
        payDialog.setStatus(PayDialog.PAYBT);
    }
    payDialog.show();*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paycenter_tvNext:
                //邮费为0则直接成功，不用支付
                if (money > 0) {
                    if (payType == PayDialog.pay_yue) {//余额

                        payDialog.show();
//                        doPayPwdRequset();
//                        payDialog.payYue();
                    } else if (payType == PayDialog.pay_zfb) {//支付宝


                    } else if (payType == PayDialog.pay_wx) {//微信


                    }
                } else {
                    toInvoiceApply();
                }
                break;
            case R.id.order_payway:
//                choosePayway();
                payWayDialog.show();
                break;
        }
    }
/*

    private void choosePayway() {
        if (payWayDialog == null) {
            payWayDialog = new PayWayDialog(this);
            payWayDialog.setViewClickListener(new PayWayDialog.PayWayDialogClick() {
                @Override
                public void onMenuClick(int payway) {
                    PayWayViewHelp.showPayWayStatus(PayCenterActivity.this, tvPayway, payway);
                    payType = payway;
                }
            });
        }
        payWayDialog.show();
    }
*/

//
//        manager.payBalance(order_no, money, new ManagerCallback() {
//            @Override
//            public void onSuccess(Object returnContent) {
//                super.onSuccess(returnContent);
//                toInvoiceApply();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//                showToast(msg);
//            }
//        });

    /**
     * 余额支付
     */
    private void doPayPwdRequset(){
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }
        mGetPayPwdTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetPayPwdTaskId,
                new GetPayPwdRequest(new GetPayPwdResult(mContext)), PayCenterActivity.this));
    }
    private void payYue() {
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
        } else {
            mPayInvoiceBalanceTaskId = TaskIdGenFactory.gen();
            mTaskDispatcher.dispatch(new HttpTask(mPayInvoiceBalanceTaskId,
                    new PayInvoiceBalanceRequest(new PayInvoiceBalanceResult(mContext), order_no, money), this));
        }

    }

    /**
     * 支付宝支付
     */
//    private void payZfb() {
//        manager.getInvoiceSign(order_no, PayDialog.pay_zfb, new ManagerCallback<InvoiceSignRsp>() {
//            @Override
//            public void onSuccess(InvoiceSignRsp returnContent) {
//                super.onSuccess(returnContent);
//                pay(returnContent.sign);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//                showToast(msg);
//            }
//        });
//        if (!Tool.isConnectingToInternet()) {
//            showToast("无网络");
//        } else {
//            mGetInvoiceSignTaskId = TaskIdGenFactory.gen();
//            mTaskDispatcher.dispatch(new HttpTask(mGetInvoiceSignTaskId,
//                    new GetInvoiceSignRequest(new GetInvoiceSignResult(mContext), order_no, PayDialog.pay_zfb), this));
//        }
//
//    }
/*
    private void pay(String sign) {
        if (payDialog == null) {
            payDialog = new PayDialog(this);
            payDialog.setMoney(money, order_no, sign, payType);
            payDialog.setPayCallback(this);
        }
        payDialog.show();
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
//        mTaskDispatcher.cancel(mGetInvoiceSignTaskId);
//        mTaskDispatcher.cancel(mPayInvoiceBalanceTaskId);
        mTaskDispatcher.cancel(mGetAccountInfoTaskId);
    }

    @Override
    public void paySuccess(String oder_no) {
        toInvoiceApply();
    }

    private void toInvoiceApply() {
        Bundle bundle = new Bundle();
        bundle.putDouble("money", allMoney);
        bundle.putString("order_no", order_no);
        skipActivity(InvoiceApplyActivity.class, bundle);
        finish();
    }

    @Override
    public void payFail(String msg) {

    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        /**
         * 获取余额值，显示到支付界面
         */
        if (mGetAccountInfoTaskId == id) {
            UserAccount userAccount = ((GetAccountInfoResult) result).getResp().getResult();
            restCash = userAccount.getRestCash();
            DecimalFormat df = new DecimalFormat("0.00");
            formatRestCash = df.format(restCash);

//            tvPayway.setText("余额(剩余" + formatRestCash + ")");
           /* if (payWay == PayDialog.pay_yue) {
                miPayway.setTvLeftText("余额" + "(" + formatRestCash + ")");
            } else if (payWay == PayDialog.pay_zfb) {
                PayWayViewHelp.showPayWayStatus(miPayway, payWay, formatRestCash);
                miPayway.setTvLeftText("支付宝");
            } else if (payWay == PayDialog.pay_wx) {
                PayWayViewHelp.showPayWayStatus(miPayway, payWay, formatRestCash);
                miPayway.setTvLeftText("微信");
            }*/
        }
        if(mGetPayPwdTaskId == id){


        }
        if (mGetInvoiceSignTaskId == id) {
//            pay(((GetInvoiceSignResult) result).getResp().getResult().sign);
        } else if (mPayInvoiceBalanceTaskId == id) {
            toInvoiceApply();
        }
    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetInvoiceSignTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((GetInvoiceSignResult) result).getResp()));
        } else if (mPayInvoiceBalanceTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((PayInvoiceBalanceResult) result).getResp()));
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
