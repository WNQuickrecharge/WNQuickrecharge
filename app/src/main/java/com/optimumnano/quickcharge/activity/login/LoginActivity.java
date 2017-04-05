package com.optimumnano.quickcharge.activity.login;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.MainActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.LoginManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvLogin,tvReg,tvForgetpwd;
    private EditText edtUsername,edtPwd;
    LoginManager manager = new LoginManager();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                manager.login(edtUsername.getText().toString(),edtPwd.getText().toString(),new Manager());
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
            skipActivity(MainActivity.class,null);
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            showToast(msg);
        }
    }
}
