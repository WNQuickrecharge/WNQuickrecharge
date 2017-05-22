package com.optimumnano.quickcharge.activity.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.setting.ModifyPayPasswordActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.AlipayBean;
import com.optimumnano.quickcharge.bean.RechargeGunBean;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.request.AddOrderRequest;
import com.optimumnano.quickcharge.request.GetAccountInfoRequest;
import com.optimumnano.quickcharge.request.GetPayPwdRequest;
import com.optimumnano.quickcharge.response.AddOrderResult;
import com.optimumnano.quickcharge.response.GetAccountInfoResult;
import com.optimumnano.quickcharge.response.GetPayPwdResult;
import com.optimumnano.quickcharge.response.PayOrderInfoDepositResult;
import com.optimumnano.quickcharge.utils.PayWayViewHelp;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * 下单界面
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback, HttpCallback {
    private static final int SDK_PAY_FLAG = 001;
    private static final int ADDORDER_FLAG = 002;

    @Bind(R.id.order_tvConfirm)
    TextView tvConfirm;
    @Bind(R.id.order_payway)
    MenuItem1 miPayway;
    @Bind(R.id.order_miRechargeNum)
    MenuItem1 miRechargenum;
    @Bind(R.id.order_miType)
    MenuItem1 miType;
    @Bind(R.id.order_miElectric)
    MenuItem1 miElectric;
    @Bind(R.id.order_miPower)
    MenuItem1 miPower;
    @Bind(R.id.order_miSimPrice)
    MenuItem1 miSimprice;
    @Bind(R.id.order_miSimServicePrice)
    MenuItem1 miSimServicePrice;
    @Bind(R.id.order_edtMoney)
    EditText edtMoney;
    @Bind(R.id.order_tvAllkwh)
    TextView tvAllkwh;

    private PayDialog payDialog;
    private PayWayDialog payWayDialog;
    private OrderManager orderManager = new OrderManager();
    private RechargeGunBean gunBean;
    private String orderNo = "";//订单号
    private String gunNo = "";
    private String mAmount;

    private int payWay;//支付方式
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==ADDORDER_FLAG){
                tvConfirm.setEnabled(true);
            }
        }
    };
    private String formatRestCash;
    private double restCash;


    private int mGetAccountInfoTaskId;
    private int mGetPayPwdTaskId;
    private int mAddOrderTaskId;
    private int mCallAlipayTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        getExtras();
        initViews();
        initData();
        loadData();
        initDialog();
    }

    private void getExtras() {
        payWay = SharedPreferencesUtil.getValue(SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY, PayDialog.pay_yue);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gunBean = (RechargeGunBean) bundle.getSerializable("gunBean");
            gunNo = bundle.getString("gunNo");
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电订单");
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        tvConfirm.setOnClickListener(this);
        miPayway.setOnClickListener(this);
        edtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isEmpty(s.toString())) {
                    if (tvAllkwh != null) {
                        double price = Double.parseDouble(s.toString()) / (gunBean.price + gunBean.service_cost);

                        DecimalFormat df = new DecimalFormat("0.00");
                        String formatPrice = df.format(price);
                        tvAllkwh.setText(formatPrice + "kwh");
                    }
                } else {
                    if (tvAllkwh != null) {
                        tvAllkwh.setText("0.0kwh");
                    }
                }
            }
        });
    }

    private void initData() {
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }
        mGetAccountInfoTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetAccountInfoTaskId,
                new GetAccountInfoRequest(new GetAccountInfoResult(mContext)), this));

    }

    private void loadData() {
        miRechargenum.setRightText(gunNo);
        miType.setRightText(gunBean.pile_type);
        miElectric.setRightText(gunBean.elec_current + "A");
        miPower.setRightText(gunBean.power + "kw");
        miSimprice.setRightText(gunBean.price + "元/kwh");
        miSimServicePrice.setRightText(gunBean.service_cost + "元/kwh");
        double price = 0.0;
        if (TextUtils.isEmpty(edtMoney.getText().toString())) {
            price = 0.0;
        } else {
            price = Double.parseDouble(edtMoney.getText().toString()) / (gunBean.price + gunBean.service_cost);

        }

        DecimalFormat df = new DecimalFormat("0.00");
        String formatPrice = df.format(price);
        tvAllkwh.setText(formatPrice + "kwh");
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
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        ButterKnife.unbind(this);
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        mTaskDispatcher.cancel(mGetAccountInfoTaskId);
        mTaskDispatcher.cancel(mGetPayPwdTaskId);
        mTaskDispatcher.cancel(mAddOrderTaskId);
        mTaskDispatcher.cancel(mCallAlipayTaskId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_tvConfirm:
                addOrder();
                break;
            case R.id.order_payway:
                payWayDialog.show();
                break;

        }
    }

    //下单
    private void addOrder() {
        mAmount = edtMoney.getText().toString().trim();
        if (TextUtils.isEmpty(mAmount)|| Double.parseDouble(mAmount)==0){
            ToastUtil.showToast(OrderActivity.this,"支付金额错误");
            return;
        }
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }

        tvConfirm.setEnabled(false);
        mHandler.sendEmptyMessageDelayed(ADDORDER_FLAG,1500);
        showLoading();
        mAddOrderTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mAddOrderTaskId,
                new AddOrderRequest(new AddOrderResult(mContext), payWay, gunNo, mAmount), this));
    }

    @Override
    public void paySuccess(String orderNo) {
        Bundle bundle = new Bundle();
        bundle.putString("order_no", orderNo);
        bundle.putString("gun_no", gunNo);
        bundle.putInt("order_status", Constants.STARTCHARGE);
        skipActivity(RechargeControlActivity.class, bundle);
    }

    @Override
    public void payFail(String msg) {
        showToast(msg);
        payDialog.close();
    }

    private void callALiPay(Double mAmount,String orderNo) {
        payDialog.setMoney(mAmount,orderNo);
        payDialog.payZFB();

       /* if (!Tool.isConnectingToInternet()) {
            showToast("请求失败，无网络");
            return;
        }
        mCallAlipayTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mCallAlipayTaskId,
                new PayOrderInfoDepositRequest(new PayOrderInfoDepositResult(mContext),
                        String.valueOf(mAmount), PayDialog.pay_zfb), this));*/

    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetAccountInfoTaskId == id) {
            UserAccount userAccount = ((GetAccountInfoResult) result).getResp().getResult();
            restCash = userAccount.getRestCash();
            DecimalFormat df = new DecimalFormat("0.00");
            formatRestCash = df.format(restCash);
            if (payWay==PayDialog.pay_yue) {
                miPayway.setTvLeftText("余额" + "(" + formatRestCash + ")");
            }else if (payWay==PayDialog.pay_zfb){
                PayWayViewHelp.showPayWayStatus(miPayway,payWay,formatRestCash);
                miPayway.setTvLeftText("支付宝");
            }else if (payWay==PayDialog.pay_wx){
                PayWayViewHelp.showPayWayStatus(miPayway,payWay,formatRestCash);
                miPayway.setTvLeftText("微信");
            }
        } else if (mGetPayPwdTaskId == id) {
            String pwd = ((GetPayPwdResult) result).getResp().getResult().getPaypwd();
            if (pwd.equals("{}")) {
                showToast("支付密码为空，请设置");
                Intent intent = new Intent(OrderActivity.this, ModifyPayPasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("PayPasswordIsNUll", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } else if (mAddOrderTaskId == id) {
            closeLoading();
            Gson gson = new Gson();
            HashMap<String, Object> ha = gson.fromJson(((AddOrderResult) result).getResp().getResult(),
                    new TypeToken<HashMap<String, Object>>() {
                    }.getType());
            orderNo = ha.get("order_no").toString();
            String sign = ha.get("sign").toString();
            switch (payWay) {
                case PayDialog.pay_yue:
                    double v = Double.parseDouble(mAmount);
                    if (restCash < v) {
                        showToast("余额不足，请使用其他支付方式");
                        return;
                    }
                    payDialog.setMoney(Double.parseDouble(mAmount),orderNo,sign);
                    payDialog.setStatus(0);
                    payDialog.setPayway(payWay);
                    payDialog.setPayResultMoney(Double.parseDouble(mAmount));
                    payDialog.show();


                    if (!Tool.isConnectingToInternet()) {
                        showToast("无网络");
                        return;
                    }
                    mGetPayPwdTaskId = TaskIdGenFactory.gen();
                    mTaskDispatcher.dispatch(new HttpTask(mGetPayPwdTaskId,
                            new GetPayPwdRequest(new GetPayPwdResult(mContext)), OrderActivity.this));

                    break;

                case PayDialog.pay_zfb:
                    double finalMoney = Double.parseDouble(mAmount);
                    callALiPay(finalMoney,orderNo);
                    break;
                case PayDialog.pay_wx:
                    payDialog.payWeiXin(Double.parseDouble(mAmount),orderNo);
                    break;

                default:
                    break;
            }

        } else if (mCallAlipayTaskId == id) {
            final String orderInfo = ((PayOrderInfoDepositResult) result).getResp().getResult();// 签名后的订单信息
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(OrderActivity.this);
                    AlipayBean alipayBean = JSON.parseObject(orderInfo, AlipayBean.class);
                    String sign = alipayBean.getSign();
                    LogUtil.i("sign==" + sign);
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
    }


    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetAccountInfoTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((GetAccountInfoResult) result).getResp()));
        } else if (mAddOrderTaskId == id) {
            closeLoading();
            showToast(ToastUtil.formatToastText(mContext, ((AddOrderResult) result).getResp()));
        } else if (mCallAlipayTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((PayOrderInfoDepositResult) result).getResp()));
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weiXinPayCallback(EventManager.WeiXinPayCallback event) {
        int code = event.code;
        if (0 == code){
            finish();
        }else {
        }
        logtesti("orderdetail weixinpay callback "+event.code);
    }
}
