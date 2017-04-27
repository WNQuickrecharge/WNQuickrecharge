package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.AlipayBean;
import com.optimumnano.quickcharge.bean.RechargeGunBean;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.dialog.PayWayDialog;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 下单界面
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback {
    private static final int SDK_PAY_FLAG = 001;

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

    private int payWay = PayDialog.pay_yue;//支付方式
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
                        paySuccess(orderNo);
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
    private String formatRestCash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        getExtras();
        initViews();
        initData();
        initDialog();
    }
    private void getExtras(){
        gunNo = getIntent().getExtras().getString("gun_no");
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电订单");

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
                if (!StringUtils.isEmpty(s.toString())){
                    if (tvAllkwh!=null) {
                        double price = Double.parseDouble(s.toString()) / gunBean.price;

                        DecimalFormat df = new DecimalFormat("0.00");
                        String formatPrice = df.format(price);
                        tvAllkwh.setText(formatPrice+"kwh");
                    }
                }
                else {
                    if (tvAllkwh!=null) {
                        tvAllkwh.setText("0.0kwh");
                    }
                }
            }
        });
    }
    private void initData(){
        showLoading("获取枪状态中请稍等！");
        orderManager.getGunInfo(gunNo, new ManagerCallback<RechargeGunBean>() {
            @Override
            public void onSuccess(RechargeGunBean returnContent) {
                super.onSuccess(returnContent);
                gunBean = returnContent;
                loadData();
                closeLoading();
            }
            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                closeLoading();
                showToast("获取失败，请检查终端号是否正确");
                finish();
            }
        });
        GetMineInfoManager.getAccountInfo(new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                String s = returnContent.toString();
                UserAccount userAccount = JSON.parseObject(s, UserAccount.class);
                double restCash = userAccount.getRestCash();
                DecimalFormat df = new DecimalFormat("0.00");
                formatRestCash = df.format(restCash);
                miPayway.setTvLeftText("余额"+"("+ formatRestCash +")");
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg);
            }
        });
    }
    private void loadData(){
        miRechargenum.setRightText(gunNo);
        miType.setRightText(gunBean.pile_type);
        miElectric.setRightText(gunBean.elec_current+"A");
        miPower.setRightText(gunBean.power+"kwh");
        miSimprice.setRightText(gunBean.price+"元/kwh");
        miSimServicePrice.setRightText(gunBean.service_cost+"元/kwh");
    }
    private void initDialog(){
        payDialog = new PayDialog(this);
        payDialog.setPayCallback(this);
        payWayDialog = new PayWayDialog(this);
        payDialog.setPaywayListener(this);
        payWayDialog.setViewClickListener(new PayWayDialog.PayWayDialogClick() {
            @Override
            public void onMenuClick(int payway) {
                switch (payway){
                    //微信
                    case PayDialog.pay_wx:
                        miPayway.setIvLeftDrawable(R.drawable.wx);
                        miPayway.setTvLeftText("微信");
                        payWay = PayDialog.pay_wx;
                        break;
                    //支付寶
                    case PayDialog.pay_zfb:
                        miPayway.setIvLeftDrawable(R.drawable.zfb);
                        miPayway.setTvLeftText("支付宝");
                        payWay = PayDialog.pay_zfb;
                        break;
                    //余額
                    case PayDialog.pay_yue:
                        miPayway.setIvLeftDrawable(R.drawable.yue);
                        miPayway.setTvLeftText("余额"+"("+formatRestCash+")");
                        payWay = PayDialog.pay_yue;
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_tvConfirm:
                addOrder();
                break;
            case R.id.order_payway:
                payWayDialog.show();
                break;

        }
    }
    //下单
    private void addOrder(){
        orderManager.addOrder(gunNo, edtMoney.getText().toString(),payWay, new ManagerCallback<String>() {
            @Override
            public void onSuccess(String returnContent) {
                super.onSuccess(returnContent);
                Gson gson = new Gson();
                HashMap<String,Object> ha = gson.fromJson(returnContent,new TypeToken<HashMap<String,Object>>(){}.getType());
                orderNo = ha.get("order_no").toString();
                String sign = ha.get("sign").toString();
                switch (payWay) {
                    case PayDialog.pay_yue:
                        payDialog.setMoney(Double.parseDouble(edtMoney.getText().toString()),orderNo,sign);
                        payDialog.setStatus(0);
                        payDialog.setPayway(payWay);
                        payDialog.setPayResultMoney(Double.parseDouble(edtMoney.getText().toString()));
                        payDialog.show();
                        break;

                    case PayDialog.pay_zfb:
                        String money = edtMoney.getText().toString();
                        double finalMoney = Double.parseDouble(money);
                        mAmount=money;
                        callALiPay(finalMoney);
                        break;
                    case PayDialog.pay_wx:

                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showToast(msg+"");
            }
        });
    }

    @Override
    public void paySuccess(String orderNo) {
        Bundle bundle = new Bundle();
        bundle.putString("order_no",orderNo);
        bundle.putString("gun_no",gunNo);
        bundle.putInt("order_status", Constants.STARTCHARGE);
        skipActivity(RechargeControlActivity.class,bundle);
    }

    @Override
    public void payFail(String msg) {
        showToast(msg);
        payDialog.close();
    }

    private void callALiPay(Double mAmount) {

        GetMineInfoManager.getALiPayOrderInfoDeposit(mAmount+"",PayDialog.pay_zfb, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                logtesti("returnContent "+returnContent.toString());

                final String orderInfo = returnContent.toString();// 签名后的订单信息
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(OrderActivity.this);
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
                showToast(msg);
                super.onFailure(msg);
            }

        });

    }
}
