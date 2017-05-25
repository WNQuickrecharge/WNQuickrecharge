package com.optimumnano.quickcharge.activity.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.views.MenuItem1;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 支付成功界面，按完成跳转到InvoiceActivity界面
 */
public class InvoiceApplyActivity extends BaseActivity {
    private TextView tvFinish;

    /**
     * 单号
     */
    private MenuItem1 miOrderno;
    /**
     *支付金额
     */
    private MenuItem1 miMoney;
    /**
     *支付时间
     */
    private MenuItem1 time;

    private double allMoney;
    private String order_no;
    private String timeStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_apply);
        getExtras();
        initViews();
    }

    private void getExtras() {
        allMoney = getIntent().getExtras().getDouble("money");
        order_no = getIntent().getExtras().getString("order_no");
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeStr = sdf.format(d);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("开票申请");
        time = (MenuItem1) findViewById(R.id.time_for_pay_postage);
        tvFinish = (TextView) findViewById(R.id.invoice_apply_tvNext);
        miOrderno = (MenuItem1) findViewById(R.id.invoice_apply_miOrderno);
        miMoney = (MenuItem1) findViewById(R.id.invoice_apply_miMoney);


        miOrderno.setRightText(order_no + "");
        miMoney.setRightText(allMoney + "");
        time.setRightText(timeStr+"");

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InvoiceApplyActivity.this, InvoiceActivity.class);
                startActivity(intent);
                InvoiceApplyActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
