package com.optimumnano.quickcharge.bean;

/**
 * Created by mfwn on 2017/4/19.
 */

public class AlipayBean {

    /**
     * d_order_no : D20170419092617009
     * sign : app_id=2017041306678802&method=alipay.trade.app.pay&version=1.0&charset=utf-8&notify_url=http%3a%2f%2f120.77.149.109%3a4720%2fcapp%2fpay%2fAlipayAccRecNotify&sign_type=RSA2&timestamp=2017-04-19+09%3a26%3a17&biz_content=%7b%22out_trade_no%22%3a%22D20170419092617009%22%2c%22total_amount%22%3a0.0%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%2c%22body%22%3a%22%e6%98%be%e7%a4%ba%e4%bd%99%e9%a2%9d%e5%85%85%e5%80%bc%22%2c%22subject%22%3a%22%e6%98%be%e7%a4%ba%e4%bd%99%e9%a2%9d%e5%85%85%e5%80%bc%22%7d&sign=b2s7JXZoHjozCo3spF7%2fa94ticFPHOK69lPl42tDyUJ078dLhD3jodpJessoaIIez6Fc0l6NfqnOG4B5tAXenKQ9lII0sxKM22w86D2eSjXMSkZVLFzMs7%2bFKe3%2fK8lvoFomd0LO80jW%2bNsETaHkzfRRxIi3aGis%2fxTiEWmierw%2fSmVF0J8VK%2fXRbK5P99KseJe%2ffXVeLgYnbvJ9xqx0TT3pr%2bHaOQBcT%2fqS5rd4oR2oQRboeNee2BXM2OJQiSeQ038MTdSpdleyBDlNfZf7CMoNbUNw952z%2bpQdy8vJ8%2b4c%2fAplQ5a5DYfuWXH7m0FvvmUbgAdEmr67HHeVi8DfpA%3d%3d
     */

    private String d_order_no;
    private String sign;

    public String getD_order_no() {
        return d_order_no;
    }

    public void setD_order_no(String d_order_no) {
        this.d_order_no = d_order_no;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
