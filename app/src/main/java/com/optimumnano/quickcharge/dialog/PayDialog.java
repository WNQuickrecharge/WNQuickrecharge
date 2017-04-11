package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseDialog;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.optimumnano.quickcharge.views.PasswordView;

import static com.optimumnano.quickcharge.R.id.pay_tvMoney;

/**
 * Created by PC on 2017/4/9.
 * 支付弹框
 */

public class PayDialog extends BaseDialog {
    private final Activity mActivity;
    private PasswordView passwordView;
    private MenuItem1 menuItem1;
    private TextView payName;
    public PayDialog(Activity mAty) {
        super(mAty);
        this.mActivity=mAty;
        dialog.getViewHolder().getView(R.id.pay_ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        passwordView = dialog.getViewHolder().getView(R.id.pay_passwordView);
        passwordView.requestFocus();
        menuItem1 = dialog.getViewHolder().getView(R.id.pay_payWay);
        payName = dialog.getViewHolder().getView(R.id.pay_name);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.dialog_pay;
    }

    /**
     * 密码控件变化监听
     * @param watcher 监听器
     */
    public void setTextChangedListener(TextWatcher watcher){
        passwordView.addTextChangedListener(watcher);
    }

/*    *//**
     * 选择支付方式
     * @param I
     *//*
    public void setPaywayListener(View.OnClickListener I){
        menuItem1.setOnClickListener(I);
        dialog.getViewHolder().getView(R.id.pay_tvUpdatePwd).setOnClickListener(I);
        dialog.getViewHolder().getView(R.id.pay_tvReInput).setOnClickListener(I);
    }*/

    /**
     * 选择支付方式
     * @param I
     */
    public void setPaywayListener(View.OnClickListener I){
        menuItem1.setOnClickListener(I);
    }

    /**
     * 重置支付密码按钮监听
     * @param I
     */
    public void setUpdatePwdBtListener(View.OnClickListener I){
        dialog.getViewHolder().getView(R.id.pay_tvUpdatePwd).setOnClickListener(I);
    }

    /**
     * 重新输入密码按钮监听
     * @param I
     */
    public void setReInputBtListener(View.OnClickListener I){
        dialog.getViewHolder().getView(R.id.pay_tvReInput).setOnClickListener(I);
    }

    /**
     * 设置付款金额
     * @param money 金额
     */
    public void setMoney(double money){
        dialog.getViewHolder().setText(pay_tvMoney,"¥"+money);
    }

    public void setPayway(int payway){
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
                menuItem1.setTvLeftText("余额");
                break;
        }
    }

    public void setPaywayAndBalance(int payway,double balance){
        String paywayFomat= mActivity.getResources().getString(R.string.dialog_pay_way_balance);
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
        if (status == 0){
            passwordView.setText("");
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,true);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
        }
        else if (status == 1){
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,true);
        }
        else {
            dialog.getViewHolder().setVisible(R.id.pay_llEdt,false);
            dialog.getViewHolder().setVisible(R.id.payresult_llFail,true);
            dialog.getViewHolder().setVisible(R.id.payresult_llSuccess,false);
        }
    }

    @Override
    public void show() {
        super.show();
        passwordView.setText("");
    }

    public void cleanPasswordView(){
        passwordView.setText("");
    }

    public void setPayName(String payname){
        payName.setText(payname);
    }

}
