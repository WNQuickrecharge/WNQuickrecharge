package com.optimumnano.quickcharge.activity.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.OrderBean;

public class OrderlistDetailActivity extends BaseActivity {
    private OrderBean orderBean;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist_detail);
        getExtras();
        initViews();
    }
    private void getExtras(){
        orderBean = (OrderBean) getIntent().getExtras().getSerializable("orderbean");
        showToast(orderBean.status+"");
    }

    @Override
    public void initViews() {
        super.initViews();
    }
    private void initListener(){

    }
}
