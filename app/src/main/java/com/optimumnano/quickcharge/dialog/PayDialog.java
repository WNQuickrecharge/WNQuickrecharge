package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.setting.ModifyPayPasswordActivity;
import com.optimumnano.quickcharge.base.BaseDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.optimumnano.quickcharge.views.PasswordView;

import org.json.JSONObject;

import java.util.Map;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_PAYPASSWORD;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

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

    public static final int pay_wx = 1;
    public static final int pay_zfb = 0;
    public static final int pay_yue = 3;
    private Activity activity;
    private TextView payName;
    private OrderManager orderManager = new OrderManager();
    private PayCallback payCallback;
    private double money;
    private String order_no;
    private int payWay;//支付方式
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
        payName = dialog.getViewHolder().getView(R.id.pay_name);
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
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    String payPwd = SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_PAYPASSWORD,"");
                    String Md5Paypassword = MD5Utils.encodeMD5(s.toString());
                    String finalPayPassword= MD5Utils.encodeMD5(Md5Paypassword);
                    if (finalPayPassword.equals(payPwd)) {
                        if (payWay == pay_yue){
                            payYue();
                        }
                        else if (payWay == pay_zfb){
                            payZFB();
                        }
                        else {
                            payCallback.payFail("微信支付开发中");
                            setStatus(PayDialog.EDTPWD);
                        }

                    } else {
                        setStatus(PayDialog.PAYFAIL);
                    }
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
                payCallback.payFail(msg);
            }
        });
    }
    private void payZFB(){
        if (StringUtils.isEmpty(sign)){
            orderManager.getSign(order_no, payWay, new ManagerCallback<String>() {
                @Override
                public void onSuccess(String returnContent) {
                    super.onSuccess(returnContent);
                    sign = returnContent;
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
        this.money = money;
        dialog.getViewHolder().setText(R.id.pay_tvMoney,"¥"+money);
    }
    public void setMoney(double money,String order_no){
        this.money = money;
        this.order_no  = order_no;
        dialog.getViewHolder().setText(R.id.pay_tvMoney,"¥"+money);
    }

    public void setMoney(double money,String order_no,String sign){
        this.money = money;
        this.order_no  = order_no;
        this.sign = sign;
        dialog.getViewHolder().setText(R.id.pay_tvMoney,"¥"+money);
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
            passwordView.setText("");
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,true);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,false);
        }
        else if (status == PAYSUCCESS){
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,true);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,false);
        }
        else if (status == PAYFAIL){
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,true);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,false);
        }
        else {
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
            dialog.getViewHolder().setVisible(R.id.pay_llPayway,true);
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
                setStatus(EDTPWD);
                break;
            //支付宝支付
            case R.id.dialog_chose_payment_zfb:
                setPayway(pay_zfb);
                setStatus(EDTPWD);
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
