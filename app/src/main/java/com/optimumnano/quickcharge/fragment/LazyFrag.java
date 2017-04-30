package com.optimumnano.quickcharge.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by chenwenguang on 2017/4/28.
 */

public abstract class LazyFrag extends Fragment{
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
            lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类需要时重写此方法
     */
    protected  void lazyLoad(){}
}
