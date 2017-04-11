package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseDialog;

/**
 * Created by PC on 2017/4/9.
 */

public class PayWayDialog extends BaseDialog {
    public PayWayDialog(Activity mAty) {
        super(mAty);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.dialog_chose_payway_tv;
    }

    /**
     * 彈框點擊監聽
     * @param I
     */
    public void setViewClickListener(View.OnClickListener I){
        dialog.getViewHolder().getView(R.id.dialog_chose_payway_qx).setOnClickListener(I);
        dialog.getViewHolder().getView(R.id.dialog_chose_payway_wx).setOnClickListener(I);
        dialog.getViewHolder().getView(R.id.dialog_chose_payway_zfb).setOnClickListener(I);
        dialog.getViewHolder().getView(R.id.dialog_chose_payway_ye).setOnClickListener(I);
    }
}
