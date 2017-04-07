package com.optimumnano.quickcharge.activity.mineinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.views.MenuItem1;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：邓传亮 on 2017/4/7 11:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class MineWalletAct extends BaseActivity {

    @Bind(R.id.act_mineinfo_wallet_mi_balance)
    MenuItem1 mBalance;
    @Bind(R.id.act_mineinfo_wallet_mi_trans_Bill)
    MenuItem1 mTransBill;
    @Bind(R.id.act_mineinfo_wallet_tv_payment)
    TextView mPayment;
    @Bind(R.id.act_mineinfo_wallet_mi_mycard)
    MenuItem1 mMycard;
    private ModifyUserInformationManager mManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineinfo_wallet);
        ButterKnife.bind(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mManager = new ModifyUserInformationManager();
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("我的钱包");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.act_mineinfo_wallet_mi_balance, R.id.act_mineinfo_wallet_mi_trans_Bill, R.id.act_mineinfo_wallet_tv_payment, R.id.act_mineinfo_wallet_mi_mycard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_mineinfo_wallet_mi_balance:
                break;
            case R.id.act_mineinfo_wallet_mi_trans_Bill:
                break;
            case R.id.act_mineinfo_wallet_tv_payment:
                /*Drawable drawable= getResources().getDrawable(R.drawable.zfb);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mPayment.setCompoundDrawables(drawable,null,null,null);*/
                break;
            case R.id.act_mineinfo_wallet_mi_mycard:
                break;
        }
    }
}
