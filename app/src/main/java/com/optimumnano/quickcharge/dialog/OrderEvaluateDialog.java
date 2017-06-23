package com.optimumnano.quickcharge.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by zhangjiancheng on 2017/6/22.
 */

public class OrderEvaluateDialog extends Dialog {
    public OrderEvaluateDialog(Context context) {
        super(context);
    }

    public OrderEvaluateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected OrderEvaluateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
