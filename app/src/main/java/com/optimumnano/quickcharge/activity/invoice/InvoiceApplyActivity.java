package com.optimumnano.quickcharge.activity.invoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

import butterknife.ButterKnife;

public class InvoiceApplyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_apply);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("开票申请");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
