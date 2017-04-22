package com.optimumnano.quickcharge.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 作者：凌章 on 16/7/26 20:40
 * 邮箱：lilingzhang@longshine.com
 */

public class MPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> list;

    public MPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list=list;
    }//写构造方法，方便赋值调用
    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }//根据Item的位置返回对应位置的Fragment，绑定item和Fragment

    @Override
    public int getCount() {
        return list.size();
    }//设置Item的数量
}
