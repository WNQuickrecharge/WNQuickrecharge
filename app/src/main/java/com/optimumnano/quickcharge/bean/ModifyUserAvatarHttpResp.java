package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/16.
 */

public class ModifyUserAvatarHttpResp extends BaseHttpResp {
    private AvatarData result;

    public AvatarData getResult() {
        return result;
    }

    public void setResult(AvatarData result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ModifyUserAvatarHttpResp{" +
                "result=" + result +
                '}';
    }

    public static class AvatarData {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "AvatarData{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }
}
