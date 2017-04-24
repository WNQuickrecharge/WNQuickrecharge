package com.optimumnano.quickcharge.activity.mineinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.BillBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：邓传亮 on 2017/4/7 15:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class WalletBillDetailAct extends BaseActivity {

    @Bind(R.id.act_bill_detail_tv_amount)
    TextView mAmount;
    @Bind(R.id.act_bill_detail_tv_type)
    TextView mType;
    @Bind(R.id.act_bill_detail_tv_time)
    TextView mTime;
    @Bind(R.id.act_bill_detail_tv_busNum)
    TextView mBusNum;
    @Bind(R.id.act_bill_detail_tv_advanceamount)
    TextView mAdvanceAmount;
    @Bind(R.id.act_bill_detail_tv_backamount)
    TextView mBackamount;
    @Bind(R.id.act_bill_detail_tv_payway)
    TextView mPayway;
    @Bind(R.id.act_bill_detail_tv_remark)
    TextView mRemark;
    @Bind(R.id.act_bill_detail_rl_advanceamount)
    RelativeLayout mRlAdvamount;
    @Bind(R.id.act_bill_detail_rl_backamount)
    RelativeLayout mRlBackamount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_item_detail);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initData() {
        BillBean billBean = (BillBean) getIntent().getSerializableExtra("BillBean");
        mAmount.setText(billBean.Cash + "");
        if (billBean.DealType==2){
            mRlAdvamount.setVisibility(View.GONE);
            mRlBackamount.setVisibility(View.GONE);
            mType.setText("收入");
        }else if (billBean.DealType==1){
            mType.setText("支出");
            mAdvanceAmount.setText(billBean.AdvanceCash + "");
            mBackamount.setText(billBean.BackCash + "");
        }

        mTime.setText(billBean.CreateTime + "");
        mBusNum.setText(billBean.OrderNo + "");
        mPayway.setText(billBean.PayType + "");
        mRemark.setText(billBean.Remark==null?"":billBean.Remark + "");
    }

    @Override
    public void initViews() {
        super.initViews();
        showBack();
        setTitle("交易明细");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
