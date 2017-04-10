package com.optimumnano.quickcharge.activity.setting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.MyDialog;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_PAYPASSWORD;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by mfwn on 2017/4/8.
 */

public class ModifyPayPasswordActivity extends BaseActivity implements TextWatcher {
    private int inputPayPasswordStatus = 1;
    private static final int FIRST_INPUT_OLD_PAY_PASSWORD = 1;
    private static final int FIRST_INPUT_NEW_PAY_PASSWORD = 2;
    private static final int SECOND_INPUT_NEW_PAY_PASSWORD = 3;
    private String tempPayPassword;
    private ImageView[] imageViews;
    private ImageView oneImag, twoImag, threeImag, fourImag, fiveImag, sixImag;
    private MyDialog myDialog;
    private StringBuffer mStringBuffer;
    private ProgressDialog progressDialog;
    private TextView mTitleView;
    EditText mPayPassword;
    private String payPassword;
    private ModifyUserInformationManager manager = new ModifyUserInformationManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pay_password);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("修改支付密码");
        tvLeft.setVisibility(View.VISIBLE);
        mPayPassword = (EditText) findViewById(R.id.et_pay_password);
        mTitleView = (TextView) findViewById(R.id.tv_title_change_pay_password);
        oneImag = (ImageView) findViewById(R.id.iv_one_img);
        twoImag = (ImageView) findViewById(R.id.iv_two_img);
        threeImag = (ImageView) findViewById(R.id.iv_three_img);
        fourImag = (ImageView) findViewById(R.id.iv_four_img);
        fiveImag = (ImageView) findViewById(R.id.iv_five_img);
        sixImag = (ImageView) findViewById(R.id.iv_six_img);
        myDialog = new MyDialog(this, R.style.MyDialog);
        progressDialog = new ProgressDialog(this);
        myDialog.setCancelable(false);
        EventBus.getDefault().register(this);
        mStringBuffer = new StringBuffer();
        mPayPassword.addTextChangedListener(this);
        mPayPassword.setOnKeyListener(keyListener);
        imageViews = new ImageView[]{oneImag, twoImag, threeImag, fourImag, fiveImag, sixImag};


    }

    View.OnKeyListener keyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_DEL
                    && event.getAction() == KeyEvent.ACTION_UP) {
                delTextValue();
                return true;
            }
            return false;
        }
    };

    private void setTextValue() {

        String str = mStringBuffer.toString();
        int len = str.length();

        if (len <= 6) {
            imageViews[len - 1].setVisibility(View.VISIBLE);
        }
        if ((len == 6) && (inputPayPasswordStatus == FIRST_INPUT_OLD_PAY_PASSWORD)) {
            String Md5Paypassword = MD5Utils.encodeMD5(str);
            showLoading();
            String payPassword = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_PAYPASSWORD, "");
            if (!Md5Paypassword.equals(payPassword)) {

                EventBus.getDefault().post(new EventManager.onInPutWrongOldPayPassword());

            } else {
                hideLoading();
                inputPayPasswordStatus = FIRST_INPUT_NEW_PAY_PASSWORD;
                EventBus.getDefault().post(new EventManager.onInputNewPayPassword(1));
            }

        } else if ((len == 6) && (inputPayPasswordStatus == FIRST_INPUT_NEW_PAY_PASSWORD)) {
            tempPayPassword = mStringBuffer.toString();
            EventBus.getDefault().post(new EventManager.onInputNewPayPassword(2));
        } else if ((len == 6) && (inputPayPasswordStatus == SECOND_INPUT_NEW_PAY_PASSWORD)) {
            String confirmPayPassword = mStringBuffer.toString();

            if (tempPayPassword.equals(confirmPayPassword)) {
                //两次密码相同提交服务器修改支付密码
                //showToast("提交服务器修改支付密码");
                showLoading();
                manager.modifyPayPassword(confirmPayPassword, new Manager());
                finish();
            } else {
                myDialog.setTitle("提醒");
                myDialog.setMessage("两次输入密码不一致，请重新输入");
                myDialog.setYesOnclickListener("重新输入", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        EventBus.getDefault().post(new EventManager.onInputNewPayPassword(1));
                        myDialog.dismiss();
                    }
                });
                myDialog.setNoOnclickListener("知道了", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        myDialog.dismiss();
                        EventBus.getDefault().post(new EventManager.onInputNewPayPassword(1));
                    }
                });

                myDialog.show();
            }
        }
    }

    private void delTextValue() {
        String str = mStringBuffer.toString();
        int len = str.length();
        if (len == 0) {
            return;
        }
        if (len > 0 && len <= 6) {
            mStringBuffer.delete(len - 1, len);
        }

        imageViews[len - 1].setVisibility(View.INVISIBLE);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            return;
        }
        if (s.length() <= 6 && mStringBuffer.toString().length() < 6) {
            mStringBuffer.append(s.toString());
            setTextValue();
            //showShortToast(mStringBuffer.toString());
        }
        s.delete(0, s.length());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayPasswordWrong(EventManager.onInPutWrongOldPayPassword event) {

        hideLoading();
        myDialog.setTitle("支付提醒");
        myDialog.setMessage("密码输入错误,请重试!");
        myDialog.setYesOnclickListener("重新输入", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                EventBus.getDefault().post(new EventManager.onRetryInputOldPayPassword());
                inputPayPasswordStatus = FIRST_INPUT_OLD_PAY_PASSWORD;
                myDialog.dismiss();
            }
        });
        myDialog.setNoOnclickListener("忘记密码", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                startActivity(new Intent(ModifyPayPasswordActivity.this,ForgetPayPasswordActivity.class));
                myDialog.dismiss();

            }
        });
        myDialog.show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetryInputOldPayPassword(EventManager.onRetryInputOldPayPassword event) {
        clearTheInputPayPassword();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInputNewPayPassword(EventManager.onInputNewPayPassword event) {
        clearTheInputPayPassword();
        switch (event.inputStatus) {
            case 1:
                mTitleView.setText("请输入新支付密码");
                inputPayPasswordStatus = FIRST_INPUT_NEW_PAY_PASSWORD;
                break;
            case 2:
                mTitleView.setText("请再次输入新支付密码");
                inputPayPasswordStatus = SECOND_INPUT_NEW_PAY_PASSWORD;
                break;

            default:

                break;
        }
    }

    private void clearTheInputPayPassword() {
        mStringBuffer.delete(0, mStringBuffer.length());

        for (ImageView i : imageViews) {
            i.setVisibility(View.INVISIBLE);
        }
    }

    class Manager extends ManagerCallback {
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            hideLoading();
            showToast("支付密码修改成功!");
            finish();
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            hideLoading();
            showToast(msg);
        }
    }

    class PayPasswordManager extends ManagerCallback {
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            JSONObject dataJson = null;
            try {
                dataJson = new JSONObject((String) returnContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String paypwd = dataJson.optString("paypwd");
            if (paypwd == null || paypwd.equals("")) {//支付密码为空时,先设定支付密码
                new AlertDialog.Builder(ModifyPayPasswordActivity.this)
                        .setTitle("支付密码为空")
                        .setMessage("请输入初始支付密码")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(new EventManager.onInputNewPayPassword(1));
                            }
                        })
                        .show();

            }else {
                SharedPreferencesUtil.putValue(SP_USERINFO,KEY_USERINFO_PAYPASSWORD,paypwd);
            }
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            showToast(msg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.getPayPassword(new PayPasswordManager());
    }
}
