package com.optimumnano.quickcharge.activity.invoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.dialog.PayDialog;
import com.optimumnano.quickcharge.views.MenuItem1;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayCenterActivity extends BaseActivity implements View.OnClickListener, PayDialog.PayCallback {
    @Bind(R.id.paycenter_miMoney)
    MenuItem1 miMoney;
    @Bind(R.id.paycenter_iv)
    ImageView iv;
    @Bind(R.id.paycenter_tvPayway)
    TextView tvPayway;
    @Bind(R.id.paycenter_tvNext)
    TextView tvNext;
    @Bind(R.id.paycenter_rlPayway)
    RelativeLayout rlPayway;

    private double allMoney;

    private PayDialog payDialog ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_center);
        ButterKnife.bind(this);
        getExtras();
        initViews();
    }
    private void getExtras() {
        allMoney = getIntent().getExtras().getDouble("money");
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitle("支付中心");

        miMoney.setRightText("￥"+allMoney);

        rlPayway.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paycenter_tvNext:
                pay();
                break;
            case R.id.paycenter_rlPayway:

                break;
        }
    }

    private void pay() {
        if (payDialog == null){
            payDialog = new PayDialog(this);
            payDialog.setMoney(allMoney,"order_no","sign");
            payDialog.setPayCallback(this);
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("money",allMoney);
        skipActivity(InvoiceApplyActivity.class,bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void paySuccess(String oder_no) {

    }

    @Override
    public void payFail(String msg) {

    }
}
