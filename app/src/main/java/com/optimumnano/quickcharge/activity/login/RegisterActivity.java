package com.optimumnano.quickcharge.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

import static com.optimumnano.quickcharge.R.id.register_edtConfirmPwd;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText edtPhone,edtChecknum,edtPwd,edtConfirmPwd;
    private TextView tvReg,tvChecknum;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        edtPhone = (EditText) findViewById(R.id.register_edtPhone);
        edtChecknum = (EditText) findViewById(R.id.register_edtChecknum);
        edtPwd = (EditText) findViewById(R.id.register_edtPwd);
        edtConfirmPwd = (EditText) findViewById(register_edtConfirmPwd);
        tvChecknum = (TextView) findViewById(R.id.register_tvChecknum);
        tvReg = (TextView) findViewById(R.id.register_tvRegister);
    }
    public void initListener(){
        tvReg.setOnClickListener(this);
        tvChecknum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_tvChecknum:

                break;
            case R.id.register_tvRegister:

                break;
            default:
                break;
        }
    }
}
