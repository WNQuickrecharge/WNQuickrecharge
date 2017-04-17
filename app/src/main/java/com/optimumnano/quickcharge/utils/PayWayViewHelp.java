package com.optimumnano.quickcharge.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;

/**
 * 作者：邓传亮 on 2017/4/15 16:00
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class PayWayViewHelp {
    public static void showPayWayStatus(BaseActivity activity, TextView textView,int payway) {
        Drawable drawable=null;
        switch (payway){
            case PayDialog.pay_wx:
                drawable= activity.getResources().getDrawable(R.drawable.wx);
                textView.setText("微信");
                break;
            case PayDialog.pay_zfb:
                drawable= activity.getResources().getDrawable(R.drawable.zfb);
                textView.setText("支付宝");
                break;
            case PayDialog.pay_yue:
                drawable= activity.getResources().getDrawable(R.drawable.yue);
                textView.setText("余额");
                break;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable,null,null,null);
    }
}
