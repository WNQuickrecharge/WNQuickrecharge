package com.optimumnano.quickcharge.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.optimumnano.quickcharge.BuildConfig;
import com.optimumnano.quickcharge.MyApplication;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.login.LoginActivity;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.request.LogoutRequest;
import com.optimumnano.quickcharge.response.LogoutResult;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.xutils.common.util.LogUtil;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_IS_REMEMBER;
import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_MOBILE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_COOKIE;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by mfwn on 2017/4/6.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener, HttpCallback {
    private MenuItem1 modifyPassword;
    private MenuItem1 modifyPayPassword;
    private MenuItem1 currentVersion;
    private Button logout;
    private ModifyUserInformationManager manager = new ModifyUserInformationManager();

    private int mLogoutTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskDispatcher.cancel(mLogoutTaskId);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("设置");
        setRightTitle("");
        modifyPassword= (MenuItem1) findViewById(R.id.setting_modify_password);
        modifyPayPassword= (MenuItem1) findViewById(R.id.setting_modify_pay_password);
        currentVersion= (MenuItem1) findViewById(R.id.tv_current_version);
        currentVersion.setRightText(BuildConfig.DEBUG?("debug "+BuildConfig.VERSION_NAME):BuildConfig.VERSION_NAME);
        logout= (Button) findViewById(R.id.logout);
        modifyPassword.setOnClickListener(this);
        modifyPayPassword.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_modify_password:
                startActivity(new Intent(this, ModifyPassword.class));
                break;

            case R.id.setting_modify_pay_password:
                startActivity(new Intent(this, ModifyPayPasswordActivity.class));
                break;
            case R.id.logout:
                LogUtil.i("Test==" + SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, false));
//                manager.logout(new Manager());
                if (!Tool.isConnectingToInternet()) {
                    showToast("无网络");
                    return;
                }
                mLogoutTaskId = TaskIdGenFactory.gen();
                mTaskDispatcher.dispatch(new HttpTask(mLogoutTaskId, new LogoutRequest(new LogoutResult(mContext)), this));
                break;
            default:
                break;
        }
    }

    class Manager extends ManagerCallback {
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            //String pwd = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_PASSWORD, "");
            String phone = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_MOBILE, "");
            boolean isRemember = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, false);
            SharedPreferencesUtil.getEditor(SP_USERINFO).clear().commit();
            SharedPreferencesUtil.getEditor(SP_COOKIE).clear().commit();
            showToast("您已退出登录");
            if (isRemember) {
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_MOBILE, phone);
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, true);
            } else {
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_MOBILE, "");
                SharedPreferencesUtil.putValue(SP_USERINFO, KEY_USERINFO_IS_REMEMBER, false);
            }
            AppManager.getAppManager().finishAllActivity();
            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            finish();
//            System.exit(0);

        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            showToast(msg);
        }
    }


    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        showToast(ToastUtil.formatToastText(mContext, ((LogoutResult) result).getResp()));

    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        showToast("您已退出登录");
        MyApplication.getuiflag = false;
        AppManager.getAppManager().finishAllActivity();
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onRequestCancel(int id) {

    }
}
