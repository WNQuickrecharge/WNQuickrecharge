package com.optimumnano.quickcharge.net;


import com.optimumnano.quickcharge.base.EventTask;
import com.optimumnano.quickcharge.manager.EventManager;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 网络请求回调
 */
public abstract class HttpCallback<T> {

    public static final String ERROR_MSG_DEFAULT = "网络请求出错啦~";
    public static final String NOT_NET = "not_net";
    public static final String EXCEPTION = "exception";

    public void onSuccess(T result, int httpCode) {
        EventTask task = new EventTask(result,httpCode);
        onSuccess(result,httpCode,task);
    }

    /**
     * 把参数封装为eventtask
     * @param result
     * @param httpCode
     */
    public void onSuccess(T result, int httpCode, EventTask task) {
    }



    public void onFailure(String msg, String errorCode, int httpCode) {

    }

    public void onError(Throwable ex, boolean isOnCallback, int httpCode) {
        String errorMsg = ERROR_MSG_DEFAULT;
        if (ex instanceof ConnectException) {
            errorMsg =  "不能连接到服务器...";
        }
        if (ex instanceof SocketTimeoutException) {
            errorMsg = "连接超时...";
        }
        String errorCode="";
        try {
            errorCode =  ((HttpException) ex).getCode()+"";
            errorMsg = ERROR_MSG_DEFAULT;
        }catch (Exception e){
            onFailure(errorMsg, errorCode, httpCode);
        }
        if (errorCode.equals("401")){
            errorCode = ex.getMessage();
            EventBus.getDefault().post(new EventManager.cookieTimeOut());
            onFailure("您已退出登录,请重新登陆", errorCode, httpCode);
            return;
        }
        onFailure(errorMsg, errorCode, httpCode);

    }

    public void onCancelled(Callback.CancelledException cex, int httpCode) {
    }

    public void onFinished(int httpCode) {
    }

    public T getT() throws InstantiationException, IllegalAccessException {
        Class<T> mTClass = getTClass();
        return mTClass.newInstance();
    }

    public Class getTClass() throws InstantiationException, IllegalAccessException {
        Type sType = getClass().getGenericSuperclass();

        if (sType.toString().contains("List<")){
            Class cla = null;
            try {
                String className = sType.toString().substring(sType.toString().indexOf("List<")+5,sType.toString().indexOf(">"));
                className = className.replaceAll(">","");
                cla = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return cla;
        }
        else {
            Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
            Class mTClass = (Class) generics[0];
            return mTClass;
        }

    }

    /**
     * 如果是list返回1,0表示实体或者string，int等类型
     * @return
     */
    public int getType(){
        Type sType = getClass().getGenericSuperclass();
        if (sType.toString().contains("List<")){
            return 1;
        }
        else {
            return 0;
        }
    }
//    public Class getType1(){
//        Type sType = getClass().getGenericSuperclass();
//        Class cla = null;
//        try {
//            String className = sType.toString().substring(sType.toString().indexOf("List<")+5,sType.toString().indexOf(">"));
//            className = className.replaceAll(">","");
//            cla = Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return cla;
//    }
}
