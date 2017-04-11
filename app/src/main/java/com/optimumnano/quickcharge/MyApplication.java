package com.optimumnano.quickcharge;

import android.app.Application;

import com.igexin.sdk.PushManager;
import com.optimumnano.quickcharge.service.GTPushService;
import com.optimumnano.quickcharge.service.MyIntentService;
import com.optimumnano.quickcharge.utils.ImageLoaderUtil;

import org.xutils.x;

/**
 * Created by ds on 2017/1/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        ImageLoaderUtil.getInstance().init(this);
        PushManager.getInstance().initialize(this.getApplicationContext(), GTPushService.class);

        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), MyIntentService.class);

    }
}
