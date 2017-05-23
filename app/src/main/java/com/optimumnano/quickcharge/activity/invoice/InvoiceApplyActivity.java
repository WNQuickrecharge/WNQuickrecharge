package com.optimumnano.quickcharge.activity.invoice;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.views.MenuItem1;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoiceApplyActivity extends BaseActivity {
    @Bind(R.id.invoice_apply_tvNext)
    TextView tvFinish;
    @Bind(R.id.invoice_apply_miOrderno)
    MenuItem1 miOrderno;
    @Bind(R.id.invoice_apply_miMoney)
    MenuItem1 miMoney;

    private double allMoney;
    private String order_no;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_apply);
        ButterKnife.bind(this);
        getExtras();
        initViews();
    }

    private void getExtras() {
        allMoney = getIntent().getExtras().getDouble("money");
        order_no = getIntent().getExtras().getString("order_no");
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("开票申请");

        miOrderno.setRightText(order_no+"");
        miMoney.setRightText(allMoney+"");

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
