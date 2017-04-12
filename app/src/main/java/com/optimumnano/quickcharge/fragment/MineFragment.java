package com.optimumnano.quickcharge.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.AboutActivity;
import com.optimumnano.quickcharge.activity.mineinfo.MineInfoAct;
import com.optimumnano.quickcharge.activity.mineinfo.MineWalletAct;
import com.optimumnano.quickcharge.activity.mineinfo.MyCollectActivity;
import com.optimumnano.quickcharge.activity.setting.SettingActivity;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.manager.EventManager;
import com.optimumnano.quickcharge.utils.SPConstant;
import com.optimumnano.quickcharge.utils.SharedPreferencesUtil;
import com.optimumnano.quickcharge.views.MenuItem1;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.optimumnano.quickcharge.utils.SPConstant.KEY_USERINFO_HEADIMG_URL;
import static com.optimumnano.quickcharge.utils.SPConstant.SP_USERINFO;

/**
 * 我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private ImageView ivHead;
    private MenuItem1 mineSetting,mineAbout,mineCollect;
    private MenuItem1 mywallet;
    private TextView mTvBalance;
    private TextView mTvNickName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                mainView = inflater.inflate(R.layout.fragment_mine, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void initViews() {
        ivHead = (ImageView) mainView.findViewById(R.id.mine_ivHead);
        ivHead.setOnClickListener(this);
        mineSetting = (MenuItem1) mainView.findViewById(R.id.mine_setting);
        mineSetting.setOnClickListener(this);
        mywallet = (MenuItem1) mainView.findViewById(R.id.frag_mine_mi_mywallet);
        mineAbout = (MenuItem1) mainView.findViewById(R.id.mine_about);
        mineCollect = (MenuItem1) mainView.findViewById(R.id.mine_collect);
        mTvBalance = (TextView) mainView.findViewById(R.id.mine_tv_balance);
        mTvNickName = (TextView) mainView.findViewById(R.id.mine_tv_nickname);

        mywallet.setOnClickListener(this);
        mineAbout.setOnClickListener(this);
        mineCollect.setOnClickListener(this);

        initUserInfo();
    }

    private void initUserInfo() {
        String headimgurl = SharedPreferencesUtil.getValue(SP_USERINFO, KEY_USERINFO_HEADIMG_URL, "");
        Glide.with(getActivity())
                .load(headimgurl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.icon_text_tip).into(ivHead);
        String balance = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_BALANCE, "");
        mTvBalance.setText(balance);
        String nickName = SharedPreferencesUtil.getValue(SPConstant.SP_USERINFO, SPConstant.KEY_USERINFO_NICKNAME, "");
        mTvNickName.setText(nickName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_ivHead:
                startActivity(new Intent(getActivity(), MineInfoAct.class));
                break;
            case R.id.mine_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.frag_mine_mi_mywallet:
                startActivity(new Intent(getActivity(), MineWalletAct.class));
                break;
            case R.id.mine_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.mine_collect:
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBalanceChangeEvent(EventManager.onBalanceChangeEvent event) {
        mTvBalance.setText(event.balance);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoChangeEvent(EventManager.onUserInfoChangeEvent event) {
        initUserInfo();
    }
}
