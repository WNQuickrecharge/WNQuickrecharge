package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.setting.ModifyPayPasswordActivity;
import com.optimumnano.quickcharge.base.BaseDialog;
import com.optimumnano.quickcharge.bean.WXPaySignBean;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.optimumnano.quickcharge.views.PasswordView;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.util.Map;

import static com.optimumnano.quickcharge.Constants.WX_APP_ID;
import static com.optimumnano.quickcharge.Constants.WX_PARTNER_ID;

/**
 * Created by ds on 2017/4/9.
 * 支付弹框
 */

public class PayDialog extends BaseDialog implements View.OnClickListener {
    private PasswordView passwordView;
    private MenuItem1 menuItem1;

    public static final int EDTPWD = 0;
    public static final int PAYWAY = 1;
    public static final int PAYSUCCESS = 2;
    public static final int PAYFAIL = 3;
    public static final int PAYBT = 4;

    public static final int pay_wx = 1;
    public static final int pay_zfb = 0;
    public static final int pay_yue = 3;
    private Activity activity;
    private TextView payName;
    private final TextView mPayTitle;
    private OrderManager orderManager = new OrderManager();
    private PayCallback payCallback;
    private double money;
    private String order_no;
    private int payWay=3;//支付方式
    private String sign;//支付宝支付的签名
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            close();
            if (msg.what==1001){
                payCallback.paySuccess(order_no);
            }
            if (msg.what==1000){
                    Map mapresult =(Map) msg.obj;
                    JSONObject dataJson = new JSONObject(mapresult);
                    String resultStatus = dataJson.optString("resultStatus");// 结果码
                    switch (resultStatus){
                        case "9000"://支付成功
                        case "8000"://正在处理,支付结果确认中
                        case "6004"://支付结果未知
//                            "支付成功"
                            payCallback.paySuccess(order_no);
                            break;
                        case "4000":
                            payCallback.payFail("订单支付失败");
                        case "5000":
                            payCallback.payFail("订单不能重复支付");
                            break;
                        case "6001":
                            payCallback.payFail("取消支付");
                            break;
                        case "6002":
                            payCallback.payFail("网络连接出错");
                            break;
                        default:
                            payCallback.payFail("支付异常");
                            break;
                    }
            }
        }
    };
    public PayDialog(Activity mAty) {
        super(mAty);
        activity = mAty;
        dialog.getViewHolder().getView(R.id.pay_ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        passwordView = dialog.getViewHolder().getView(R.id.pay_passwordView);
        passwordView.requestFocus();
        menuItem1 = dialog.getViewHolder().getView(R.id.pay_payWay);

        dialog.getViewHolder().getView(R.id.dialog_chose_payment_qx).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_qx1).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_wx).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_zfb).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_ye).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.pay_tvReInput).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.pay_tvUpdatePwd).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.pay_payWay).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.bt_pay).setOnClickListener(this);
        payName = dialog.getViewHolder().getView(R.id.pay_name);
        mPayTitle = dialog.getViewHolder().getView(R.id.pay_title);
        setTextChangedListener();
    }

    public void setPayCallback(PayCallback payCallback) {
        this.payCallback = payCallback;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.dialog_pay;
    }

    /**
     * 密码控件变化监听
     */
    public void setTextChangedListener(TextWatcher textWatcher){
        passwordView.addTextChangedListener(textWatcher);
    }
    public void setTextChangedListener(){
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() == 6) {
                    //BaseActivity.showBaseLoading();
                    GetMineInfoManager.getPayPwd(new ManagerCallback() {
                        @Override
                        public void onSuccess(Object returnContent) {
                            super.onSuccess(returnContent);
                            //BaseActivity.hideBaseLoading();
                            JSONObject dataJson = null;
                            try {
                                dataJson = new JSONObject(returnContent.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String paypwd = dataJson.optString("paypwd");
                            String Md5Paypassword = MD5Utils.encodeMD5(s.toString());
                            String finalPayPassword= MD5Utils.encodeMD5(Md5Paypassword);

                            if (finalPayPassword.equals(paypwd)) {
                                if (payWay == pay_yue){
                                    payYue();
                                }
                                else if (payWay == pay_zfb){
                                    payZFB();
                                }
                                else {
//                                    payCallback.payFail("微信支付开发中");
//                                    setStatus(PayDialog.EDTPWD);
                                    payWeiXin(money,order_no);
                                }

                            } else {
                                setStatus(PayDialog.PAYFAIL);
                            }
                        }

                        @Override
                        public void onFailure(String msg) {
                            super.onFailure(msg);
                            //BaseActivity.hideBaseLoading();
                            ToastUtil.showToast(activity,msg);
                        }
                    });
//                    String payPwd = SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_PAYPASSWORD,"");

                }
            }
        });
    }
    //余额支付
    private void payYue() {
        orderManager.startPay(order_no, money, new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                setStatus(PAYSUCCESS);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            handler.sendEmptyMessage(1001);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                close();
                ToastUtil.showToast(activity,msg);
                payCallback.payFail(msg);
            }
        });
    }
    public void payZFB(){
        if (StringUtils.isEmpty(sign)){
            orderManager.getSign(order_no, PayDialog.pay_zfb, new ManagerCallback<String>() {
                @Override
                public void onSuccess(String returnContent) {
                    super.onSuccess(returnContent);
                    JSONObject jsonObject=null;
                    try {
                        jsonObject=new JSONObject(returnContent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sign = jsonObject.optString("sign");
                    startPay();
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                }
            });
        }
        else {
            startPay();
        }

    }

    public void payWeiXin(final double amount, final String orderNum) {
        if (amount == 0 || TextUtils.isEmpty(orderNum)){
            ToastUtil.showToast(activity,"支付参数错误");
            return;
        }
        orderManager.getSign(orderNum, PayDialog.pay_wx, new ManagerCallback<String>() {
            @Override
            public void onSuccess(String returnContent) {
                super.onSuccess(returnContent);
                LogUtil.i("returnContent "+returnContent.toString());
                JSONObject dataJson=null;
                try {
                    dataJson = new JSONObject(returnContent.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final IWXAPI wxApi = WXAPIFactory.createWXAPI(activity, WX_APP_ID);
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
                    request.extData = PayDialog.pay_wx+","+amount +","+orderNum;
                    wxApi.sendReq(request);
                }else {
                    ToastUtil.showToast(activity,"微信未安装或者版本过低");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtil.showToast(activity,"请求失败 "+msg);
                super.onFailure(msg);
            }

        });
    }

    private void startPay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(sign, true);//true表示唤起loading等待界面

                Message msg = new Message();
                msg.what = 1000;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 选择支付方式
     * @param I
     */
    public void setPaywayListener(View.OnClickListener I){
        menuItem1.setOnClickListener(I);
    }

    /**
     * 设置付款金额
     * @param money 金额
     */
    public void setMoney(double money){
        setMoney(money,"");
    }
    public void setMoney(double money,String order_no){
        setMoney(money,order_no,"");
    }

    public void setMoney(double money,String order_no,String sign){
        setMoney(money,order_no,sign, SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_DEFPAYWAY,PayDialog.pay_yue));
    }
    public void setMoney(double money,String order_no,String sign,int paytype){
        this.money = money;
        this.order_no  = order_no;
        this.sign = sign;
        dialog.getViewHolder().setText(R.id.pay_tvMoney,"¥"+money);
        setPayway(paytype);
    }
    public void setPayResultMoney(double money){
        this.money = money;
        dialog.getViewHolder().setText(R.id.payresult_tvMoney,"¥"+money);
    }

    public void setPayway(int payway){
        this.payWay = payway;
        switch (payway){
            //微信
            case pay_wx:
                menuItem1.setIvLeftDrawable(R.drawable.wx);
                menuItem1.setTvLeftText("微信");
                break;
            //支付寶
            case pay_zfb:
                menuItem1.setIvLeftDrawable(R.drawable.zfb);
                menuItem1.setTvLeftText("支付宝");
                break;
            //余額
            case pay_yue:
                menuItem1.setIvLeftDrawable(R.drawable.yue);
                menuItem1.setTvLeftText("余额");
                break;
        }
    }

    public void setPaywayAndBalance(int payway,double balance){
        String paywayFomat= activity.getResources().getString(R.string.dialog_pay_way_balance);
        switch (payway){
            //微信
            case 0:
                menuItem1.setIvLeftDrawable(R.drawable.wx);
                menuItem1.setTvLeftText("微信");
                break;
            //支付寶
            case 1:
                menuItem1.setIvLeftDrawable(R.drawable.zfb);
                menuItem1.setTvLeftText("支付宝");
                break;
            //余額
            case 2:
                menuItem1.setIvLeftDrawable(R.drawable.yue);
                String sFinal = String.format(paywayFomat,"余额",balance);
                menuItem1.setTvLeftText(sFinal);
                break;
        }
    }

    /**
     * 设置弹框显示
     * 0输入密码
     * 1支付成功
     * 2支付失败
     */
    public void setStatus(int status){
        if (status == EDTPWD){
            passwordView.setVisibility(View.VISIBLE);
            passwordView.setText("");
            mPayTitle.setText("请输入支付密码");
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,true);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,false);
            dialog.getViewHolder().setVisible(R.id.bt_pay,false);
        }
        else if (status == PAYSUCCESS){
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,true);
            dialog.getViewHolder().setText(R.id.payresult_tvMoney,"");
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,false);
            dialog.getViewHolder().setVisible(R.id.bt_pay,false);
        }
        else if (status == PAYFAIL){
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,true);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,false);
            dialog.getViewHolder().setVisible(R.id.bt_pay,false);
        }
        else if (status == PAYBT){
            mPayTitle.setText("支付订单");
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,true);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,false);
            dialog.getViewHolder().setVisible(R.id.bt_pay,true);
            passwordView.setVisibility(View.GONE);
        }
        else {
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,true);
            dialog.getViewHolder().setVisible(R.id.bt_pay,false);
        }
    }

    @Override
    public void show() {
        super.show();
        passwordView.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_chose_payment_qx:
                setStatus(EDTPWD);
                break;
            case R.id.dialog_chose_payment_qx1:
                setStatus(EDTPWD);
                break;
            //微信支付
            case R.id.dialog_chose_payment_wx:
                setPayway(pay_wx);
                setStatus(PAYBT);
                break;
            //支付宝支付
            case R.id.dialog_chose_payment_zfb:
                setPayway(pay_zfb);
                setStatus(PAYBT);
                break;
            //余额支付
            case R.id.dialog_chose_payment_ye:
                setPayway(pay_yue);
                setStatus(EDTPWD);
                break;
            //支付彈框 修改支付方式
            case R.id.pay_payWay:
                setStatus(PAYWAY);
                break;
            //重新输入
            case R.id.pay_tvReInput:
                setStatus(EDTPWD);
                break;
            //修改密码
            case R.id.pay_tvUpdatePwd:
                //跳转界面
                activity.startActivity(new Intent(activity, ModifyPayPasswordActivity.class));
                break;
            case R.id.bt_pay:
                payWxOrAlipay();
                break;
        }
    }

    private void payWxOrAlipay() {
        close();
        if (PayDialog.pay_wx == payWay){
            payWeiXin(money,order_no);
        }else if (PayDialog.pay_zfb == payWay){
            payZFB();
        }
    }

    public void cleanPasswordView(){
        passwordView.setText("");
    }

    public void setPayName(String payname){
        payName.setText(payname);
    }

    public interface PayCallback{
        void paySuccess(String oder_no);
        void payFail(String msg);
    }
}
