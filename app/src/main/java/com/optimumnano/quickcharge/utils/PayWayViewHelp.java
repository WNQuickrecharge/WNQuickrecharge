package com.optimumnano.quickcharge.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.views.MenuItem1;

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
    public static void showPayWayStatus(MenuItem1 textView, int payway,String formatRestCash) {
        int drawable=0;
        switch (payway){
            case PayDialog.pay_wx:
                drawable= R.drawable.wx;
                textView.setTvLeftText("微信");
                break;
            case PayDialog.pay_zfb:
                drawable= R.drawable.zfb;
                textView.setTvLeftText("支付宝");
                break;
            case PayDialog.pay_yue:
                drawable= R.drawable.yue;
                textView.setTvLeftText("余额"+"("+formatRestCash+")");
                break;
        }
        textView.setIvLeftDrawable(drawable);
    }
}
