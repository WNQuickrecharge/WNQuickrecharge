package com.optimumnano.quickcharge.http;

import com.optimumnano.quickcharge.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;

public class LogInterceptor implements Interceptor {
    private static final String TAG = "quickcharge";

    @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            LogUtils.i(TAG, "=======>request:  " + request.toString());
            okhttp3.Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtils.i("=======>response body:  " + content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }