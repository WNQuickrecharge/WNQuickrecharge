package com.optimumnano.quickcharge.activity.login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserInfo;
import com.optimumnano.quickcharge.manager.LoginManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_HEADIMG_URL;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_MOBILE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_NICKNAME;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_SEX;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvLogin,tvReg,tvForgetpwd;
    private EditText edtUsername,edtPwd;
    private ProgressDialog progressDialog;
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
    }
    private void initListener(){
        tvLogin.setOnClickListener(this);
        tvReg.setOnClickListener(this);
        tvForgetpwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_tvLogin:
                String password = edtPwd.getText().toString();
                String Md5Password = MD5Utils.encodeMD5(password);
                showLoading();
                manager.login(edtUsername.getText().toString(),Md5Password,new Manager());
                break;
            case R.id.login_tvReg:
                skipActivity(RegisterActivity.class,null);
                break;
            case R.id.login_tvForgetpwd:
                skipActivity(ForgetPwdActivity.class,null);
                break;
            default:
                break;
        }
    }

    class Manager extends ManagerCallback{
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            showToast("登陆成功!");
            LogUtil.i("test==returnContent "+returnContent);
            hideLoading();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

            JSONObject dataJson = null;
            try {
                dataJson = new JSONObject(returnContent.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String data = dataJson.optString("userinfo");

            UserInfo.UserinfoBean userinfoBean = JSON.parseObject(data, UserInfo.UserinfoBean.class);
            LogUtil.i("test==http NickName "+userinfoBean.NickName+" Gender "+userinfoBean.Gender+" PhoneNum "+userinfoBean.PhoneNum+" headurl "+userinfoBean.AvatarUrl);
            if (!TextUtils.isEmpty(userinfoBean.NickName))
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_NICKNAME,userinfoBean.NickName);
            if (!TextUtils.isEmpty(userinfoBean.AvatarUrl))
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_HEADIMG_URL,userinfoBean.AvatarUrl);
            SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_SEX,userinfoBean.Gender);
            SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_MOBILE,userinfoBean.PhoneNum);

            finish();
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
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
