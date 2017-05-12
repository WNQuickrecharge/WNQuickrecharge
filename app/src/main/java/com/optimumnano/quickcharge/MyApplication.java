package com.optimumnano.quickcharge;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.optimumnano.quickcharge.bean.UpdateBean;
import com.optimumnano.quickcharge.net.HttpApi;
import com.optimumnano.quickcharge.service.GTPushService;
import com.optimumnano.quickcharge.utils.ImageLoaderUtil;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.callback.UpdateCheckCB;
import org.lzh.framework.updatepluginlib.callback.UpdateDownloadCB;
import org.lzh.framework.updatepluginlib.model.HttpMethod;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.File;

/**
 * Created by ds on 2017/1/16.
 */
public class MyApplication extends Application {
    public static boolean getuiflag=false;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        ImageLoaderUtil.getInstance().init(this);
        PushManager.getInstance().initialize(this.getApplicationContext(), GTPushService.class);

        //PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), MyIntentService.class);
        instance=this;
        SDKInitializer.initialize(this);
        updateVersion();
    }
    public static MyApplication getInstance(){
        return instance;
    }
    private void updateVersion() {
        String url= HttpApi.getInstance().getUrl(HttpApi.update_apk_url);
        UpdateConfig.getConfig().url(url).checkEntity(UpdateConfig.getConfig().getCheckEntity().setMethod(HttpMethod.POST)).jsonParser(new UpdateParser() {
            @Override
            public UpdateBean parse(String httpResponse) {
                //                        Log.e("AppContext", httpResponse);
                Gson gson=new Gson();
                UpdateBean update = gson.fromJson(httpResponse, UpdateBean.class);
                if (update.getVersionNo() != 0) {
                    update.setUpdateTime(System.currentTimeMillis());
                    // 此apk包的下载地址
                    update.setUpdateUrl(update.getVersionUrl());
                    // 此apk包的版本号
                    update.setVersionCode(update.getVersionNo());
                    // 此apk包的版本名称
                    update.setVersionName(update.getVersionName());
                    // 此apk包的更新内容
                    update.setUpdateContent(update.getVersionDesc());
                    // 此apk包是否为强制更新
                    //                        update.setForced(false);
                    update.setForced(update.getForcedUpdate().equals("1"));
                    // 是否显示忽略此次版本更新按钮
                    update.setIgnore(true);
                }
                return update;
            }
        }).checkCB(new UpdateCheckCB() {

             public void onCheckError(int code, String errorMsg) {
                LogUtil.e("更新失败：code:" + code + ",errorMsg:" + errorMsg);
            }

            @Override public void onUserCancel() {
                LogUtil.e("用户取消更新");
            }

            @Override public void onCheckIgnore(Update update) {
                LogUtil.e("用户忽略此版本更新");
            }

            @Override
            public void onCheckStart() {

            }

            @Override public void hasUpdate(Update update) {
                LogUtil.e("检查到有更新");
            }

            @Override public void noUpdate() {
                LogUtil.e("无更新");
            }

            @Override
            public void onCheckError(Throwable t) {

            }
        }).downloadCB(new UpdateDownloadCB() {
            @Override public void onUpdateStart() {
                LogUtil.e("下载开始");
            }

            @Override public void onUpdateComplete(File file) {
                LogUtil.e("下载完成");
            }

            @Override public void onUpdateProgress(long current, long total) {
            }

            @Override
            public void onUpdateError(Throwable t) {

            }

             public void onUpdateError(int code, String errorMsg) {
                LogUtil.e("下载失败：code:" + code + ",errorMsg:" + errorMsg);
            }
        }).strategy(new UpdateStrategy() {
            @Override public boolean isShowUpdateDialog(Update update) {
                return true;//开启wifi下更新提示
            }

            @Override public boolean isAutoInstall() {
                return true;
            }

            @Override public boolean isShowDownloadDialog() {
                return true;
            }
        });
    }
}
