package com.optimumnano.quickcharge.net;

import org.xutils.common.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/28.
 */
public abstract class ManagerCallback<T> {
    /**
     * 请求成功
     *
     * @param returnContent 请求返回结果
     */
    public void onSuccess(T returnContent) {
    }

    /**
     * 请求成功
     * @param returnContent 请求结果
     * @param requestCode 请求code
     */
    public void onSuccess(T returnContent,int requestCode){}
    /**
     * 请求失败
     *
     * @param msg 消息
     */
    public void onFailure(String msg) {
    }
    /**
     * 进度条更新
     */
    public void onProgress(long total, long current, boolean isUploading) {
    }
    public void onSuccess(T returnContent, Map<String, List<String>> header) {

    }
    /**
     * 取消网络请求
     *
     * @param cex
     */
    public void onCancelled(Callback.CancelledException cex) {
    }

    ;

    public Class<T> getTClass() throws InstantiationException, IllegalAccessException {
        Type sType = getClass().getGenericSuperclass();
        Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
        Class<T> mTClass = (Class<T>) (generics[0]);
        return mTClass;
    }

}
