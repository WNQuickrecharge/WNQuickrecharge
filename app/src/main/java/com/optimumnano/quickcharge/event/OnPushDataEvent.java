package com.optimumnano.quickcharge.event;

/**
 * Created by 秋平 on 2016/4/1.
 */
public class OnPushDataEvent {

    private String tag;
    private Object obj;


    public OnPushDataEvent(Object obj) {
        this.obj = obj;
    }

    public OnPushDataEvent(Object obj, String tag) {
        this.obj = obj;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public Object getObj() {
        return obj;
    }
}
