package com.optimumnano.quickcharge.service;

import android.content.Context;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.optimumnano.quickcharge.manager.GetuiPushManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import org.xutils.common.util.LogUtil;

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
        LogUtil.i("test==onReceiveMessageData "+data);

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtil.i("test==onReceiveClientId "+clientid);
        GetuiPushManager.setGetuiRegisterid(clientid,new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                LogUtil.i("test==setRegistId success ");
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                LogUtil.i("test==setRegistId failure "+msg);
            }
        });
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}