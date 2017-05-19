package com.optimumnano.quickcharge.http;

import android.support.compat.BuildConfig;
import android.util.Log;

public class HttpTask implements Runnable {
    private static final String TAG = "HttpTask";
    protected int id;
    protected BaseRequest request;
    protected HttpCallback callback;
    protected int result;

    protected TaskDispatcher taskDispatcher;

    public HttpTask(int taskId, BaseRequest request) {
        this(taskId, request, null);
    }

    public HttpTask(int taskId, BaseRequest request, HttpCallback callback) {
        this.id = taskId;
        this.request = request;
        this.callback = callback;
        if (this.callback == null) {
            this.callback = new HttpCallback.SimpleCallback();
        }
    }

    @Override
    public void run() {
        result = request.sent();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "this request task result is : " + result);
        }
        cleanTaskRefInDispatcher();
        publishResult();
    }

    public void cancel() {
        // TODO
        request.cancel();
    }

    protected void setDispatcher(TaskDispatcher dispatcher) {
        taskDispatcher = dispatcher;
        this.request.setDispatcher(dispatcher);
    }

    // private

    private void cleanTaskRefInDispatcher() {
        taskDispatcher.remove(id);
    }

    private void publishResult() {
        TaskDispatcher.HANDLER.post(REPORT_TASK_RESULT_RUNNABLE);
    }

    private Runnable REPORT_TASK_RESULT_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            if (callback == null) {
                return;
            }
            switch (result) {
                case HttpResult.SUCCESS:
                    callback.onRequestSuccess(id, request.result);
                    break;
                case HttpResult.FAIL:
                    callback.onRequestFail(id, request.result);
                    break;
                case HttpResult.CANCELED:
                    callback.onRequestCancel(id);
                    break;
            }
        }
    };
}
