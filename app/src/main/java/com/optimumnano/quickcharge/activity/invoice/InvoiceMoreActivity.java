package com.optimumnano.quickcharge.activity.invoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InvoiceMoreActivity extends BaseActivity implements View.OnClickListener {


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

    private String regPhone, regAddress, bankCard, indentifyNum, remark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        initViews();
        setTitle(getString(R.string.more_message));
        tvSubmit.setOnClickListener(this);
    }


    public static void start(Activity context) {
        Intent intent = new Intent(context, InvoiceMoreActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    //只要填了一个，其余信息必须全填
    private boolean check() {
        regPhone = etBankNumber.getText().toString();
        regAddress = etRegisterAddress.getText().toString();
        bankCard = etCompanyRisa.getText().toString();
        indentifyNum = etTaxpayerNumber.getText().toString();
        remark = etRemark.getText().toString();
        //全为空
        if (StringUtils.isEmpty(regPhone) && StringUtils.isEmpty(regAddress) && StringUtils.isEmpty(bankCard)
                && StringUtils.isEmpty(indentifyNum) && StringUtils.isEmpty(remark)) {
            return true;
        } else if (!StringUtils.isEmpty(regPhone) && !StringUtils.isEmpty(regAddress) && !StringUtils.isEmpty(bankCard)
                && !StringUtils.isEmpty(indentifyNum) && !StringUtils.isEmpty(remark)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (check()) {
            Intent intent = new Intent();
            intent.putExtra("regPhone", regPhone);
            intent.putExtra("regAddress", regAddress);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("indentifyNum", indentifyNum);
            intent.putExtra("remark", remark);
            setResult(100, intent);
            finish();
        } else {
            showToast("请完善信息");
        }
    }
}
