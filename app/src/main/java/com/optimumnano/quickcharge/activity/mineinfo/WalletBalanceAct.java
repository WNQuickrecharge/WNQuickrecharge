package com.optimumnano.quickcharge.activity.mineinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.bean.UserAccount;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.manager.GetMineInfoManager;
import com.optimumnano.quickcharge.net.ManagerCallback;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.utils.StringUtils;
import com.optimumnano.quickcharge.views.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_HEADIMG_URL;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * 作者：邓传亮 on 2017/4/7 15:47
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class WalletBalanceAct extends BaseActivity {

    @Bind(R.id.act_wallet_balance_headview)
    CircleImageView mHeadview;
    @Bind(R.id.act_wallet_balance_value)
    TextView mBalanceValue;
    @Bind(R.id.act_wallet_balance_deposit)
    TextView mDeposit;
    @Bind(R.id.act_wallet_balance_withdraw)
    TextView mWithdraw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        GetMineInfoManager.getAccountInfo(new ManagerCallback() {
            @Override
            public void onSuccess(Object returnContent) {
                super.onSuccess(returnContent);
                String s = returnContent.toString();
                UserAccount userAccount = JSON.parseObject(s, UserAccount.class);
                double restCash = userAccount.getRestCash();
                mBalanceValue.setText(StringUtils.formatDouble(restCash));
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        setRightTitle("");
        showBack();
        setTitle("余额");

        String headimgurl = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_HEADIMG_URL, "");
        Glide.with(WalletBalanceAct.this)
                .load(headimgurl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.wd).into(mHeadview);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.act_wallet_balance_deposit, R.id.act_wallet_balance_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_wallet_balance_deposit:
                startActivity(new Intent(WalletBalanceAct.this, WalletDepositAct.class));
                break;
            case R.id.act_wallet_balance_withdraw://提现 暂时不做的功能
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBalanceChangeEvent(EventManager.onBalanceChangeEvent event) {
        mBalanceValue.setText(event.balance);
    }
}
