package com.optimumnano.quickcharge.response;

import android.content.Context;

import okhttp3.Response;

/**
 * Created by mfwn on 2017/5/19.
 */

public class GetAskChargeResult extends BaseChargeResult {
    public GetAskChargeResult(Context context) {
        super(context);
    }

    @Override
    protected int parseResponse(Response response) throws Exception {
        return 0;
    }
}
