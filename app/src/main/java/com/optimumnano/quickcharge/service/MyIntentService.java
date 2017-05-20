package com.optimumnano.quickcharge.service;

import android.content.Context;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.optimumnano.quickcharge.MyApplication;
import com.optimumnano.quickcharge.bean.PushCustom;
import com.optimumnano.quickcharge.request.GetUIPushRequest;
import com.optimumnano.quickcharge.response.GetUIPushResult;
import com.optimumnano.quickcharge.utils.LogUtils;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class MyIntentService extends GTIntentService {

    public MyIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String data = new String(msg.getPayload());
        LogUtils.i("test==onReceiveMessageData " + data);
        PushCustom.inject(data);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtils.i("test==onReceiveClientId " + clientid);
//        GetuiPushManager.setGetuiRegisterid(clientid, new ManagerCallback() {
//            @Override
//            public void onSuccess(Object returnContent) {
//                super.onSuccess(returnContent);
//                LogUtil.i("test==setRegistId success ");
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                super.onFailure(msg);
//                LogUtil.i("test==setRegistId failure " + msg);
//            }
//        });
        if (MyApplication.getuiflag) {
            return;
        }
        GetUIPushRequest r = new GetUIPushRequest(new GetUIPushResult(getApplicationContext()), clientid);
        r.directSent(getApplicationContext());
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}