package com.optimumnano.quickcharge.activity.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.LoginManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.StringUtils;

import static com.optimumnano.quickcharge.R.id.register_edtConfirmPwd;

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {
    private EditText edtPhone,edtChecknum,edtPwd,edtConfirmPwd;
    private TextView tvReg,tvChecknum;
    private LoginManager loginManager = new LoginManager();
    private ShortMessageCountDownTimer smcCountDownTimer;
    private RequestCallback requestCallback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        initViews();
        initListener();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle(getString(R.string.forgetpwd));

        edtPhone = (EditText) findViewById(R.id.register_edtPhone);
        edtChecknum = (EditText) findViewById(R.id.register_edtChecknum);
        edtPwd = (EditText) findViewById(R.id.register_edtPwd);
        edtConfirmPwd = (EditText) findViewById(register_edtConfirmPwd);
        tvChecknum = (TextView) findViewById(R.id.register_tvChecknum);
        tvReg = (TextView) findViewById(R.id.register_tvRegister);

        requestCallback = new RequestCallback();
    }
    public void initListener(){
        tvReg.setOnClickListener(this);
        tvChecknum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_tvChecknum:
                getChecknum();
                break;
            case R.id.register_tvRegister:
                loginManager.register(edtPhone.getText().toString(), edtPwd.getText().toString(), edtChecknum.getText().toString(),
                        edtConfirmPwd.getText().toString(), requestCallback, 1);
                break;
            default:
                break;
        }
    }

    private void getChecknum() {
        String mobile = edtPhone.getText().toString();
        if (StringUtils.isEmpty(mobile)){
            showToast("电话号码不能为空");
            return;
        }
        if (!StringUtils.isMobile(mobile)){
            showToast("电话号码格式不对");
            return;
        }
        tvChecknum.setClickable(false);
        startCountTime(60*1000,1000);
        loginManager.getCheckNum(mobile,requestCallback,0);
    }

    class RequestCallback extends ManagerCallback<String> {
        @Override
        public void onSuccess(String returnContent, int requestCode) {
            super.onSuccess(returnContent, requestCode);
            //获取验证码成功
            if (requestCode==0){

            }
            else {

            }
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            showToast(msg);
            stopCountTime();
            tvChecknum.setText("重新获取");
            tvChecknum.setClickable(true);
        }
    }

    private void startCountTime(long allTime, long time) {
        stopCountTime();
        smcCountDownTimer = new ShortMessageCountDownTimer(allTime, time);
        smcCountDownTimer.start();
    }

    private void stopCountTime() {
        if (smcCountDownTimer != null) {
            smcCountDownTimer.cancel();
        }
    }


    class ShortMessageCountDownTimer extends CountDownTimer {

        public ShortMessageCountDownTimer(long millisInFuture,
                                          long countDownInterval) {
            super(millisInFuture, countDownInterval);
            tvChecknum.setText(millisInFuture / 1000 + " S");
        }

        @Override
        public void onTick(long arg0) {
            tvChecknum.setText(arg0 / 1000 + " S");

        }

        @Override
        public void onFinish() {
            tvChecknum.setText("重新获取");
            tvChecknum.setClickable(true);
        }

    }
}
