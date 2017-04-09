package com.optimumnano.quickcharge.activity.order;

import android.os.Bundle;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

public class OrderActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("充电订单");
    }
}
