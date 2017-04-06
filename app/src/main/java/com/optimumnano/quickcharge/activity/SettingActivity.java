package com.optimumnano.quickcharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.views.MenuItem1;

/**
 * Created by mfwn on 2017/4/6.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private MenuItem1 modifyPassword;
    private MenuItem1 modifyPayPassword;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_modify_password:
                //startActivity(new Intent(this,ModifyPassword.class));
                break;

            case R.id.setting_modify_pay_password:

                break;

            default:
                break;
        }
    }
}
