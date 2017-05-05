package com.optimumnano.quickcharge.bean;

import org.lzh.framework.updatepluginlib.model.Update;

/**
 * Created by chenwenguang on 2017/5/4.
 */

public class UpdateBean extends Update {
    public UpdateBean(String original) {
        super(original);
    }
    private int ret;
    private String platform;
    private String forcedUpdate;
    private String versionUrl;
    private String versionDesc;
    private String versionNo;
    private String msg;
    private String isLastVersion;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getForcedUpdate() {
        return forcedUpdate;
    }

    public void setForcedUpdate(String forcedUpdate) {
        this.forcedUpdate = forcedUpdate;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(String isLastVersion) {
        this.isLastVersion = isLastVersion;
    }
}
