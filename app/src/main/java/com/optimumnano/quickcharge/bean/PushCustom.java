package com.optimumnano.quickcharge.bean;

import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.utils.JsonSerializer;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;

/**
 * Created by 秋平 on 2017/4/24 0024.
 */

public class PushCustom {
    public int msg_type;//: 1,//1是工单消息，2是充电消息，3是发票消息
    public int ask_state;//: 1,//1是已派单，4是已取消，5是已完成
    public String content;//;//"工单状态改变",
    public String car_no;//"粤BCV247",
    public String car_vin;//"LGAX4C445F3008374",
    public String phone;//"18986068297",
    public String name;//"测试黄璐",
    public String msgId;//"e06a5da9-7ad2-4186-93cd-91d4a451f0fa"


    public static void inject(String json) {
        PushCustom msg = JsonSerializer.deSerialize(json, PushCustom.class);
        switch (msg.msg_type) {
            case 1:
                EventBus.getDefault().post(new EventManager.onOrderDispatched(msg));
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

}
