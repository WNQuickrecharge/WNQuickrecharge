package com.optimumnano.quickcharge.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.activity.mineinfo.MineInfoAct;
import com.optimumnano.quickcharge.activity.setting.SettingActivity;
import com.optimumnano.quickcharge.activity.mineinfo.MineWalletAct;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.views.MenuItem1;

/**
 * 我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private ImageView ivHead;
    private MenuItem1 mineSetting;
    private MenuItem1 mywallet;

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
    }

    private void initViews() {
        ivHead = (ImageView) mainView.findViewById(R.id.mine_ivHead);
        ivHead.setOnClickListener(this);
        mineSetting = (MenuItem1) mainView.findViewById(R.id.mine_setting);
        mineSetting.setOnClickListener(this);
        mywallet = (MenuItem1) mainView.findViewById(R.id.frag_mine_mi_mywallet);
        mywallet.setOnClickListener(this);
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
            default:
                break;
        }
    }
}
