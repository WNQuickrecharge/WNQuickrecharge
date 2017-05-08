package com.optimumnano.quickcharge.activity.invoice;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.InvoiceOrderRsp;
import com.optimumnano.quickcharge.manager.InvoiceManager;
import com.optimumnano.quickcharge.net.ManagerCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoiceTypeActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.invoice_type_llPaper)
    LinearLayout llPaper;
    @Bind(R.id.invoice_type_llEmail)
    LinearLayout llEmail;
    @Bind(R.id.invoice_type_rg)
    RadioGroup rg;
    @Bind(R.id.invoice_type_tvMoney)
    TextView tvMoney;
    @Bind(R.id.invoice_type_rbEle)
    RadioButton rbEle;
    @Bind(R.id.invoice_type_tvNext)
    TextView tvNext;
    @Bind(R.id.et_company_risa)
    EditText etCompanyRisa;
    @Bind(R.id.et_battery_charging_price)
    EditText etBatteryChargingPrice;
    @Bind(R.id.ll_go_to_more)
    RelativeLayout llGoToMore;
    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.activity_invoice_type)
    LinearLayout activityInvoiceType;
    @Bind(R.id.invoice_type_etName)
    EditText etName;
    @Bind(R.id.invoice_type_etPhone)
    EditText etPhone;
    @Bind(R.id.invoice_type_etAddress)
    EditText etAddress;

    private double allMoney;//发票金额
    private String ids;//所有的订单id
    private double orderMoney = 0;//订单金额
    private InvoiceManager manager = new InvoiceManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_type);
        ButterKnife.bind(this);
        getExtras();
        initViews();
    }

    private void getExtras() {
        allMoney = getIntent().getExtras().getDouble("money");
        ids = getIntent().getExtras().getString("ids");
        //暂定高于500没有邮费
        if (allMoney<500){
            orderMoney = 10;
        }

    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("发票类型");
        setRightTitle("发票记录");
        rbEle.setChecked(true);

        tvMoney.setText("￥" + allMoney);

        tvNext.setOnClickListener(this);
        llGoToMore.setOnClickListener(this);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.invoice_type_rbEle:
                        llEmail.setVisibility(View.VISIBLE);
                        llPaper.setVisibility(View.GONE);
                        break;
                    case R.id.invoice_type_rbPaper:
                        llEmail.setVisibility(View.GONE);
                        llPaper.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invoice_type_tvNext:
                addInviceOrder();
                break;
            case R.id.ll_go_to_more:
                InvoiceMoreActivity.start(this);
                break;
        }

    }
    //提交订单
    private void addInviceOrder(){
        manager.addInvoiceOrder(orderMoney, ids, etCompanyRisa.getText().toString(), allMoney,
                etName.getText().toString(), etAddress.getText().toString(),
                etPhone.getText().toString(), new ManagerCallback<InvoiceOrderRsp>() {
                    @Override
                    public void onSuccess(InvoiceOrderRsp returnContent) {
                        super.onSuccess(returnContent);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("money", returnContent.postage);
                        bundle.putDouble("allmoney",allMoney);
                        bundle.putString("order_no",returnContent.i_order_no);
                        skipActivity(PayCenterActivity.class, bundle);
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                        showToast(msg);
                    }
                });
    }
}
