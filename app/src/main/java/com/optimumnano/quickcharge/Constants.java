package com.optimumnano.quickcharge;

/**
 * Created by 秋平 on 2017/4/5 0005.
 */

public class Constants {

    public static final String WX_APP_ID = "wxcf3083b3dd2841fc";//微信appid
    public static final String WX_PARTNER_ID = "1462160702";//微信商户号

    /**
     * Result Return
     */
    public static final String RESULT_TYPE = "result_type";
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILED = 0;

    public static final String RESULT_STRING = "result_string";


    public static final String APP_FOLDER_NAME = "WNQuickrecharge";

    public static String mSDCardPath = null;

    //订单状态
    public static final int GETCHARGEPROGRESS = 1;
    public static final int STARTCHARGE = 2;
    //判断是否开发票余额支付
    public static Boolean isInvoiceYue = false;
}
