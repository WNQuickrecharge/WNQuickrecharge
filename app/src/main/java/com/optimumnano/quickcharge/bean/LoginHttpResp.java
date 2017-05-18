package com.optimumnano.quickcharge.bean;

/**
 * Created by herry on 2017/5/15.
 */

public class LoginHttpResp extends BaseHttpResp {

    private LoginBean result;


    public LoginBean getResult() {
        return result;
    }

    public void setResult(LoginBean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LoginHttpResp{" +
                "result=" + result +
                '}';
    }

    public static class LoginBean {
        private UserInfo userinfo;
        private Account account;

        public UserInfo getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserInfo userinfo) {
            this.userinfo = userinfo;
        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }

        @Override
        public String toString() {
            return "LoginBean{" +
                    "userinfo=" + userinfo +
                    ", account=" + account +
                    '}';
        }
    }

    public static class UserInfo {
        private int id;
        private String PhoneNum;
        private String UserName;
        private int UserType;
        private int UserNature;
        private String CompanyId;
        private int Gender;
        private String AvatarUrl;
        private String Signature;
        private String NickName;
        private String IsSetPayPwd;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhoneNum() {
            return PhoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            PhoneNum = phoneNum;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public int getUserType() {
            return UserType;
        }

        public void setUserType(int userType) {
            UserType = userType;
        }

        public int getUserNature() {
            return UserNature;
        }

        public void setUserNature(int userNature) {
            UserNature = userNature;
        }

        public String getCompanyId() {
            return CompanyId;
        }

        public void setCompanyId(String companyId) {
            CompanyId = companyId;
        }

        public int getGender() {
            return Gender;
        }

        public void setGender(int gender) {
            Gender = gender;
        }

        public String getAvatarUrl() {
            return AvatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            AvatarUrl = avatarUrl;
        }

        public String getSignature() {
            return Signature;
        }

        public void setSignature(String signature) {
            Signature = signature;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getIsSetPayPwd() {
            return IsSetPayPwd;
        }

        public void setIsSetPayPwd(String isSetPayPwd) {
            IsSetPayPwd = isSetPayPwd;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "id=" + id +
                    ", PhoneNum='" + PhoneNum + '\'' +
                    ", UserName='" + UserName + '\'' +
                    ", UserType=" + UserType +
                    ", UserNature=" + UserNature +
                    ", CompanyId='" + CompanyId + '\'' +
                    ", Gender=" + Gender +
                    ", AvatarUrl='" + AvatarUrl + '\'' +
                    ", Signature='" + Signature + '\'' +
                    ", NickName='" + NickName + '\'' +
                    ", IsSetPayPwd=" + IsSetPayPwd +
                    '}';
        }
    }

    public static class Account {
        private float TotalCash;
        private float FrozenCash;
        private float RestCash;
        private String Coupons;

        public float getTotalCash() {
            return TotalCash;
        }

        public void setTotalCash(float totalCash) {
            TotalCash = totalCash;
        }

        public float getFrozenCash() {
            return FrozenCash;
        }

        public void setFrozenCash(float frozenCash) {
            FrozenCash = frozenCash;
        }

        public float getRestCash() {
            return RestCash;
        }

        public void setRestCash(float restCash) {
            RestCash = restCash;
        }

        public String getCoupons() {
            return Coupons;
        }

        public void setCoupons(String coupons) {
            Coupons = coupons;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "TotalCash=" + TotalCash +
                    ", FrozenCash=" + FrozenCash +
                    ", RestCash=" + RestCash +
                    ", Coupons='" + Coupons + '\'' +
                    '}';
        }
    }
}
