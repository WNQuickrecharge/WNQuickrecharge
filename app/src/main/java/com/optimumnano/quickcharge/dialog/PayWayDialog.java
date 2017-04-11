package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseDialog;

/**
 * Created by ds on 2017/4/9.
 */

public class PayWayDialog extends BaseDialog implements View.OnClickListener {
    public PayWayDialog(Activity mAty) {
        super(mAty);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_qx).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_wx).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_zfb).setOnClickListener(this);
        dialog.getViewHolder().getView(R.id.dialog_chose_payment_ye).setOnClickListener(this);
    }
    private PayWayDialogClick payWayDialogClick;

    @Override
    protected int getContentViewID() {
        return R.layout.dialog_chose_payway_tv;
    }

    /**
     * 彈框點擊監聽
     */
    public void setViewClickListener(PayWayDialogClick payWayDialogClick){
        this.payWayDialogClick = payWayDialogClick;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_chose_payway_qx1:
                break;
            //微信支付
            case R.id.dialog_chose_payway_wx:
                payWayDialogClick.onMenuClick(PayDialog.pay_wx);
                break;
            //支付宝支付
            case R.id.dialog_chose_payway_zfb:
                payWayDialogClick.onMenuClick(PayDialog.pay_zfb);
                break;
            //余额支付
            case R.id.dialog_chose_payway_ye:
                payWayDialogClick.onMenuClick(PayDialog.pay_yue);
                break;
        }
        close();
    }

    public interface PayWayDialogClick{
        void onMenuClick(int payway);
    }
}
