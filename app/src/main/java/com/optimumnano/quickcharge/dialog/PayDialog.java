package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.Constants;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.order.RechargeControlActivity;
import com.optimumnano.quickcharge.activity.setting.ModifyPayPasswordActivity;
import com.optimumnano.quickcharge.base.BaseDialog;
import com.optimumnano.quickcharge.manager.OrderManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.optimumnano.quickcharge.views.PasswordView;

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

    public static final int pay_wx = 0;
    public static final int pay_zfb = 1;
    public static final int pay_yue = 2;
    private Activity activity;
    private TextView payName;
    private OrderManager orderManager = new OrderManager();
    private PayCallback payCallback;
    private double money;
    private String order_no;
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
                    if (s.toString().equals("123456")) {
                        orderManager.startPay(order_no, money, new ManagerCallback() {
                            @Override
                            public void onSuccess(Object returnContent) {
                                super.onSuccess(returnContent);
                                close();
//                                payCallback.paySuccess();
                                payCallback.paySuccess(order_no);
                            }

                            @Override
                            public void onFailure(String msg) {
                                super.onFailure(msg);
                                payCallback.payFail();
                            }
                        });
                    } else {
                        setStatus(PayDialog.PAYFAIL);
                    }
                }
            }
        });
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

    public void setPayway(int payway){
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
        void payFail();
    }
}
