package com.optimumnano.quickcharge.activity.setting;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.LoginManager;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;

/**
 * Created by mfwn on 2017/4/8.
 */

public class ForgetPayPasswordActivity extends BaseActivity implements View.OnClickListener{
    private EditText verificationCode,payPassword,confirmPaypassword;
    private TextView button,tv_code,mobile;
    private LoginManager manager=new LoginManager();
    private ModifyUserInformationManager modifyUserInformationManager = new ModifyUserInformationManager();
    private ShortMessageCountDownTimer smcCountDownTimer;
    private RequestCallback requestCallback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pay_password);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("忘记支付密码");
        tvLeft.setVisibility(View.VISIBLE);
        mobile= (TextView) findViewById(R.id.forget_pay_password_edtPhone);
        mobile.setText(SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO,SPConstant.KEY_USERINFO_MOBILE,""));
        verificationCode= (EditText) findViewById(R.id.forget_pay_edtChecknum);
        payPassword= (EditText) findViewById(R.id.forget_pay_edtPwd);
        confirmPaypassword= (EditText) findViewById(R.id.forget_pay_edtConfirmPwd);
        button= (TextView) findViewById(R.id.forget_pay_password_confirm);
        tv_code= (TextView) findViewById(R.id.forget_pay_tvChecknum);
        button.setOnClickListener(this);
        tv_code.setOnClickListener(this);
        requestCallback = new RequestCallback();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_pay_password_confirm:
                forgetPassword();
                break;

            case R.id.forget_pay_tvChecknum:
                getChecknum();
                break;

            default:
                break;
        }
    }
    private void getChecknum() {
        String phone = mobile.getText().toString();
        if (StringUtils.isEmpty(phone)){
            showToast("电话号码不能为空");
            return;
        }
        if (!StringUtils.isMobile(phone)){
            showToast("电话号码格式不对");
            return;
        }
        tv_code.setClickable(false);
        startCountTime(5*60*1000,1000);
        manager.getCheckNum(phone,"ForgetPayPwdCApp",requestCallback,0);
    }

    private void forgetPassword() {
        String phone = mobile.getText().toString();
        if (StringUtils.isEmpty(phone)){
            showToast("电话号码不能为空");
            return;
        }
        if (!StringUtils.isMobile(phone)){
            showToast("电话号码格式不对");
            return;
        }
        if (TextUtils.isEmpty(payPassword.getText().toString())||TextUtils.isEmpty(confirmPaypassword.getText().toString())){
            showToast("密码不能为空");
            return;
        }
        if (!TextUtils.equals(payPassword.getText().toString(),confirmPaypassword.getText().toString())){
            showToast("两次密码不一致,请重输入");
            return;
        }
        if (payPassword.getText().toString().length()<6||confirmPaypassword.getText().toString().length()<6){
            showToast("请输入6位支付密码");
            return;
        }
        if (TextUtils.isEmpty(verificationCode.getText().toString())){
            showToast("请输入验证码");
            return;
        }
        String newPassword = confirmPaypassword.getText().toString();
        String Md5Password = MD5Utils.encodeMD5(newPassword);
        String finalPaypassword = MD5Utils.encodeMD5(Md5Password);
        modifyUserInformationManager.forgetPayPassword(phone,"ForgetPayPwdCApp",verificationCode.getText().toString(),finalPaypassword,new Manager());
    }

    private void startCountTime(long allTime, long time) {
        stopCountTime();
        smcCountDownTimer = new ForgetPayPasswordActivity.ShortMessageCountDownTimer(allTime, time);
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
            tv_code.setText(millisInFuture / 1000 + " S");
        }

        @Override
        public void onTick(long arg0) {
            tv_code.setText(arg0 / 1000 + " S");

        }

        @Override
        public void onFinish() {
            tv_code.setText("重新获取");
            tv_code.setClickable(true);
        }

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
            tv_code.setText("重新获取");
            tv_code.setClickable(true);
        }
    }
    class Manager extends ManagerCallback{
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            showToast("支付密码修改成功!");
            finish();
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            showToast(msg);
        }
    }
}
