package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.text.TextWatcher;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseDialog;
import com.optimumnano.quickcharge.views.MenuItem1;
import com.optimumnano.quickcharge.views.PasswordView;

/**
 * Created by PC on 2017/4/9.
 * 支付弹框
 */

public class PayDialog extends BaseDialog {
    private PasswordView passwordView;
    private MenuItem1 menuItem1;
    public PayDialog(Activity mAty) {
        super(mAty);
        dialog.getViewHolder().getView(R.id.pay_ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        passwordView = dialog.getViewHolder().getView(R.id.pay_passwordView);
        passwordView.requestFocus();
        menuItem1 = dialog.getViewHolder().getView(R.id.pay_payWay);
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
        dialog.getViewHolder().setText(R.id.pay_tvMoney,money+"");
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

    @Override
    public void show() {
        super.show();
        passwordView.setText("");
    }
}
