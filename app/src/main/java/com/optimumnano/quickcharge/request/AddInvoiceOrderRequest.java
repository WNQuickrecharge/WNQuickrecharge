package com.optimumnano.quickcharge.request;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.InternalConstants;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.utils.StringUtils;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by herry on 2017/5/17.
 */

public class AddInvoiceOrderRequest extends BaseChargeRequest {

    private String consume_ids;
    private String title;
    private double invoice_amount;
    private String name;
    private String address;
    private String mobile;
    private String regPhone;
    private String regAddress;
    private String bankCard;
    private String indentifyNum;
    private String remark;

    public AddInvoiceOrderRequest(BaseResult result, String consume_ids, String title, double invoice_amount,
                                  String name, String address, String mobile, String regPhone, String regAddress,
                                  String bankCard, String indentifyNum, String remark) {
        super(result);
        this.consume_ids = consume_ids;
        this.title = title;
        this.invoice_amount = invoice_amount;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.regPhone = regPhone;
        this.regAddress = regAddress;
        this.bankCard = bankCard;
        this.indentifyNum = indentifyNum;
        this.remark = remark;
    }

    @Override
    protected String getUrl() {
        return HttpApi.getInstance().getUrl(HttpApi.add_invoice);
    }

    @Override
    protected ArrayMap<String, String> getHeaders() {
        return null;
    }

    @Override
    protected ArrayMap<String, String> getParams() {
        return null;
    }

    @Override
    protected RequestBody getRequestBody() {
        HashMap<String, Object> ha = new HashMap<>();
        if (!StringUtils.isEmpty(regPhone)) {
            ha.put("tax_no", indentifyNum);
            ha.put("register_addr", regAddress);
            ha.put("register_phone", regPhone);
            ha.put("bank_num", bankCard);
            ha.put("remark", remark);
        }
        ha.put("consume_ids", consume_ids);
        ha.put("title", title);
        ha.put("invoice_amount", invoice_amount);
        ha.put("name", name);
        ha.put("address", address);
        ha.put("mobile", mobile);
        String json = JSON.toJSONString(ha);
        return RequestBody.create(MediaType.parse(InternalConstants.MEDIA_TYPE), json);
    }
}
