package com.optimumnano.quickcharge.activity.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.AddInvoiceOrderHttpResp;
import com.optimumnano.quickcharge.http.BaseResult;
import com.optimumnano.quickcharge.http.HttpCallback;
import com.optimumnano.quickcharge.http.HttpTask;
import com.optimumnano.quickcharge.http.TaskIdGenFactory;
import com.optimumnano.quickcharge.request.AddInvoiceOrderRequest;
import com.optimumnano.quickcharge.response.AddInvoiceOrderResult;
import com.optimumnano.quickcharge.utils.ToastUtil;
import com.optimumnano.quickcharge.utils.Tool;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发票类型页面，目前只支持纸质发票。
 */

public class InvoiceTypeActivity extends BaseActivity implements View.OnClickListener, HttpCallback {
    @Bind(R.id.invoice_type_llPaper)
    LinearLayout llPaper;
    @Bind(R.id.invoice_type_llEmail)
    LinearLayout llEmail;
    @Bind(R.id.invoice_type_rg)
    RadioGroup rg;
    /**
     * 发票金额
     */
    @Bind(R.id.invoice_type_tvMoney)
    TextView tvMoney;
    @Bind(R.id.invoice_type_rbEle)
    RadioButton rbEle;
    /**
     * 下一步
     */
    @Bind(R.id.invoice_type_tvNext)
    TextView tvNext;
    /**
     * 公司抬头
     */
    @Bind(R.id.et_company_risa)
    EditText etCompanyRisa;
    /**
     * 充电费用
     */
    @Bind(R.id.et_battery_charging_price)
    TextView etBatteryChargingPrice;
    /**
     * 更多信息栏
     */
    @Bind(R.id.ll_go_to_more)
    RelativeLayout llGoToMore;
    /**
     * 电子邮箱，开电子发票时启用
     */
    @Bind(R.id.et_email)
    EditText etEmail;
    /**
     *
     */
    @Bind(R.id.activity_invoice_type)
    LinearLayout activityInvoiceType;
    /**
     * 收件人姓名
     */
    @Bind(R.id.invoice_type_etName)
    EditText etName;
    /**
     * 收件人手机号码
     */
    @Bind(R.id.invoice_type_etPhone)
    EditText etPhone;
    /**
     * 详细地址
     */
    @Bind(R.id.invoice_type_etAddress)
    EditText etDetailAddress;
    /**
     * 大地址，省市县
     */
    @Bind(R.id.invoice_type_etArea)
    TextView tvPriviceAddress;
    /**
     * 邮费提示
     */
    @Bind(R.id.money_tips)
    TextView tvMoneyTips;

