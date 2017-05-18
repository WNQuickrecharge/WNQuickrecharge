package com.optimumnano.quickcharge.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class TaskDispatcher {
    private static final String TAG = "TaskDispatcher";

    static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private SparseArray<HttpTask> taskMap;

    protected OkHttpClient httpClient;

    protected CookieMgr cookieMgr;

    private static TaskDispatcher INSTANCE = null;

    public static TaskDispatcher getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDispatcher.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TaskDispatcher(context);
                }
            }
        }
        return INSTANCE;
    }

    private TaskDispatcher(Context context) {
        taskMap = new SparseArray<HttpTask>();
        cookieMgr = CookieMgr.getInstance(context);
        try {
            httpClient = new OkHttpClient.Builder()
//                    .hostnameVerifier(new AllHostNameVerifier())
//                    .sslSocketFactory(AllTrustSSLSocketFactory.get())
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS)
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                            Log.e("herry", "saveFromResponse ,list : " + list);
                            cookieMgr.saveCookie(httpUrl, list);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                            List<Cookie> list = cookieMgr.getCookie(httpUrl);
                            Log.e("herry", "loadForRequest,list : " + list);
                            return list;
                        }
                    })
                    .build();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    /**
     * 发送http请求
     *
     * @param task
     */
    public void dispatch(HttpTask task) {
        synchronized (taskMap) {
            taskMap.put(task.id, task);
        }
        task.setDispatcher(this);
        new Thread(task).start();
    }

    public void cancel(int taskId) {
        HttpTask task = get(taskId);
        if (task == null) {
            return;
        }
        task.cancel();

    }

    HttpTask get(int taskId) {
        HttpTask task = null;
        synchronized (taskMap) {
            task = taskMap.get(taskId);
        }
        return task;
    }

    protected void remove(int taskId) {
        synchronized (taskMap) {
            taskMap.remove(taskId);
        }
    }
}
