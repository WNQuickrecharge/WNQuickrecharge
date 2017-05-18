package com.optimumnano.quickcharge.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.LoginManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.request.GetVerifyCodeRequest;
import com.optimumnano.quickcharge.request.RegisterRequest;
import com.optimumnano.quickcharge.response.GetVerifyCodeResult;
import com.optimumnano.quickcharge.response.RegisterResult;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;

import static com.optimumnano.quickcharge.R.id.register_edtConfirmPwd;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, HttpCallback {
    private EditText edtPhone, edtChecknum, edtPwd, edtConfirmPwd;
    private TextView tvReg, tvChecknum;
    private LoginManager loginManager = new LoginManager();
    private RequestCallback requestCallback;
    private ShortMessageCountDownTimer smcCountDownTimer;

    private int mGetVerifyCodeTaskId;
    private int mRegisterTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskDispatcher.cancel(mGetVerifyCodeTaskId);
        mTaskDispatcher.cancel(mRegisterTaskId);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle(getString(R.string.register));

        edtPhone = (EditText) findViewById(R.id.register_edtPhone);
        edtChecknum = (EditText) findViewById(R.id.register_edtChecknum);
        edtPwd = (EditText) findViewById(R.id.register_edtPwd);
        edtConfirmPwd = (EditText) findViewById(register_edtConfirmPwd);
        tvChecknum = (TextView) findViewById(R.id.register_tvChecknum);
        tvReg = (TextView) findViewById(R.id.register_tvRegister);

        requestCallback = new RequestCallback();
    }

    public void initListener() {
        tvReg.setOnClickListener(this);
        tvChecknum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_tvChecknum:
                getChecknum();
                break;
            case R.id.register_tvRegister:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        String confirmPassword = edtConfirmPwd.getText().toString();
        String password = edtPwd.getText().toString();
        if (!TextUtils.equals(confirmPassword, password)) {
            showToast("两次密码不一致,请确认!");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(password)) {
            showToast("密码不能为空!");
            return;
        }
        if (TextUtils.isEmpty(edtChecknum.getText().toString())) {
            showToast("验证码不能为空!");
            return;
        }
        if (TextUtils.isEmpty(edtPhone.getText().toString())) {
            showToast("手机号码不能为空!");
            return;
        }
        if (confirmPassword.length() < 6 || password.length() < 6) {
            showToast("密码长度不能小于6位!");
            return;
        }
        showLoading();

        String Md5Pasword = MD5Utils.encodeMD5(password);
        String finalPassword = MD5Utils.encodeMD5(Md5Pasword);


//        loginManager.register(edtPhone.getText().toString(), finalPassword, edtChecknum.getText().toString(), new ManagerCallback() {
//            @Override
//            public void onSuccess(Object returnContent, int requestCode) {
//                super.onSuccess(returnContent, requestCode);
//                if (requestCode == -1) {
//                    closeLoading();
//                    showToast("注册成功,请登陆");
//                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                closeLoading();
//                super.onFailure(msg);
//                showToast(msg);
//            }
//        });

        if (!Tool.isConnectingToInternet()) {
            closeLoading();
            showToast("无网络");
            return;
        }
        mRegisterTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mRegisterTaskId, new RegisterRequest(new RegisterResult(mContext), edtPhone.getText().toString(),
                edtChecknum.getText().toString(), finalPassword), this));

    }

    private void getChecknum() {
        String mobile = edtPhone.getText().toString();
        if (StringUtils.isEmpty(mobile)) {
            showToast("电话号码不能为空");
            return;
        }
        if (!StringUtils.isMobile(mobile)) {
            showToast("电话号码格式不对");
            return;
        }
        tvChecknum.setClickable(false);
        startCountTime(5 * 60 * 1000, 1000);
//        loginManager.getCheckNum(mobile, "RegisterCApp", requestCallback, 0);
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            stopCountTime();
            tvChecknum.setText("重新获取");
            tvChecknum.setClickable(true);
            return;
        }
        mGetVerifyCodeTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mGetVerifyCodeTaskId,
                new GetVerifyCodeRequest(new GetVerifyCodeResult(mContext), mobile, "RegisterCApp"), this));
    }

    class RequestCallback extends ManagerCallback<String> {
        @Override
        public void onSuccess(String returnContent, int requestCode) {
            super.onSuccess(returnContent, requestCode);
            //获取验证码成功
            if (requestCode == 0) {

            } else {

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

    //http

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mRegisterTaskId == id) {
            closeLoading();
            showToast("注册成功,请登陆");
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mGetVerifyCodeTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((GetVerifyCodeResult) result).getResp()));
            stopCountTime();
            tvChecknum.setText("重新获取");
            tvChecknum.setClickable(true);
        }
        if (mRegisterTaskId == id) {
            closeLoading();
            showToast(ToastUtil.formatToastText(mContext, ((RegisterResult) result).getResp()));
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
