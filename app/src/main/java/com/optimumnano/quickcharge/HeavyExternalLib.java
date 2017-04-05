package com.optimumnano.quickcharge;

import android.app.Application;

/**
 * Created by 秋平 on 2017/1/19 0019.
 */

public class HeavyExternalLib {

    private HeavyExternalLib() {
    }

    public static HeavyExternalLib initialize(Application app) {
        HeavyExternalLib heavyExternalLib = new HeavyExternalLib();

        //Do some blocking operations
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        callMethod(app);
        return heavyExternalLib;
    }

    public static void callMethod(Application context) {
//        x.Ext.init(context);
//        x.Ext.setDebug(BuildConfig.DEBUG);
//        SDKInitializer.initialize(context);

////        FragmentUtils.getInstance();
////        createUserComponent(new User());
//        ZXingLibrary.initDisplayOpinion(context);
//        SDKInitializer.initialize(context);
//        Toast.makeText(context, "Heavy lib method called", Toast.LENGTH_LONG).show();
    }
}
