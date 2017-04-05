package com.optimumnano.quickcharge;

import android.app.Application;

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
    }
}
