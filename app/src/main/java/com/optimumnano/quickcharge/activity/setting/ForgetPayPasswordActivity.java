package com.optimumnano.quickcharge.activity.setting;

import android.os.Bundle;
import android.view.View;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

/**
 * Created by mfwn on 2017/4/8.
 */

public class ForgetPayPasswordActivity extends BaseActivity {
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
    }
}
