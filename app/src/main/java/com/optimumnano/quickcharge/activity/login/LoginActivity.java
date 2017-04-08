package com.optimumnano.quickcharge.activity.login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.LoginManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_NICKNAME;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_MOBILE;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_PAYPASSWORD;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private TextView tvLogin,tvReg,tvForgetpwd,tvUserType;
    private EditText edtUsername,edtPwd;
    private ProgressDialog progressDialog;
    private int userType=1;
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
    }
    private void initListener(){
        tvLogin.setOnClickListener(this);
        tvReg.setOnClickListener(this);
        tvForgetpwd.setOnClickListener(this);
        tvUserType.setOnClickListener(this);
        tvUserType.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_tvLogin:
                String password = edtPwd.getText().toString();
                String Md5Password = MD5Utils.encodeMD5(password);
                showLoading();
                if ("企业登录".equals(tvLogin.getText().toString())){
                    userType=3;
                }else if ("个人登录".equals(tvUserType.getText().toString())){
                    userType=1;
                }
                manager.login(edtUsername.getText().toString(),Md5Password,userType,new Manager());
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
            LogUtil.i("return s==="+returnContent);
            hideLoading();
            JSONObject dataJson = null;
            try {
                dataJson = new JSONObject((String) returnContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String data = dataJson.optString("NickName");
            String payPassword = dataJson.optString("PayPassword");
            if (!TextUtils.isEmpty(payPassword)){
                SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_PAYPASSWORD,payPassword);
            }
            LogUtil.i("test==http NickName"+data);
            if (!TextUtils.isEmpty(data))
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_NICKNAME,data);
            SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_MOBILE,edtUsername.getText().toString());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
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
