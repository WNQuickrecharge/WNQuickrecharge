package com.optimumnano.quickcharge.bean;

import java.io.Serializable;

/**
 * Created by ds on 2017/4/1.
 */
public class UserInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    public String name;
    public String password;

    /**
     * userinfo : {"Id":11,"PhoneNum":"13724253750","UserName":"13724253750","PassWord":"e10adc3949ba59abbe56e057f20f883e","PayPassword":null,"UserType":1,"UserNature":1,"RegisteredType":2,"CompanyId":null,"Gender":1,"AvatarUrl":null,"Signature":null,"NickName":"呵呵呵","CreateTime":"2017-04-06T09:09:25.277","CreateBy":"Register","UpdateTime":null,"UpdateBy":null,"Remark":null,"IsDel":false}
     * account : {"Id":6,"UserId":11,"AmmountType":1,"TotalCash":0,"FrozenCash":0,"RestCash":0,"CreateTime":"2017-04-06T09:09:25.333","CreateBy":"Register","UpdateTime":null,"UpdateBy":null,"Remark":null,"IsDel":false}
     */

    public UserinfoBean userinfo;
    public AccountBean account;

    public static class UserinfoBean {
        /**
         * Id : 11
         * PhoneNum : 13724253750
         * UserName : 13724253750
         * PassWord : e10adc3949ba59abbe56e057f20f883e
         * PayPassword : null
         * UserType : 1
         * UserNature : 1
         * RegisteredType : 2
         * CompanyId : null
         * Gender : 1
         * AvatarUrl : null
         * Signature : null
         * NickName : 皮皮虾
         * CreateTime : 2017-04-06T09:09:25.277
         * CreateBy : Register
         * UpdateTime : null
         * UpdateBy : null
         * Remark : null
         * IsDel : false
         */

        public int Id;
        public String PhoneNum;
        public String UserName;
        public String PassWord;
        public Object PayPassword;
        public int UserType;
        public int UserNature;
        public int RegisteredType;
        public Object CompanyId;
        public int Gender;
        public Object AvatarUrl;
        public Object Signature;
        public String NickName;
        public String CreateTime;
        public String CreateBy;
        public Object UpdateTime;
        public Object UpdateBy;
        public Object Remark;
        public boolean IsDel;
    }

    public static class AccountBean {
        /**
         * Id : 6
         * UserId : 11
         * AmmountType : 1
         * TotalCash : 0
         * FrozenCash : 0
         * RestCash : 0
         * CreateTime : 2017-04-06T09:09:25.333
         * CreateBy : Register
         * UpdateTime : null
         * UpdateBy : null
         * Remark : null
         * IsDel : false
         */

        public int Id;
        public int UserId;
        public int AmmountType;
        public int TotalCash;
        public int FrozenCash;
        public int RestCash;
        public String CreateTime;
        public String CreateBy;
        public Object UpdateTime;
        public Object UpdateBy;
        public Object Remark;
        public boolean IsDel;
    }
}
