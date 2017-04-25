package com.optimumnano.quickcharge.activity.invoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoiceMoreActivity extends BaseActivity {


    private static int REQUEST_CODE = 1110;

    @Bind(R.id.et_taxpayer_number)
    EditText etTaxpayerNumber;
    @Bind(R.id.et_register_address)
    EditText etRegisterAddress;
    @Bind(R.id.et_bank_number)
    EditText etBankNumber;
    @Bind(R.id.et_company_risa)
    EditText etCompanyRisa;
    @Bind(R.id.et_remark)
    EditText etRemark;
    @Bind(R.id.ll_top)
    LinearLayout llTop;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.activity_invoice)
    RelativeLayout activityInvoice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        initViews();
        setTitle(getString(R.string.more_message));
    }


    public static void start(Activity context) {
        Intent intent = new Intent(context, InvoiceMoreActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE);
    }
}
