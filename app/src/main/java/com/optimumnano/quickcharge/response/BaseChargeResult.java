package com.optimumnano.quickcharge.response;


import android.content.Context;

import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by herry on 2017/5/14.
 */

public abstract class BaseChargeResult extends BaseResult {
    public BaseChargeResult(Context context) {
        super(context);
    }

    @Override
    protected boolean processStatus(int status) {
        if (status == 401) {
            EventBus.getDefault().post(new EventManager.cookieTimeOut());
            return true;
        }
        return super.processStatus(status);
    }
}
