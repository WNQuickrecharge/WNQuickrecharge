package com.optimumnano.quickcharge.activity.login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserInfo;
import com.optimumnano.quickcharge.manager.LoginManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.DESEncryptTools;
import com.optimumnano.quickcharge.utils.GlideCacheUtil;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_BALANCE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_HEADIMG_URL;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_IS_REMEMBER;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_MOBILE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_NICKNAME;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_PASSWORD;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_PAYPASSWORD;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_SEX;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private TextView tvLogin,tvReg,tvForgetpwd,tvUserType;
    private EditText edtUsername,edtPwd;
    private ProgressDialog progressDialog;
    private int userType=1;
    private CheckBox checkBox;
    private String pwdKey = "mfwnydgiyutjyg";
    LoginManager manager = new LoginManager();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog=new ProgressDialog(this);
        initViews();
        initListener();
    }

    @Override
    public void initViews() {
//        super.initViews();
        tvLogin = (TextView) findViewById(R.id.login_tvLogin);
        tvReg = (TextView) findViewById(R.id.login_tvReg);
        tvForgetpwd = (TextView) findViewById(R.id.login_tvForgetpwd);
        edtPwd = (EditText) findViewById(R.id.login_edtPwd);
        edtUsername = (EditText) findViewById(R.id.login_edtUsername);
        tvUserType= (TextView) findViewById(R.id.tv_login_type_textView);
        checkBox= (CheckBox) findViewById(R.id.login_checkbox);
        checkBox.setChecked(SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_IS_REMEMBER,false));
        if (checkBox.isChecked()){
            edtUsername.setText(SharedPreferencesUtil.getValue(SP_USERINFO,KEY_USERINFO_MOBILE,""));
            String password = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_PASSWORD, "");

            byte[] decode = Base64.decode(password.getBytes(), Base64.DEFAULT);
            byte[] decrypt = DESEncryptTools.decrypt(decode, pwdKey.getBytes());
            try {
                if (decrypt!=null)
                    edtPwd.setText(new String(decrypt,"utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            edtPwd.setText("");
            edtUsername.setText("");
        }
    }
    private void initListener(){
        tvLogin.setOnClickListener(this);
        tvReg.setOnClickListener(this);
        tvForgetpwd.setOnClickListener(this);
        tvUserType.setOnClickListener(this);
        tvUserType.addTextChangedListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_MOBILE,edtUsername.getText().toString());
                    SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_IS_REMEMBER,true);
                    return;
                }
                SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_MOBILE,"");
                SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_IS_REMEMBER,false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_tvLogin:
                String password = edtPwd.getText().toString();
                String Md5Password = MD5Utils.encodeMD5(password);
                String finalPassword = MD5Utils.encodeMD5(Md5Password);
                showLoading();
                if ("企业登录".equals(tvLogin.getText().toString())){
                    userType=3;
                }else if ("个人登录".equals(tvLogin.getText().toString())){
                    userType=1;
                }
                manager.login(edtUsername.getText().toString(),finalPassword,userType,new Manager());
                break;
            case R.id.login_tvReg:
                skipActivity(RegisterActivity.class,null);
                break;
            case R.id.login_tvForgetpwd:
                skipActivity(ForgetPwdActivity.class,null);
                break;
            case R.id.tv_login_type_textView:
                if ("企业".equals(tvUserType.getText().toString())){
                    tvUserType.setText("个人");
                }else if ("个人".equals(tvUserType.getText().toString())){
                    tvUserType.setText("企业");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        LogUtil.i("变化后的字符串是:"+s);
        String type = s.toString();
        if ("企业".equals(type)){
            tvReg.setVisibility(View.INVISIBLE);
            tvLogin.setText("企业登录");
        }else if ("个人".equals(type)){
            tvReg.setVisibility(View.VISIBLE);
            tvLogin.setText("个人登录");
        }
    }

    class Manager extends ManagerCallback{
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            showToast("登陆成功!");
            LogUtil.i("test==returnContent "+returnContent);
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

            JSONObject dataJson = null;
            try {
                dataJson = new JSONObject(returnContent.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String password = edtPwd.getText().toString();
            try {
                byte[] utf8s = DESEncryptTools.encrypt(password.getBytes("utf-8"), pwdKey.getBytes());
                byte[] encode = Base64.encode(utf8s, Base64.DEFAULT);
                SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_PASSWORD,new String(encode));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String data = dataJson.optString("userinfo");
            String payPassword = dataJson.optString("PayPassword");
            if (!TextUtils.isEmpty(payPassword)){
                SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_PAYPASSWORD,payPassword);
            }
            UserInfo userinfoBean = JSON.parseObject(dataJson.toString(), UserInfo.class);
            LogUtil.i("test==http NickName "+userinfoBean.userinfo.NickName+" Gender "+userinfoBean.userinfo.Gender+" PhoneNum "+userinfoBean.userinfo.PhoneNum+" headurl "+userinfoBean.userinfo.AvatarUrl+" RestCash balance "+userinfoBean.account.RestCash);

            String phoneNum = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_MOBILE, "");
            if (!phoneNum.equals(userinfoBean.userinfo.PhoneNum)){
                GlideCacheUtil.getInstance().clearImageAllCache(LoginActivity.this);
                SharedPreferencesUtil.getEditor(SP_USERINFO).clear().commit();
                LogUtil.i("test==Glide  clearDiskCache");
            }

            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_NICKNAME,TextUtils.isEmpty(userinfoBean.userinfo.NickName)?"":userinfoBean.userinfo.NickName);
            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_HEADIMG_URL,TextUtils.isEmpty(userinfoBean.userinfo.AvatarUrl)?"":userinfoBean.userinfo.AvatarUrl);
            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_SEX,userinfoBean.userinfo.Gender);
            SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_MOBILE,userinfoBean.userinfo.PhoneNum);
            DecimalFormat df = new DecimalFormat("0.00");
            SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_BALANCE,df.format(userinfoBean.account.RestCash));

            hideLoading();
            finish();
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            hideLoading();
            showToast(msg);
        }

        @Override
        public void onFailure(String msg, int requestCode) {
            super.onFailure(msg, requestCode);
            hideLoading();
            showToast(msg);
        }

    }

    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

    }


    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
