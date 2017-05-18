package com.optimumnano.quickcharge.http;

public class InternalConstants {
    /* http请求默认次数 */
    static final int DEF_RETRY_TIMES = 3;

    /* http header */
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_ACCEPT_CODING = "Accept-Encoding";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_APP_VERSION = "hc_version";
    public static final String HEADER_APP_CODE = "hc_code";
    public static final String HEADER_APP_PACKAGE = "hc_package";
    public static final String HEADER_APP_AGENT = "hc_agent";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    /* http header value */
    public static final String VALUE_ACCEPT_CODING = "gzip,deflate,sdch";
    public static final String VALUE_CONTENT_ENCODING = "gzip";
    public static final String VALUE_APP_AGENT = "shoppingcar";
    public static final String VALUE_ACCEPT = "application/json";
    public static final String VALUE_CONTENT_TYPE = "application/json";
    public static final String MEDIA_TYPE = "application/json";

}
