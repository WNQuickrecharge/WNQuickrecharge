package com.optimumnano.quickcharge.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.adapter.MPagerAdapter;
import com.optimumnano.quickcharge.animation.ZoomOutPageTransformer;
import com.optimumnano.quickcharge.base.BaseFragment;
import com.optimumnano.quickcharge.views.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mfwn on 2017/4/21.
 */

public class RechargerViewPagerFrag extends BaseFragment {
    private CustomViewPager viewPager;
    private View mainView;
    private List<Fragment> mFragments = new ArrayList<>();

    public CustomViewPager getViewPager() {
        return viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.frag_recharge_viewpager, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }
    private void initView() {
        viewPager= (CustomViewPager) getActivity().findViewById(R.id.frag_recharge_viewpager);
        mFragments.add(new RechargeFragment());
        mFragments.add(new RechargerListFrag(null));
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
    }
    private void initData() {
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(new MPagerAdapter(getChildFragmentManager(), mFragments));
    }


}
