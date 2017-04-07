package com.optimumnano.quickcharge.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.httpresponse.MyResponseInfo;
import com.optimumnano.quickcharge.utils.Tool;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.io.File;

import static org.xutils.x.http;

/**
 * Created by ds on 2016/8/15.
 * 网络请求
 */
public class MyHttpUtils<T> {
    private static final String TAG = MyHttpUtils.class.getSimpleName();
    private static volatile MyHttpUtils instance;

    public static MyHttpUtils getInstance() {
        synchronized (MyHttpUtils.class) {
            if (instance == null) {
                instance = new MyHttpUtils();
            }
        }
        return instance;

    }
    public Callback.Cancelable get( RequestParams maps, final HttpCallback<T> callback) {
        return get( maps, callback, -1);
    }

    /**
     * get 请求
     *
     * @param maps     参数
     * @param callback 回调
     * @param httpCode 网络请求响应吗
     */
    public Callback.Cancelable get( RequestParams maps, final HttpCallback<T> callback, final int httpCode) {
        return httpRequestIml(HttpMethod.GET,  maps, callback, httpCode);
    }

    public Callback.Cancelable delete(final String url, RequestParams maps, final HttpCallback<T> callback) {
        return delete( maps, callback, -1);
    }

    /**
     * delete 请求
     *
     * @param maps     参数
     * @param callback 回调
     * @param httpCode 网络请求响应吗
     */
    public Callback.Cancelable delete( RequestParams maps, final HttpCallback<T> callback, final int httpCode) {
        return httpRequestIml(HttpMethod.DELETE,  maps, callback, httpCode);
    }

    public Callback.Cancelable put( RequestParams maps, final HttpCallback<T> callback) {
        return put( maps, callback, -1);
    }

    /**
     * put 请求
     *
     * @param maps     参数
     * @param callback 回调
     * @param httpCode 网络请求响应吗
     */
    public Callback.Cancelable put( RequestParams maps, final HttpCallback<T> callback, final int httpCode) {
        return httpRequestIml(HttpMethod.PUT, maps, callback, httpCode);
    }

    /**
     * post 请求
     *
     * @param maps
     * @param callback
     */
    public Callback.Cancelable post( RequestParams maps, final HttpCallback<T> callback) {
        return post( maps, callback, -1);
    }

    /**
     * post 请求
     *
     * @param maps     参数
     * @param callback 回调
     * @param httpCode 网络请求响应吗
     */
    public Callback.Cancelable post( RequestParams maps, final HttpCallback<T> callback, final int httpCode) {
        return httpRequestIml(HttpMethod.POST, maps, callback, httpCode);
    }


    /**
     * 网络请求具体实现
     *
     * @param method   网络请求方式(POST\PUT\GET\DELETE)
     * @param callback 回调
     * @param httpCode 网络请求响应吗
     */
    private Callback.Cancelable httpRequestIml(HttpMethod method, RequestParams params, final HttpCallback callback, final int httpCode) {
        Callback.Cancelable cancelable = null;
        try {
            //检查网络是否可以，再进行下一步操作
            if (Tool.isConnectingToInternet()) {
                params.setConnectTimeout(30 * 1000);//超时时间30秒
//                params.setUseCookie(false);
                switch (method) {
                    case PUT:
                    case POST:
                        setPostHeader(params);
                        break;
                    case DELETE:
                    case GET:
                        setGetHeader(params);
                        break;
                }

                LogUtil.d(TAG + "======>>>>>Headers:  " + params.getHeaders());
                LogUtil.d(TAG + "======>>>>>Body:  " + params.getBodyContent());
                cancelable = http().request(method, params, new MyCommonCallback(callback, httpCode));
//                CancelableManager.getInstance().putCancelable();
            } else {
                callback.onFailure("无网络", HttpCallback.NOT_NET, httpCode);
            }
        } catch (Exception e) {
            callback.onFailure("请求异常", HttpCallback.EXCEPTION, httpCode);
            LogUtil.e(TAG, e);
        }
        return cancelable;
    }

    public static void setGetHeader(RequestParams params) {
        try {
            setCommHeader(params);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
    }
    //需要header则添加，否则就讲map中的参数添加至body
    public static void setPostHeader(RequestParams params) {
        try {
            setCommHeader(params);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
    }

    /**
     * 设置公共头信息
     *
     * @param params
     */
    private static void setCommHeader(RequestParams params) {
        params.addHeader("Accept", "application/json");
        params.addHeader("Content-Type", "application/json");
    }

    private class MyCommonCallback implements Callback.CommonCallback<MyResponseInfo> {

        private HttpCallback callback;
        private int httpCode;

        public MyCommonCallback(HttpCallback callback, int httpCode) {
            this.callback = callback;
            this.httpCode = httpCode;
        }

        @Override
        public void onSuccess(MyResponseInfo result) {
            LogUtil.d(TAG + "返回数据：======>>>>>  " + result.getResult());
            try {
                JSONObject dataJson = new JSONObject((result.getResult()));
                if (0 == dataJson.optInt("status")) {//操作成功
                    String data = dataJson.optString("result");
                    //检验数据
                    if (callback != null) {
                        if (!TextUtils.isEmpty(data)) {
                            try {
                                Class clazz = callback.getTClass();
                                int type = callback.getType();
                                if (type == 0){
                                    callback.onSuccess(JSON.parseObject(data, clazz),httpCode);
                                }
                                else {
                                    callback.onSuccess(JSON.parseArray(data,clazz),httpCode);
                                }
                            } catch (Exception e) {
                                LogUtil.e(TAG, e);
                                callback.onSuccess(data, httpCode);
                            }
                        } else {
                            callback.onSuccess(null, httpCode);
                        }
                    }
                } else {
                    callback.onFailure( dataJson.optString("resultMsg"),dataJson.optInt("status")+"", httpCode);
                }
            } catch (Exception e) {
                callback.onFailure("数据异常", null, httpCode);
                LogUtil.e(TAG, e);
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            try {
                if (callback != null) {
                    callback.onError(ex, isOnCallback, httpCode);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }

        @Override
        public void onCancelled(CancelledException cex) {
            if (callback != null) {
                callback.onCancelled(cex, httpCode);
            }
        }

        @Override
        public void onFinished() {
            if (callback != null) {
                callback.onFinished(httpCode);
            }
        }

    }
    public void download(RequestParams params, final ManagerCallback<String> callback) {
        final Callback.Cancelable cancelable = http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {
                LogUtil.d("scucess");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.d("onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.d("onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d("onFinished");
            }

            @Override
            public void onWaiting() {
                LogUtil.d("onWaiting");
            }

            @Override
            public void onStarted() {
                LogUtil.d("onStarted");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.d("onLoading" + total + "current" + current);
            }
        });
    }
}
