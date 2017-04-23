package com.optimumnano.quickcharge.manager;

import com.optimumnano.quickcharge.bean.InvoiceOrder;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.net.HttpCallback;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.net.MyHttpUtils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.xutils.http.RequestParams;

import java.util.List;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;

/**
 * Created by ds on 2017/4/22.
 */

public class InvoiceManager {
    /**
     * 获取没有开发票的交易记录
     */
    public void getInvoiceRecord(final ManagerCallback callback){
        String url = HttpApi.getInstance().getUrl(HttpApi.get_invoice_consume);
        RequestParams params = new RequestParams(url);
        params.setHeader("Cookie", SharedPreferencesUtil.getValue(SP_COOKIE,KEY_USERINFO_COOKIE,""));
        MyHttpUtils.getInstance().post(params, new HttpCallback<List<InvoiceOrder>>() {
            @Override
            public void onSuccess(List<InvoiceOrder> result, int httpCode) {
                super.onSuccess(result, httpCode);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(String msg, String errorCode, int httpCode) {
                super.onFailure(msg, errorCode, httpCode);
            }
        });
    }
}
