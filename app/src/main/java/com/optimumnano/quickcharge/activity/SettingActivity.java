package com.optimumnano.quickcharge.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.AppManager;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.util.List;

import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * Created by mfwn on 2017/4/6.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private MenuItem1 modifyPassword;
    private MenuItem1 modifyPayPassword;
    private Button logout;
    private ModifyUserInformationManager manager=new ModifyUserInformationManager();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("设置");
        setRightTitle("");
        modifyPassword= (MenuItem1) findViewById(R.id.setting_modify_password);
        modifyPayPassword= (MenuItem1) findViewById(R.id.setting_modify_pay_password);
        logout= (Button) findViewById(R.id.logout);
        modifyPassword.setOnClickListener(this);
        modifyPayPassword.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_modify_password:
                startActivity(new Intent(this,ModifyPassword.class));
                break;

            case R.id.setting_modify_pay_password:

                break;
            case R.id.logout:
                manager.logout(new Manager());
                break;
            default:
                break;
        }
    }

    class Manager extends ManagerCallback {
        @Override
        public void onSuccess(Object returnContent) {
            super.onSuccess(returnContent);
            SharedPreferencesUtil.getEditor(SP_USERINFO).clear().commit();
            showToast("您已退出登录");
            AppManager.getAppManager().finishAllActivity();
            finish();

        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            showToast(msg);
        }
    }

}
