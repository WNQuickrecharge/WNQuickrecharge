package com.optimumnano.quickcharge.dialog;

import android.app.Activity;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseDialog;
import com.optimumnano.quickcharge.views.MyWaveView;

/**
 * Created by ds on 2017/4/8 0008.
 */

public class WaitRechargeDialog extends BaseDialog{
    private MyWaveView waveView;
    public WaitRechargeDialog(Activity mAty) {
        super(mAty);
        waveView = dialog.getViewHolder().getView(R.id.dig_waveView);
        waveView.start();
    }

    @Override
    protected int getContentViewID() {
        return R.layout.dialog_waitrecharge;
    }
    public void cancelDialog(){
        waveView.stop();
        dialog.cancel();

    }
    public boolean isShowing(){
        if(dialog.isShowing()){
            return true;
        }else {
            return false;
        }
    }
}