    private double allMoney;//发票金额
    private String ids;//所有的订单id
    private double orderMoney = 0;//订单金额
    /**
     * 收件人姓名
     */
    private String nameStr;
    /**
     * 收件人电话
     */
    private String mobilePhoneStr;
    /**
     * 收件人大地址，省市县
     */
    private String bigAddressStr;
    /**
     * 收件人详细地址，
     */
    private String detailAddressstr;
    /**
     * 更多信息内容分别为：
     * 注册电话，注册地址，开户银行及账号，纳税人识别号，备注说明
     */
    private String regPhone, regAddress, bankCard, indentifyNum, remark;
    private String moneyTipsStr = "温馨提示：开票金额不足<font color='#FF0000'>￥500</font>元，需支付邮费。";
    /**
     * 拼接的地址
     */
    private String endAddressStr;
    private int mAddInvoiceOrderTaskId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_type);
        ButterKnife.bind(this);
        getExtras();
        initViews();
    }

    private void getExtras() {
        ids = getIntent().getExtras().getString("ids");
        allMoney = getIntent().getExtras().getDouble("money");
        //暂定高于500没有邮费
        if (allMoney < 500) {
            orderMoney = 10;
        }

    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("纸质发票");
//        setRightTitle("发票记录");
//        rbEle.setChecked(true);

        tvMoney.setText("￥" + allMoney);
        tvMoneyTips.setText(Html.fromHtml(moneyTipsStr));
        tvNext.setOnClickListener(this);
        llGoToMore.setOnClickListener(this);
        tvPriviceAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 520为识别码，与AdressChoiceActivity里一致
                 */
                Intent intent = new Intent();
                intent.setClass(InvoiceTypeActivity.this, AdressChoiceActivity.class);
                startActivityForResult(intent, 520);
            }
        });
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
    public void onClick(View v) {
        nameStr = etName.getText().toString().trim();
        mobilePhoneStr = etPhone.getText().toString().trim();
        bigAddressStr = tvPriviceAddress.getText().toString().trim();
        detailAddressstr = etDetailAddress.getText().toString().trim();
        switch (v.getId()) {
            case R.id.invoice_type_tvNext:
                if ("".equals(nameStr)) {
                    showToast("收件人姓名不能为空");
                    return;
                }
                if ("".equals(mobilePhoneStr)) {
                    showToast("收件人电话不能为空");
                    return;
                }
                if ("".equals(bigAddressStr)) {
                    showToast("请选择正确地址");
                    return;
                }
                if ("".equals(detailAddressstr)) {
                    showToast("请填写详细地址");
                    return;
                }
                if (!"".equals(nameStr) && !"".equals(mobilePhoneStr) && !"".equals(bigAddressStr) && !"".equals(detailAddressstr)) {

                    addInviceOrder();
                }
                break;
            case R.id.ll_go_to_more:
                InvoiceMoreActivity.start(this);
                break;
        }

    }

    /**
     * 提交开发票订单服务
     * 必填字段
     * consume_ids  消费ID
     title  公司抬头
     invoice_amount  发票金额
     name  收件人姓名
     address  地址
     mobile  手机号码

     *
     */
    private void addInviceOrder() {
        if (!Tool.isConnectingToInternet()) {
            showToast("无网络");
        } else {
            mAddInvoiceOrderTaskId = TaskIdGenFactory.gen();
            mTaskDispatcher.dispatch(new HttpTask(mAddInvoiceOrderTaskId,
                    new AddInvoiceOrderRequest(new AddInvoiceOrderResult(mContext),
                            ids.substring(0,ids.length()-1),
                            etCompanyRisa.getText().toString(),
                            allMoney,
                            nameStr,
                            bigAddressStr + detailAddressstr,
                            mobilePhoneStr,
                            regPhone, regAddress, bankCard, indentifyNum, remark), this));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!TextUtils.isEmpty(regPhone)) {
            regPhone = data.getStringExtra("regPhone");
        }
        if (!TextUtils.isEmpty(regAddress)) {
            regAddress = data.getStringExtra("regAddress");
        }
        if (!TextUtils.isEmpty(bankCard)) {
            bankCard = data.getStringExtra("bankCard");
        }
        if (!TextUtils.isEmpty(indentifyNum)) {
            indentifyNum = data.getStringExtra("indentifyNum");
        }
        if (!TextUtils.isEmpty(remark)) {
            remark = data.getStringExtra("remark");
        }
        if (requestCode == 520 && null != data) {
            endAddressStr = data.getStringExtra("address");
            if ("".equals(endAddressStr)) {
                tvPriviceAddress.setHint("选择所在的省份/直辖市");
            } else {
                tvPriviceAddress.setText(endAddressStr);
            }
        } else {
            tvPriviceAddress.setHint("选择所在的省份/直辖市");
        }
    }

    @Override
    public void onRequestSuccess(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        AddInvoiceOrderHttpResp resp = ((AddInvoiceOrderResult) result).getResp();
        Bundle bundle = new Bundle();
        bundle.putDouble("money", resp.getResult().postage);
        bundle.putDouble("allmoney", allMoney);
        bundle.putString("order_no", resp.getResult().i_order_no);
        skipActivity(PayCenterActivity.class, bundle);
        InvoiceTypeActivity.this.finish();
    }

    @Override
    public void onRequestFail(int id, BaseResult result) {
        if (isFinishing()) {
            return;
        }
        showToast(ToastUtil.formatToastText(mContext, ((AddInvoiceOrderResult) result).getResp()));

    }

    @Override
    public void onRequestCancel(int id) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mTaskDispatcher.cancel(mAddInvoiceOrderTaskId);
    }
}
