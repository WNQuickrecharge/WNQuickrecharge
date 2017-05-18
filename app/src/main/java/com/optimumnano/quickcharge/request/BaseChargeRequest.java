package com.optimumnano.quickcharge.request;

import com.optimumnano.quickcharge.http.BaseRequest;
import com.optimumnano.quickcharge.http.BaseResult;

/**
 * Created by herry on 2017/5/14.
 */

public abstract class BaseChargeRequest extends BaseRequest {
    public BaseChargeRequest(BaseResult result) {
        super(result);
    }

    public BaseChargeRequest(BaseResult result, boolean needCookie) {
        super(result, needCookie);
    }


}
