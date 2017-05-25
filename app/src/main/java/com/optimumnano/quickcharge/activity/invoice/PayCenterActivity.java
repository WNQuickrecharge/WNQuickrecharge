package com.optimumnano.quickcharge.activity.invoice;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.mineinfo.WalletDepositAct;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.bean.WXPaySignBean;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.request.GetAccountInfoRequest;
import com.optimumnano.quickcharge.request.GetInvoiceSignRequest;
import com.optimumnano.quickcharge.request.GetPayPwdRequest;
import com.optimumnano.quickcharge.request.PayInvoiceBalanceRequest;
import com.optimumnano.quickcharge.request.PayOrderInfoDepositRequest;
import com.optimumnano.quickcharge.response.GetAccountInfoResult;
import com.optimumnano.quickcharge.response.GetInvoiceSignResult;
import com.optimumnano.quickcharge.response.GetPayPwdResult;
import com.optimumnano.quickcharge.response.PayInvoiceBalanceResult;
import com.optimumnano.quickcharge.response.PayOrderInfoDepositResult;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.optimumnano.quickcharge.Constants.WX_APP_ID;
import static com.optimumnano.quickcharge.Constants.WX_PARTNER_ID;

/**
 * 支付中心界面
 */

public class PayCenterActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback, HttpCallback {
    /**
     * 订单金额
     */
    @Bind(R.id.paycenter_miMoney)
    MenuItem1 miMoney;
//    @Bind(R.id.paycenter_tvPayway)
//    TextView tvPayway;
    /**
     * 下一步
     */
    @Bind(R.id.paycenter_tvNext)
    TextView tvNext;
//    @Bind(R.id.paycenter_rlPayway)
//    RelativeLayout rlPayway;
    /**
     * 订单编号
     */
    @Bind(R.id.paycenter_miOrderno)
    MenuItem1 miOrderno;
    /**
     * 余额
     */
    @Bind(R.id.invoice_payway)
    MenuItem1 miPayway;

    private double money;
    private double allMoney = 0;

    private String formatRestCash;
    /**
     * 账户余额
     */
    private double restCash;

    /**
     * 支付方式弹窗
     */
    private PayDialog payDialog;

    private PayWayDialog payWayDialog;

    private int payWay;//支付方式

    private String order_no;

    private int mGetInvoiceSignTaskId;
    /**
     * 余额支付请求识别码
     */
    private int mPayInvoiceBalanceTaskId;
    /**
     * 请求密码服务识别ID
     */
    private int mGetPayPwdTaskId;
    /**
     * 请求账户信息识别ID
     */
    private int mGetAccountInfoTaskId;

    private int mGetWXPayOrderInfoDepositTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_center);
        ButterKnife.bind(this);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
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
//        rlPayway.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        payWay = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paycenter_tvNext:
                //邮费为0则直接成功，不用支付
                if (money > 0) {
                    if (payWay == PayDialog.pay_yue) {//余额
                        Constants.isInvoiceYue = true;
                        yue_pay();
                    } else if (payWay == PayDialog.pay_zfb) {//支付宝
                        payDialog.setMoney(money, order_no);
                        payDialog.payZFB();
                    } else if (payWay == PayDialog.pay_wx) {//微信
                        callWXPay();
                    }
                } else {
                    toInvoiceApply();
                }
                break;
            case R.id.invoice_payway:
                payWayDialog.show();
                break;
        }
    }

    private void yue_pay() {
        if (restCash < 12) {
            showToast("余额不足，请使用其他支付方式");
            return;
        }
        payDialog.setMoney(money, order_no);
        payDialog.setStatus(PayDialog.EDTPWD);
        payDialog.setPayway(payWay);
        payDialog.setPayResultMoney(money);
        payDialog.setPayName("支付金额");
        payDialog.show();
//        doPayPwdRequset();
    }


    /**
     * 微信支付
     */
    private void callWXPay() {

        if (!Tool.isConnectingToInternet()) {
            showToast("请求失败，无网络");
            return;
        }
        mGetWXPayOrderInfoDepositTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetWXPayOrderInfoDepositTaskId,
                new PayOrderInfoDepositRequest(new PayOrderInfoDepositResult(mContext), String.valueOf(money), payWay), this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
//        mTaskDispatcher.cancel(mGetInvoiceSignTaskId);
        mTaskDispatcher.cancel(mPayInvoiceBalanceTaskId);
        mTaskDispatcher.cancel(mGetPayPwdTaskId);
        mTaskDispatcher.cancel(mGetAccountInfoTaskId);
        mTaskDispatcher.cancel(mGetWXPayOrderInfoDepositTaskId);
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
        PayCenterActivity.this.finish();
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
            miPayway.setTvLeftText("余额(￥" + formatRestCash + ")");
        }
        if (mGetInvoiceSignTaskId == id) {
//            pay(((GetInvoiceSignResult) result).getResp().getResult().sign);


            if (payWay == PayDialog.pay_wx) {
                payDialog.payWeiXin(money, order_no);
            } else if (payWay == PayDialog.pay_zfb) {
                payDialog.setMoney(money, order_no);
                payDialog.payZFB();
            }
        }
        if (mGetWXPayOrderInfoDepositTaskId == id) {
            JSONObject dataJson = null;
            try {
                dataJson = new JSONObject(((PayOrderInfoDepositResult) result).getResp().getResult());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final IWXAPI wxApi = WXAPIFactory.createWXAPI(PayCenterActivity.this, WX_APP_ID);
            //将该app注册到微信
            wxApi.registerApp(WX_APP_ID);

            String sign = dataJson.optString("sign");
            WXPaySignBean wxpayBean = JSON.parseObject(sign.replace("\\", ""), WXPaySignBean.class);
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
                request.extData = payWay + "," + money+","+"a"+","+"b";
                wxApi.sendReq(request);
            } else {
                showToast("微信未安装或者版本过低");
            }
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvoiceWxPaySueecss(EventManager.onInvoiceWxPaySueecss event) {

        toInvoiceApply();
    }

}
