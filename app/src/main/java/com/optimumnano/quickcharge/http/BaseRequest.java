package com.optimumnano.quickcharge.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

public abstract class BaseRequest {
    private static final String TAG = "BaseRequest";

    protected BaseResult result;
    protected boolean needCookie;
    protected TaskDispatcher taskDispatcher;
    protected int retryTimes;

    // request object
    protected Request.Builder requestBuilder;
    protected Call call;
    protected HttpUrl httpUrl;
    protected ArrayMap<String, String> headers;
    protected ArrayMap<String, String> params;
    protected RequestBody requestBody;
    protected String url;
    protected Response response;

    // state
    protected boolean alive;

    public BaseRequest(BaseResult result) {
        this(result, false);
    }

    public BaseRequest(BaseResult result, boolean needCookie) {
        this.result = result;
        this.result.setRequest(this);
        this.needCookie = needCookie;
        this.retryTimes = InternalConstants.DEF_RETRY_TIMES;
        this.alive = true;
    }

    protected void setDispatcher(TaskDispatcher dispatcher) {
        this.taskDispatcher = dispatcher;
    }

    public int directSent(Context context) {
        this.taskDispatcher = TaskDispatcher.getInstance(context);
        return sent();
    }


    /**
     * 三种处理结果{@code HttpResult}
     *
     * @return
     */
    protected int sent() {
        int ret = HttpResult.FAIL;
        response = null;
        ret = result.preProcess();
        if (ret == HttpResult.SUCCESS) {
            // 如果结果在预处理中就已经获取到，直接返回，不执行http请求
            return ret;
        }
        //
        onPreSent();
        retryTimes = getRetryTimes();
        url = getUrl();
        headers = getHeaders();
        params = getParams();
        requestBody = getRequestBody();
        // TODO add auto-reg logic
        if (CommonUtils.isStringInvalid(url)) {
            return ret;
        }
        for (int i = 0; isAlive() && response == null && i < retryTimes; i++) {
            initRequest();
            addCommonHeaders();
            addExtHeaders();
            addParams();
            setRequestUrl();
            setRequestBody();
            Log.d(TAG, String.format("final request url : %s ,with times : ( %s )", httpUrl.url(), (i + 1)));
            try {
                call = taskDispatcher.httpClient.newCall(requestBuilder.build());
                Headers headers = call.request().newBuilder().build().headers();
                for (int j = 0; j < headers.size(); j++) {
                    String headerName = headers.name(j);
                    String headerValue = headers.value(j);
                    Log.e("ttt", "headerName : " + headerName + ",headerValue : " + headerValue);
                }
                response = call.execute();
                int code = response.code();
                Log.d(TAG, "status code : " + code);
                Log.d(TAG, "response : " + response);
                if (result.processStatus(code)) {
                    response = null;
                    return ret;
                }
                if (!response.isSuccessful()) {
                    response = null;// make it continue
                    continue;
                }
                if (isGzipResponse()) {
                    fixGzipFeature();
                }
                ret = result.parseResponse(response);
            } catch (Exception e) {
                Log.e(TAG, String.format("Exception occur within request times : ( %s )", (i + 1)), e);
                if (call.isCanceled()) {
                    ret = HttpResult.CANCELED;
                    return ret;
                }
                if (i < retryTimes) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        /* 如果线程被终止，则中止本次http请求 */
                        alive = false;
                        ret = HttpResult.CANCELED;
                        return ret;
                    }
                    if (response != null) {
                        response = null;// make it continue
                    }
                }

            } finally {
                if (response != null) {
                    response.body().close();
                }
            }
        }
        Log.e(TAG, "finish request,ret : " + ret);
        if (!alive) {
            // check if request canceled
            ret = HttpResult.CANCELED;
        }
        return ret;
    }

    protected boolean isAlive() {
        return alive;
    }

    // private

    private void initRequest() {
        requestBuilder = new Request.Builder();
        httpUrl = HttpUrl.parse(url);
    }

    private void addCommonHeaders() {
        requestBuilder.header(InternalConstants.HEADER_ACCEPT_CODING, InternalConstants.VALUE_ACCEPT_CODING);
        requestBuilder.header(InternalConstants.HEADER_ACCEPT, InternalConstants.VALUE_ACCEPT);
        requestBuilder.header(InternalConstants.HEADER_CONTENT_TYPE, InternalConstants.VALUE_CONTENT_TYPE);
    }

    /* 添加专用的htttp header */
    private void addExtHeaders() {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        Set<Entry<String, String>> entrys = headers.entrySet();
        Iterator<Entry<String, String>> it = entrys.iterator();
        Entry<String, String> entry = null;
        while (it.hasNext()) {
            entry = it.next();
            requestBuilder.header(entry.getKey(), entry.getValue());
        }
    }

    /* 增加url查询参数 */
    private void addParams() {
        if (params == null || params.isEmpty()) {
            return;
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        Set<Entry<String, String>> entrys = params.entrySet();
        Iterator<Entry<String, String>> it = entrys.iterator();
        Entry<String, String> entry = null;
        while (it.hasNext()) {
            entry = it.next();
            urlBuilder.setEncodedQueryParameter(entry.getKey(), entry.getValue());
        }
        httpUrl = urlBuilder.build();
    }

    private void setRequestUrl() {
        requestBuilder.url(httpUrl);
    }

    private void setRequestBody() {
        if (requestBody == null) {
            return;
        }
        requestBuilder.post(requestBody);
    }

    private void fixGzipFeature() {
        GzipSource gzip = new GzipSource(response.body().source());
        BufferedSource newSource = Okio.buffer(gzip);
        response = response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), response.body().contentLength(), newSource))
                .build();
    }

    private boolean isGzipResponse() {
        Headers headers = response.headers();
        List<String> values = headers.values(InternalConstants.HEADER_CONTENT_ENCODING);
        if (values.isEmpty()) {
            return false;
        }
        for (String value : values) {
            if (value.equalsIgnoreCase(InternalConstants.VALUE_CONTENT_ENCODING)) {
                return true;
            }
        }
        return false;
    }

    // for override
    protected int getRetryTimes() {
        // defalut value
        return InternalConstants.DEF_RETRY_TIMES;
    }

    protected RequestBody getRequestBody() {
        return null;// default
    }

    protected void onPreSent() {
        //
    }

    protected void onPostSent() {
        //
    }

    // abstract

    protected abstract String getUrl();

    protected abstract ArrayMap<String, String> getHeaders();

    protected abstract ArrayMap<String, String> getParams();

    // public

    public void cancel() {
        alive = false;
        if (call != null) {
            call.cancel();
        }
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

}
