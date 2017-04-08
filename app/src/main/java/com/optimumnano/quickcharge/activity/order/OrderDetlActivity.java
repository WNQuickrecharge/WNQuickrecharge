package com.optimumnano.quickcharge.activity.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

public class OrderDetlActivity extends BaseActivity {
    private TextView tvConfirm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detl);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("订单详情");
    }
}
