package com.optimumnano.quickcharge.activity.mineinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseActivity;
import com.optimumnano.quickcharge.manager.ModifyUserInformationManager;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_HEADIMG;
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
    private ModifyUserInformationManager mManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance);
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
        setTitle("余额");

        //TODO 获取网络数据
        String url = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_HEADIMG, "");
        if (!TextUtils.isEmpty(url)) {
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeFile(url);
            mHeadview.setImageBitmap(bmp);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.act_wallet_balance_deposit, R.id.act_wallet_balance_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_wallet_balance_deposit:
                showToast("充值");
                break;
            case R.id.act_wallet_balance_withdraw:
                showToast("提现");
                break;
        }
    }
}
