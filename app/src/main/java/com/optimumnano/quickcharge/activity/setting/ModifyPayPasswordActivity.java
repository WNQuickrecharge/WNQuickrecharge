package com.optimumnano.quickcharge.activity.setting;

import android.app.AlertDialog;
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
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.request.ChangePayPwdRequest;
import com.optimumnano.quickcharge.request.GetPayPwdRequest;
import com.optimumnano.quickcharge.response.ChangePayPwdResult;
import com.optimumnano.quickcharge.response.GetPayPwdResult;
import com.optimumnano.quickcharge.utils.MD5Utils;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mfwn on 2017/4/8.
 */

public class ModifyPayPasswordActivity extends BaseActivity implements TextWatcher, HttpCallback {
    private int inputPayPasswordStatus = 1;
    private static final int FIRST_INPUT_OLD_PAY_PASSWORD = 1;
    private static final int FIRST_INPUT_NEW_PAY_PASSWORD = 2;
    private static final int SECOND_INPUT_NEW_PAY_PASSWORD = 3;
    private String tempPayPassword;
    private ImageView[] imageViews;
    private ImageView oneImag, twoImag, threeImag, fourImag, fiveImag, sixImag;
    private MyDialog myDialog;
    private StringBuffer mStringBuffer;
    private TextView mTitleView;
    EditText mPayPassword;
    private String payPassword;
    private ModifyUserInformationManager manager = new ModifyUserInformationManager();
    private boolean payPasswordIsNUll;

    private int mGetPayPwdTaskId;
    private int mAnotherGetPayPwdTaskId;
    private String mModifyPwd;
    private int mChangePayPwdTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pay_password);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payPasswordIsNUll = bundle.getBoolean("PayPasswordIsNUll");
        }

        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        if (payPasswordIsNUll == true) {
            inputPayPasswordStatus = FIRST_INPUT_NEW_PAY_PASSWORD;
            setTitle("设置支付密码");
        } else {
            setTitle("修改支付密码");
        }
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

        final String str = mStringBuffer.toString();
        mModifyPwd = str;
        int len = str.length();

        if (len <= 6) {
            imageViews[len - 1].setVisibility(View.VISIBLE);
        }
        if ((len == 6) && (inputPayPasswordStatus == FIRST_INPUT_OLD_PAY_PASSWORD)) {
            showLoading();
//            GetMineInfoManager.getPayPwd(new ManagerCallback() {
//                @Override
//                public void onSuccess(Object returnContent) {
//                    super.onSuccess(returnContent);
//                    closeLoading();
//                    JSONObject dataJson = null;
//                    try {
//                        dataJson = new JSONObject(returnContent.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    String paypwd = dataJson.optString("paypwd");
//                    String Md5Paypassword = MD5Utils.encodeMD5(str.toString());
//                    String finalPayPassword = MD5Utils.encodeMD5(Md5Paypassword);
//                    if (!finalPayPassword.equals(paypwd)) {
//                        EventBus.getDefault().post(new EventManager.onInPutWrongOldPayPassword());
//                    } else {
//                        inputPayPasswordStatus = FIRST_INPUT_NEW_PAY_PASSWORD;
//                        EventBus.getDefault().post(new EventManager.onInputNewPayPassword(1));
//                    }
//
//                }
//
//                @Override
//                public void onFailure(String msg) {
//                    closeLoading();
//                    showToast(msg);
//                }
//            });


            if (!Tool.isConnectingToInternet()) {
                showToast("无网络");
                closeLoading();
                return;
            }
            mGetPayPwdTaskId = TaskIdGenFactory.gen();
            mTaskDispatcher.dispatch(new HttpTask(mGetPayPwdTaskId,
                    new GetPayPwdRequest(new GetPayPwdResult(mContext)), this));

        } else if ((len == 6) && (inputPayPasswordStatus == FIRST_INPUT_NEW_PAY_PASSWORD)) {
            tempPayPassword = mStringBuffer.toString();
            EventBus.getDefault().post(new EventManager.onInputNewPayPassword(2));
        } else if ((len == 6) && (inputPayPasswordStatus == SECOND_INPUT_NEW_PAY_PASSWORD)) {
            String confirmPayPassword = mStringBuffer.toString();

            if (tempPayPassword.equals(confirmPayPassword)) {
                //两次密码相同提交服务器修改支付密码
                //showToast("提交服务器修改支付密码");
                showLoading();
                String md5PayPassword = MD5Utils.encodeMD5(confirmPayPassword);
                String finalPayPassword = MD5Utils.encodeMD5(md5PayPassword);
                if (!Tool.isConnectingToInternet()) {
                    showToast("无网络");
                    closeLoading();
                    return;
                }
//                manager.modifyPayPassword(finalPayPassword, new Manager());
//                finish();
                //TODO
                mChangePayPwdTaskId = TaskIdGenFactory.gen();
                mTaskDispatcher.dispatch(new HttpTask(mChangePayPwdTaskId,
                        new ChangePayPwdRequest(new ChangePayPwdResult(mContext), finalPayPassword), this));
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayPasswordWrong(EventManager.onInPutWrongOldPayPassword event) {

        closeLoading();
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
                startActivity(new Intent(ModifyPayPasswordActivity.this, ForgetPayPasswordActivity.class));
                myDialog.dismiss();
                finish();

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
            closeLoading();
            showToast("支付密码修改成功!");
            finish();
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            closeLoading();
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
//        manager.getPayPassword(new PayPasswordManager());
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
            return;
        }
        mAnotherGetPayPwdTaskId = TaskIdGenFactory.gen();
        mTaskDispatcher.dispatch(new HttpTask(mAnotherGetPayPwdTaskId,
                new GetPayPwdRequest(new GetPayPwdResult(mContext)), this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mTaskDispatcher.cancel(mGetPayPwdTaskId);
        mTaskDispatcher.cancel(mAnotherGetPayPwdTaskId);
        mTaskDispatcher.cancel(mChangePayPwdTaskId);
    }

    //http


    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mAnotherGetPayPwdTaskId == id) {
            showToast(ToastUtil.formatToastText(mContext, ((GetPayPwdResult) result).getResp()));
        } else if (mGetPayPwdTaskId == id) {
            closeLoading();
            showToast(ToastUtil.formatToastText(mContext, ((GetPayPwdResult) result).getResp()));
        } else if (mChangePayPwdTaskId == id) {
            closeLoading();
            showToast(ToastUtil.formatToastText(mContext, ((ChangePayPwdResult) result).getResp()));
        }
    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        if (mAnotherGetPayPwdTaskId == id) {
            String paypwd = ((GetPayPwdResult) result).getResp().getResult().getPaypwd();
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

            }
        } else if (mGetPayPwdTaskId == id) {
            closeLoading();
            String paypwd = ((GetPayPwdResult) result).getResp().getResult().getPaypwd();
            String Md5Paypassword = MD5Utils.encodeMD5(mModifyPwd.toString());
            String finalPayPassword = MD5Utils.encodeMD5(Md5Paypassword);
            if (!finalPayPassword.equals(paypwd)) {
                EventBus.getDefault().post(new EventManager.onInPutWrongOldPayPassword());
            } else {
                inputPayPasswordStatus = FIRST_INPUT_NEW_PAY_PASSWORD;
                EventBus.getDefault().post(new EventManager.onInputNewPayPassword(1));
            }
        } else if (mChangePayPwdTaskId == id) {
            closeLoading();
            showToast("支付密码修改成功!");
            finish();
        }
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
