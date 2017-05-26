package com.optimumnano.quickcharge.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.BaseHttpResp;
import com.optimumnano.quickcharge.response.BaseChargeResult;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context ctx, @StringRes int resId) {
        showToast(ctx, ctx.getString(resId));
    }

    public static void showToast(Context ctx, String msg) {
        showToast(ctx, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context ctx, String msg, int duration) {
        if (toast == null) {
            toast = Toast.makeText(ctx, msg, duration);
        } else {
            toast.setText(msg);
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static String formatToastText(Context context, BaseHttpResp resp) {
        if (resp==null){
            return "网络连接异常";
        }
        if (TextUtils.isEmpty(resp.getResultMsg())) {
            return "网络连接异常";//TODO
        }
        return resp.getResultMsg();
    }

    public static String formatToastText(Context context, BaseChargeResult result, BaseHttpResp resp) {
        if (result.getStatus() == 401) {
            return context.getString(R.string.cookie_timeout);
        }
        if (resp==null){
            return "网络连接异常";
        }
        if (TextUtils.isEmpty(resp.getResultMsg())) {
            return "网络连接异常";//TODO
        }
        return resp.getResultMsg();
    }
}
